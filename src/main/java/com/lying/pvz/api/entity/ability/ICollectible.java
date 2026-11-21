package com.lying.pvz.api.entity.ability;

import net.minecraft.world.entity.LivingEntity;

/**
 * 可被收集的实体（阳光、金币等）。
 */
public interface ICollectible {

    boolean canCollectBy(LivingEntity living);

    void onCollect(LivingEntity living);
}