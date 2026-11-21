package com.lying.pvz.api.entity.ability;

/**
 * 实体所属阵营。
 *
 * <p>默认攻击逻辑：
 * <ul>
 *   <li>{@link #PLANTS} 优先攻击 {@link #ZOMBIES} 及 {@link #OTHER_MONSTERS}；被其他阵营攻击时反击。</li>
 *   <li>{@link #ZOMBIES} 优先攻击 {@link #PLANTS} 及 {@link #OTHER_GUARDIANS}；被其他阵营攻击时反击。</li>
 *   <li>{@link #NEUTRALS} 中立，被动攻击才反击。</li>
 *   <li>同阵营实体之间不会互相攻击。</li>
 * </ul>
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 被 NBT / 网络持久化。
 * <b>新值只能追加在末尾</b>，不能在中间插入或重排。
 */
public enum GroupType {

    /**
     * 除 PVZ 僵尸外的其他怪物。
     */
    OTHER_MONSTERS,

    /**
     * PVZ 僵尸。
     */
    ZOMBIES,

    /**
     * 中立生物。
     */
    NEUTRALS,

    /**
     * 植物。
     */
    PLANTS,

    /**
     * 铁傀儡、雪傀儡、狼等。
     */
    OTHER_GUARDIANS
}