package com.lying.pvz.api.lottery;

import org.jspecify.annotations.Nullable;

/**
 * 一次抽奖的判定结果。
 *
 * <p>{@code count}：0 表示未中奖，1 表示单抽，2 表示两连，3 表示三连。
 * 未中奖时 {@code entry} 为 {@code null}。
 */
public record LotteryResult(@Nullable SlotEntry entry, int count) {

    /**
     * 是否中奖。
     */
    public boolean win() {
        return this.entry != null;
    }

    /**
     * 未中奖结果。
     */
    public static LotteryResult miss() {
        return new LotteryResult(null, 0);
    }

    /**
     * 中奖结果。
     *
     * @param entry 中奖条目，不能为 {@code null}
     * @param count 连中次数（1 / 2 / 3）
     */
    public static LotteryResult win(SlotEntry entry, int count) {
        return new LotteryResult(entry, count);
    }
}