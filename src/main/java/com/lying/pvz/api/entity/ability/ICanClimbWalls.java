package com.lying.pvz.api.entity.ability;

/**
 * 能攀墙的僵尸。
 *
 * <p>默认所有僵尸撞墙时短暂上浮；特殊僵尸可覆写 {@link #isClimbing()}
 * 加额外条件（如梯子僵尸只在有梯子时攀爬）。
 */
public interface ICanClimbWalls {

    /**
     * 当前是否正在攀墙。
     */
    boolean isClimbing();

    /**
     * 攀墙时向上的速度分量。
     */
    double getClimbSpeed();
}