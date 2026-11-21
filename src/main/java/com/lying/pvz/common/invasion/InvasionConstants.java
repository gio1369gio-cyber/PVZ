package com.lying.pvz.common.invasion;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * 入侵波次常量与旧版曲线算法。
 *
 * <p>迁移自旧 {@code InvasionManager} / {@code Invasion}：
 * 当数据包没有显式提供 {@code waves} 时，由 {@link #rollDefaultWaves} 按等级生成。
 */
public final class InvasionConstants {

    public static final int MAX_WAVE_NUM = 10;

    /** 第 i 波的基础刷怪量（旧版曲线）。 */
    public static final int[] SPAWN_COUNT_EACH_WAVE = new int[] {
            10, 12, 16, 20, 24, 30, 36, 42, 44, 46
    };

    private InvasionConstants() { }

    /** {@code [min, max]} 闭区间随机。 */
    public static int randomBetween(RandomSource random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * 按等级滚动本局波数（沿用旧版分段曲线）。
     *
     * @param level 玩家等级（旧版 {@code TREE_LVL}，新版 {@code experienceLevel}）
     */
    public static int rollWaveCount(RandomSource random, int level) {
        final int max = Mth.clamp(level <= 20 ? (level + 9) / 10 : level <= 80 ? (level - 6) / 15 + 2 : level / 20 + 3, 1, MAX_WAVE_NUM);
        final int min = Mth.clamp(level <= 40 ? (level + 19) / 20 : (level + 39) / 40 + 1, 1, max);
        return randomBetween(random, min, max);
    }

    /**
     * 按等级滚动默认波次。
     *
     * <p>时间点：{@code 2000 + i * (20000/count) + rand(20000/count)}；
     * 刷怪量：{@code [2/3 * SPAWN_COUNT_EACH_WAVE[i], SPAWN_COUNT_EACH_WAVE[i]]}。
     * 超出 {@link #SPAWN_COUNT_EACH_WAVE} 长度的波次复用最后一档。
     */
    public static RolledWaves rollDefaultWaves(RandomSource random, int level) {
        final int count = rollWaveCount(random, level);
        final int eachTime = 20000 / count;
        final int[] times = new int[count];
        final int[] spawnCounts = new int[count];
        for (int i = 0; i < count; i++) {
            times[i] = 2000 + i * eachTime + random.nextInt(eachTime);
            final int max = SPAWN_COUNT_EACH_WAVE[Math.min(i, SPAWN_COUNT_EACH_WAVE.length - 1)];
            final int min = max * 2 / 3;
            spawnCounts[i] = randomBetween(random, min, max);
        }
        return new RolledWaves(times, spawnCounts);
    }

    public record RolledWaves(int[] times, int[] counts) { }
}