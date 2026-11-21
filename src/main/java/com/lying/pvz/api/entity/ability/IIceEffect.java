package com.lying.pvz.api.entity.ability;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

/**
 * 冰系状态效果提供者。用于查询"寒冷"与"冰冻"两个状态效果的模板。
 */
public interface IIceEffect {

    Optional<MobEffectInstance> getColdEffect();

    Optional<MobEffectInstance> getFrozenEffect();
}