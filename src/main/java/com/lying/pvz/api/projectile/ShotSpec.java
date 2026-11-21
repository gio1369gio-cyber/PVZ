package com.lying.pvz.api.projectile;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 一次发射的<b>可变配置</b>。
 *
 * <p>由植物 / 道具在 {@code ICanShoot#configureShot} 中填写；随后
 * {@code ShotResolver} 把它与发射者技能、卡牌附魔一起解析成不可变的
 * {@code BulletShot}。飞行中不再修改。
 *
 * <p>约定：
 * <ul>
 *   <li>未设置的数值使用 {@link BulletKind} 的默认值（{@code -1} 哨兵）；</li>
 *   <li>效果这里只放模板，每个目标命中时复制；</li>
 *   <li>标记只写 id + 层数，duration / 上限由 {@code MarkRegistry} 查表。</li>
 * </ul>
 */
public final class ShotSpec {

    private int variant;
    private float baseDamage = -1.0F;
    private float damageAdd;
    private float damageMultiplier = 1.0F;
    private float speed = -1.0F;
    private int lifeTicks = -1;
    private int pierce = Integer.MIN_VALUE;
    private int knockback;
    private double angleOffset;
    private final List<MobEffectInstance> hitEffects = new ArrayList<>();
    private final List<MobEffectInstance> splashEffects = new ArrayList<>();
    private final List<MarkRequest> marks = new ArrayList<>();
    private final List<ItemStack> drops = new ArrayList<>();

    /** 标记请求。duration / maxStacks 从 {@code MarkRegistry} 取。 */
    public record MarkRequest(Identifier mark, int stacks) {

        public MarkRequest {
            Objects.requireNonNull(mark, "mark");
            if (stacks <= 0) {
                throw new IllegalArgumentException("stacks must be > 0");
            }
        }
    }

    public int variant() {
        return this.variant;
    }

    public ShotSpec variant(int variant) {
        this.variant = variant;
        return this;
    }

    public boolean hasBaseDamageOverride() {
        return this.baseDamage >= 0.0F;
    }

    public float baseDamage() {
        return this.baseDamage;
    }

    /** 覆盖 Kind 的基础伤害（植物技能通常走这里）。 */
    public ShotSpec baseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
        return this;
    }

    public float damageAdd() {
        return this.damageAdd;
    }

    public float damageMultiplier() {
        return this.damageMultiplier;
    }

    public ShotSpec damage(float add, float multiplier) {
        this.damageAdd = add;
        this.damageMultiplier = multiplier;
        return this;
    }

    public boolean hasSpeedOverride() {
        return this.speed >= 0.0F;
    }

    public float speed() {
        return this.speed;
    }

    public ShotSpec speed(float speed) {
        this.speed = speed;
        return this;
    }

    public boolean hasLifeOverride() {
        return this.lifeTicks > 0;
    }

    public int lifeTicks() {
        return this.lifeTicks;
    }

    public ShotSpec lifeTicks(int lifeTicks) {
        this.lifeTicks = lifeTicks;
        return this;
    }

    public boolean hasPierceOverride() {
        return this.pierce != Integer.MIN_VALUE;
    }

    public int pierce() {
        return this.pierce;
    }

    public ShotSpec pierce(int pierce) {
        this.pierce = pierce;
        return this;
    }

    public double angleOffset() {
        return this.angleOffset;
    }

    public int knockback() {
        return this.knockback;
    }

    /** 命中击退等级；0 表示不击退。 */
    public ShotSpec knockback(int knockback) {
        this.knockback = knockback;
        return this;
    }

    /** 多发射击的绕 Y 角度偏移（度）。 */
    public ShotSpec angleOffset(double angleOffset) {
        this.angleOffset = angleOffset;
        return this;
    }

    public List<MobEffectInstance> hitEffects() {
        return this.hitEffects;
    }

    public ShotSpec hitEffect(@Nullable MobEffectInstance effect) {
        if (effect != null) {
            this.hitEffects.add(effect);
        }
        return this;
    }

    public List<MobEffectInstance> splashEffects() {
        return this.splashEffects;
    }

    public ShotSpec splashEffect(@Nullable MobEffectInstance effect) {
        if (effect != null) {
            this.splashEffects.add(effect);
        }
        return this;
    }

    public List<MarkRequest> marks() {
        return this.marks;
    }

    public ShotSpec mark(Identifier mark, int stacks) {
        this.marks.add(new MarkRequest(mark, stacks));
        return this;
    }

    public List<ItemStack> drops() {
        return this.drops;
    }

    /** 命中/爆炸时掉落的物品模板。 */
    public ShotSpec drop(@Nullable ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            this.drops.add(stack.copy());
        }
        return this;
    }

    /** 复制一份，供三线 / 分裂等多发场景逐发设置角度。 */
    public ShotSpec copy() {
        final ShotSpec copy = new ShotSpec();
        copy.variant = this.variant;
        copy.baseDamage = this.baseDamage;
        copy.damageAdd = this.damageAdd;
        copy.damageMultiplier = this.damageMultiplier;
        copy.speed = this.speed;
        copy.lifeTicks = this.lifeTicks;
        copy.pierce = this.pierce;
        copy.knockback = this.knockback;
        copy.angleOffset = this.angleOffset;
        copy.hitEffects.addAll(this.hitEffects);
        copy.splashEffects.addAll(this.splashEffects);
        copy.marks.addAll(this.marks);
        for (ItemStack drop : this.drops) {
            copy.drops.add(drop.copy());
        }
        return copy;
    }
}
