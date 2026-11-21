package com.lying.pvz.api.entity.ability;

import net.minecraft.world.entity.LivingEntity;

/**
 * 吸引者。实现此接口的实体可以"吸引"其他 PAZ 实体（如防御植物吸引僵尸）。
 *
 * <p>被吸引方通过 {@link ICanBeAttracted#canBeAttractedBy(ICanAttract)} 决定是否接受，
 * 通过 {@link ICanBeAttracted#attractBy(ICanAttract)} 执行吸引。
 */
public interface ICanAttract {

    /**
     * 是否能吸引 {@code target}。
     */
    boolean canAttract(LivingEntity target);

    /**
     * 执行吸引。
     */
    void attract(LivingEntity target);

    /**
     * 吸引范围（方块）。
     */
    float getAttractRange();
}