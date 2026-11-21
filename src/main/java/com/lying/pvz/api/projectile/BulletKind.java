package com.lying.pvz.api.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 一种弹药的<b>不变描述</b>。
 *
 * <p>一个 {@code BulletKind} 对应一个 {@code EntityType}，在 {@code BulletKinds}
 * 中一次性注册；飞行中不会修改。每发子弹的差异放在 {@link ShotSpec} /
 * {@code BulletShot}，由 {@code ShotResolver} 绑定来源后生成。
 *
 * <p><b>变体</b>：{@link VariantTable} 负责 id → 数值/外观映射，以及
 * "经过某触发器后变成哪个变体"的转移表。触发器（如火炬树桩）只发
 * {@link net.minecraft.resources.Identifier} 信号，不知道豌豆有哪些变体。
 */
public final class BulletKind {

    private final Identifier id;
    private final BulletStats stats;
    private final BulletMotion motion;
    private final VariantTable variants;
    private final List<BulletImpact.DamageTermFactory> damageTerms;
    private final List<BulletImpact.Factory> impact;
    private final Set<Identifier> transformerTags;
    private final boolean passThrough;
    private final Predicate<BlockState> ignoreBlocks;
    @Nullable
    private final TrailSpec trail;

    private BulletKind(Builder builder) {
        this.id = builder.id;
        this.stats = Objects.requireNonNull(builder.stats, "stats");
        this.motion = Objects.requireNonNull(builder.motion, "motion");
        this.variants = Objects.requireNonNull(builder.variants, "variants");
        this.damageTerms = List.copyOf(builder.damageTerms);
        this.impact = List.copyOf(builder.impact);
        this.transformerTags = Collections.unmodifiableSet(new LinkedHashSet<>(builder.transformerTags));
        this.passThrough = builder.passThrough;
        this.ignoreBlocks = builder.ignoreBlocks;
        this.trail = builder.trail;
    }

    public Identifier id() {
        return this.id;
    }

    public BulletStats stats() {
        return this.stats;
    }

    public BulletMotion motion() {
        return this.motion;
    }

    public VariantTable variants() {
        return this.variants;
    }

    public List<BulletImpact.DamageTermFactory> damageTerms() {
        return this.damageTerms;
    }

    public List<BulletImpact.Factory> impact() {
        return this.impact;
    }

    public Set<Identifier> transformerTags() {
        return this.transformerTags;
    }

    public boolean passThrough() {
        return this.passThrough;
    }

    /** 命中时忽略的方块（例如烟雾穿过灌木、荆棘无物理）。 */
    public Predicate<BlockState> ignoreBlocks() {
        return this.ignoreBlocks;
    }

    /** 飞行尾迹粒子；无尾迹时返回 null。 */
    @Nullable
    public TrailSpec trail() {
        return this.trail;
    }

    public VariantDef variant(int variant) {
        return this.variants.get(variant);
    }

    /** 经过触发器后应切换到的变体；没有登记则保持原样。 */
    public int nextVariant(int current, Identifier trigger) {
        return this.variants.next(current, trigger);
    }

    public EntityDimensions hitbox(int variant) {
        final VariantDef def = this.variant(variant);
        return def.hitbox() != null ? def.hitbox() : this.stats.hitbox();
    }

    public static Builder builder(Identifier id) {
        return new Builder(id);
    }

    /* ================================================================== */
    /* 变体                                                                */
    /* ================================================================== */

    /**
     * 一个变体。
     *
     * @param id               稳定 id，0 必须是默认变体
     * @param damageMultiplier 伤害倍率
     * @param visualKey        客户端视觉键，由 {@code BulletVisualRegistry} 解析
     * @param hitbox           命中盒覆盖；null 表示沿用 {@link BulletStats#hitbox()}
     * @param damage           伤害源工厂
     * @param effects          命中时施加的效果模板
     */
    public record VariantDef(
            int id,
            float damageMultiplier,
            Identifier visualKey,
            @Nullable EntityDimensions hitbox,
            DamageSourceFactory damage,
            List<EffectSpec> effects
    ) {

        public VariantDef {
            if (id < 0) {
                throw new IllegalArgumentException("variant id must be >= 0");
            }
            if (damageMultiplier < 0.0F) {
                throw new IllegalArgumentException("damageMultiplier must be >= 0");
            }
            Objects.requireNonNull(visualKey, "visualKey");
            Objects.requireNonNull(damage, "damage");
            effects = effects == null ? List.of() : List.copyOf(effects);
        }
    }

