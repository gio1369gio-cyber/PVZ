package com.lying.pvz.api.projectile;

import net.minecraft.world.entity.EntityDimensions;

/**
 * 弹药的不变数值。
 *
 * <p>命中盒与渲染尺寸必须同源：{@code BulletVisual} 的 scale 应当与
 * {@link #hitbox()} 对齐，避免再出现"方块渲染 0.8、命中盒 0.5"的偏移。
 *
 * @param baseDamage    基础伤害，不含变体倍率
 * @param speed         初速（格/tick）
 * @param lifeTicks     最大存活 tick；0 表示不限（由状态机自行结束）
 * @param gravity       重力（格/tick²），0 表示无重力
 * @param hitbox        默认命中盒；变体可覆盖
 * @param pierce        初始可穿透次数；-1 为无限，0 为命中即消失
 * @param maxFlyDistance 最大飞行距离（格），0 表示不限
 * @param canHitAirborne 是否能命中气球等空中目标
 */
public record BulletStats(
        float baseDamage,
        float speed,
        int lifeTicks,
        float gravity,
        EntityDimensions hitbox,
        int pierce,
        double maxFlyDistance,
        boolean canHitAirborne
) {

    public BulletStats {
        if (baseDamage < 0.0F) {
            throw new IllegalArgumentException("baseDamage must be >= 0");
        }
        if (speed < 0.0F) {
            throw new IllegalArgumentException("speed must be >= 0");
        }
        if (lifeTicks < 0) {
            throw new IllegalArgumentException("lifeTicks must be >= 0");
        }
        if (gravity < 0.0F) {
            throw new IllegalArgumentException("gravity must be >= 0");
        }
        if (hitbox == null) {
            throw new IllegalArgumentException("hitbox must not be null");
        }
        if (pierce < -1) {
            throw new IllegalArgumentException("pierce must be >= -1");
        }
        if (maxFlyDistance < 0.0D) {
            throw new IllegalArgumentException("maxFlyDistance must be >= 0");
        }
    }
}
