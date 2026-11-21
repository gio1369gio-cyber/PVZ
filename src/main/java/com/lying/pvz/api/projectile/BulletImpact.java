package com.lying.pvz.api.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * 命中管线。
 *
 * <p>一发子弹的命中效果由有序的 {@link Step} 组成，由 {@code ShotResolver}
 * 在发射时绑定来源（发射者 UUID / 变体）后放进 {@code BulletEntity}。
 * 默认顺序：
 * <pre>
 * 伤害（读旧标记） → 加标记 → 施加效果 → 溅射（子管线） → 反馈 → 穿透/消失
 * </pre>
 *
 * <p>这样"第一次命中只上标记，第二次才吃加成"是默认行为，而不是特例。
 */
public final class BulletImpact {

    private BulletImpact() {
    }

    public enum Result {
        /** 继续执行后续 step。 */
        CONTINUE,
        /** 命中即销毁，后续 step 不再执行。 */
        CONSUME
    }

    /**
     * 一次命中的上下文。
     *
     * @param target      命中的宿主实体；方块命中为 null
     * @param pos         接触点（已按视觉半径修正）
     * @param normal      接触法线；可能为 null
     * @param blockState  命中方块；实体命中为 null
     * @param direct      是否为直接命中（溅射子管线里为 false）
     */
    public record Hit(
            @Nullable Entity target,
            Vec3 pos,
            @Nullable Vec3 normal,
            @Nullable BlockState blockState,
            boolean direct
    ) {

        public Hit {
            Objects.requireNonNull(pos, "pos");
        }

        public static Hit entity(Entity target, Vec3 pos) {
            return new Hit(target, pos, null, null, true);
        }

        public static Hit block(Vec3 pos, @Nullable Vec3 normal, BlockState state) {
            return new Hit(null, pos, normal, state, true);
        }

        public Hit asSplash() {
            return new Hit(this.target, this.pos, this.normal, this.blockState, false);
        }
    }

    @FunctionalInterface
    public interface Step {
        Result apply(IBullet bullet, Hit hit);
    }

    /** 发射时绑定来源、变体与本次 ShotSpec，返回可执行的 step。 */
    @FunctionalInterface
    public interface Factory {
        Step bind(@Nullable UUID owner, int variant, BulletKind kind, ShotSpec spec);
    }

    /**
     * 伤害修正项。读取目标身上的标记时必须使用绑定到本发子弹的 {@code MarkKey}，
     * 不能读"所有来源"的聚合层。
     */
    @FunctionalInterface
    public interface DamageTerm {
        float apply(float base, IBullet bullet, Hit hit);
    }

    @FunctionalInterface
    public interface DamageTermFactory {
        DamageTerm bind(@Nullable UUID owner, int variant, BulletKind kind, ShotSpec spec);
    }
}
