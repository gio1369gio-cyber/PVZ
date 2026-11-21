package com.lying.pvz.api.content.paz;

/**
 * 植物战斗形态。比 {@link PlantCategory} 细一层，决定碰撞、目标选择、发射行为。
 *
 * <p>旧版 {@code shouldCollideWithEntity} 里 {@code instanceof SquashEntity / SpikeWeedEntity}
 * 的判定，现在由 {@link #MELEE_DEFENSE} 配合"可被碰撞规则"接口完成：
 * 基类检查 {@code other} 的战斗形态是否为 {@link #MELEE_DEFENSE}，
 * 再询问 {@code other} 是否允许被本实体碰撞。
 *
 * <p>本枚举的 {@code ordinal()} <b>不</b>被持久化——它只是编译期元数据。
 */
public enum PlantCombatStyle {

    /**
     * 近战攻击型：大嘴花、窝瓜。接触 / 咬合触发伤害。
     */
    MELEE_ATTACK,

    /**
     * 近战防御型：地刺、地刺王。被踩时反伤，占格阻挡。
     */
    MELEE_DEFENSE,

    /**
     * 远程平射：豌豆射手、机枪射手。直线弹道。
     */
    RANGED_DIRECT,

    /**
     * 远程抛物线：卷心菜投手、西瓜投手、玉米加农炮。
     */
    RANGED_LOB,

    /**
     * 主动防御：坚果墙、高坚果、南瓜壳。无攻击，纯血量。
     */
    DEFENSE_ACTIVE,

    /**
     * 无战斗形态：向日葵、咖啡豆、三叶草等特殊植物。
     */
    NONE
}