    /**
     * 效果模板。命中时用 {@link MobEffectInstance#MobEffectInstance(MobEffectInstance)}
     * 复制一份，避免多个目标共享同一个可变实例。
     */
    public record EffectSpec(MobEffectInstance template) {

        public EffectSpec {
            Objects.requireNonNull(template, "template");
        }

        public MobEffectInstance create() {
            return new MobEffectInstance(this.template);
        }
    }

    /** 变体转移键：{@code from} 变体遇到 {@code trigger} 触发器。 */
    public record TransitionKey(int from, Identifier trigger) {

        public TransitionKey {
            if (from < 0) {
                throw new IllegalArgumentException("from must be >= 0");
            }
            Objects.requireNonNull(trigger, "trigger");
        }
    }

    /**
     * 飞行尾迹粒子。客户端每 tick 在弹药位置生成 {@code count} 个粒子。
     *
     * @param particle 粒子供应器（客户端调用）
     * @param speed    随机初速
     * @param count    每 tick 数量
     */
    public record TrailSpec(Supplier<ParticleOptions> particle, float speed, int count) {

        public TrailSpec {
            Objects.requireNonNull(particle, "particle");
            if (speed < 0.0F) {
                throw new IllegalArgumentException("speed must be >= 0");
            }
            if (count < 0) {
                throw new IllegalArgumentException("count must be >= 0");
            }
        }
    }

    /** 按变体生成伤害源。 */
    @FunctionalInterface
    public interface DamageSourceFactory {
        DamageSource create(IBullet bullet, @Nullable Entity target);
    }

    /* ================================================================== */
    /* Builder                                                             */
    /* ================================================================== */

    public static final class Builder {

        private final Identifier id;
        private BulletStats stats;
        private BulletMotion motion;
        private VariantTable variants;
        private final List<BulletImpact.DamageTermFactory> damageTerms = new ArrayList<>();
        private final List<BulletImpact.Factory> impact = new ArrayList<>();
        private final Set<Identifier> transformerTags = new LinkedHashSet<>();
        private boolean passThrough;
        private Predicate<BlockState> ignoreBlocks = state -> false;
        @Nullable
        private TrailSpec trail;

        private Builder(Identifier id) {
            this.id = Objects.requireNonNull(id, "id");
        }

        public Builder stats(BulletStats stats) {
            this.stats = stats;
            return this;
        }

        public Builder motion(BulletMotion motion) {
            this.motion = motion;
            return this;
        }

        public Builder variants(VariantTable variants) {
            this.variants = variants;
            return this;
        }

        public Builder damageTerm(BulletImpact.DamageTermFactory term) {
            this.damageTerms.add(Objects.requireNonNull(term, "term"));
            return this;
        }

        public Builder impact(BulletImpact.Factory step) {
            this.impact.add(Objects.requireNonNull(step, "step"));
            return this;
        }

        public Builder transformerTag(Identifier tag) {
            this.transformerTags.add(Objects.requireNonNull(tag, "tag"));
            return this;
        }

        public Builder passThrough(boolean passThrough) {
            this.passThrough = passThrough;
            return this;
        }

        public Builder ignoreBlocks(Predicate<BlockState> ignoreBlocks) {
            this.ignoreBlocks = Objects.requireNonNull(ignoreBlocks, "ignoreBlocks");
            return this;
        }

        public Builder trail(@Nullable TrailSpec trail) {
            this.trail = trail;
            return this;
        }

        public BulletKind build() {
            Objects.requireNonNull(this.stats, "stats");
            Objects.requireNonNull(this.motion, "motion");
            Objects.requireNonNull(this.variants, "variants");
            return new BulletKind(this);
        }
    }
}
