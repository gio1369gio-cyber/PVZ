package com.lying.pvz.common.level.item.misc;

import com.lying.pvz.common.init.PvZEffectRegister;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class PvZConsumable {
    public static final Consumable REAL_BRAIN =  Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER,200,1),1.0F)).build();

    public static final Consumable ICE_CABBAGE =  Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS,80,1),1.0F)).build();

    public static final Consumable PEPPER =  Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,20,1),1.0F)).build();


    public static final Consumable GARLIC =  Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(PvZEffectRegister.HALITOSIS,300,1),1.0F)).build();


    public static final Consumable TACOS = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    List.of(
                            new MobEffectInstance(MobEffects.SPEED, 200, 1),
                            new MobEffectInstance(MobEffects.JUMP_BOOST, 200, 1),
                            new MobEffectInstance(MobEffects.HASTE, 200, 1),
                            new MobEffectInstance(MobEffects.REGENERATION, 200, 1)
                    ), 1.0F)).build();
}
