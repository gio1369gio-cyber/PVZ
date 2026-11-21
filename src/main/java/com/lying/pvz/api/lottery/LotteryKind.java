package com.lying.pvz.api.lottery;

/**
 * 奖池类型。
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 被 NBT / 网络持久化。
 * <b>新值只能追加在末尾</b>，不能在中间插入或重排。
 */
public enum LotteryKind {
    NORMAL,
    ALL_PLANT_CARDS,
    ALL_ZOMBIE_CARDS,
    ALL_SUMMON_CARDS
}