package com.lying.pvz.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectUtil {

    public static MobEffectInstance effect(Holder<MobEffect> effect, int time, int lvl) {
        return new MobEffectInstance(effect, time, lvl, false, false);
    }

    public static MobEffectInstance viewEffect(Holder<MobEffect> effect, int time, int lvl) {
        return new MobEffectInstance(effect, time, lvl, true, true);
    }
}