package com.lying.pvz.common.list;
import com.lying.pvz.utils.others.WeightList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;


public class EffectList {
    public static final WeightList<MobEffect> EFFECT = new WeightList<>();

    static {
        EFFECT.addItem(MobEffects.JUMP_BOOST.value(), 100);
        EFFECT.addItem(MobEffects.SATURATION.value(), 100);
        EFFECT.addItem(MobEffects.BLINDNESS.value(), 100);
        EFFECT.addItem(MobEffects.POISON.value(), 100);
        EFFECT.addItem(MobEffects.NIGHT_VISION.value(), 100);
        EFFECT.addItem(MobEffects.WEAKNESS.value(), 100);
    }
}
