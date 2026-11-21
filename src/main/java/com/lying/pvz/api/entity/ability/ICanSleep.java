package com.lying.pvz.api.entity.ability;

/**
 * 会睡觉的植物。蘑菇白天睡觉是 PVZ 核心机制，用咖啡豆唤醒。
 *
 * <p>默认实现为"蘑菇且白天"进入睡眠；非蘑菇植物默认不睡。
 * 扩展玩法可覆写 {@link #shouldSleepNow()} 让其他植物也睡。
 */
public interface ICanSleep {

    /**
     * 当前是否应进入睡眠。
     */
    boolean shouldSleepNow();

    /**
     * 当前是否在睡眠中。默认读实体状态位。
     */
    default boolean isSleeping() {
        return false;
    }
}