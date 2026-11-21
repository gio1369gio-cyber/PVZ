package com.lying.pvz.api.entity.ability;

import net.minecraft.world.entity.LivingEntity;

/**
 * 可被车轮类攻击（如压路机）作用的实体。
 */
public interface IHasWheel {

    void spikeWheelBy(LivingEntity entity);
}