package com.lying.pvz.api.entity.ability;

import net.minecraft.world.damagesource.DamageSource;

/**
 * 会掉落身体部件的僵尸（掉手、掉头）。
 *
 * <p>默认实现按血量阈值触发 {@link #onLostPart(BodyType, DamageSource)}；
 * 子类可覆写自定义掉落条件或效果。
 */
public interface ICanDropBodyParts {

    /**
     * 当前是否可以掉手。
     */
    boolean canLostHand();

    /**
     * 当前是否可以掉头。
     */
    boolean canLostHead();

    /**
     * 当前身体是否缺手。
     */
    boolean hasLostHand();

    /**
     * 当前身体是否缺头。
     */
    boolean hasLostHead();

    /**
     * 掉部件的统一入口。实现层在血量阈值判定通过后调用。
     *
     * @param type   掉的部件
     * @param source 致伤来源
     */
    void onLostPart(BodyType type, DamageSource source);
}