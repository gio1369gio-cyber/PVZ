package com.lying.pvz.api.entity.ability;

/**
 * 可被迷你化的僵尸。
 *
 * <p>迷你化时生命按 {@link #getMiniLifeRatio()} 缩放，并附加加速 / 减益效果，
 * 由 {@link #onBeMini()} 实现。
 *
 * <p>默认所有僵尸都能被 mini；不需要的僵尸通过 {@code IPAZEntity#canBeMini()}
 * 返回 {@code false} 关闭。
 */
public interface ICanBeMini {

    /**
     * 迷你化时生命倍率。默认 0.6。
     */
    default float getMiniLifeRatio() {
        return 0.6F;
    }

    /**
     * 迷你化时附加效果。由实现类决定加什么药水效果。
     */
    void onBeMini();
}