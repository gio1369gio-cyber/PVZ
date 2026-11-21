package com.lying.pvz.api.entity.projectile;

/**
 * 弹药的<b>运动学</b>轨迹类型。
 *
 * <p>只描述"怎么飞"，不描述"打谁"。追踪是正交能力，见 {@code IPAZProjectile.isHoming()}：
 * 追踪弹可以是 {@link #DIRECT} + 追踪，也可以是 {@link #LOB} + 追踪。
 *
 * <p>变体切换（如豌豆被火炬树加热从冰变火）不改轨迹，通过 {@link IVariant} 表达。
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 被 NBT / 网络持久化。
 * <b>新值只能追加在末尾</b>，不能在中间插入或重排。
 */
public enum ProjectileTrajectory {

    /**
     * 平射：直线飞行，受空气阻力。代表：豌豆、孢子、烟雾、星星。
     */
    DIRECT,

    /**
     * 抛物线：受重力，按目标点或目标实体解弹道方程。代表：卷心菜、玉米、西瓜。
     */
    LOB
}