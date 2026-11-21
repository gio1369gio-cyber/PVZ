package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZPotionRegister {

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, PvZMod.MOD_ID);

    public static final DeferredHolder<Potion, Potion> EXCITE_POTION_1 =
            POTIONS.register("excite_potion_1", () -> new Potion("excite_potion_1",
                    new MobEffectInstance(PvZEffectRegister.EXCITE_EFFECT, 600, 0)));
    public static final DeferredHolder<Potion, Potion> EXCITE_POTION_2 =
            POTIONS.register("excite_potion_2", () -> new Potion("excite_potion_2",
                    new MobEffectInstance(PvZEffectRegister.EXCITE_EFFECT, 1800, 1)));
    public static final DeferredHolder<Potion, Potion> EXCITE_POTION_3 =
            POTIONS.register("excite_potion_3", () -> new Potion("excite_potion_3",
                    new MobEffectInstance(PvZEffectRegister.EXCITE_EFFECT, 3600, 0)));
    public static final DeferredHolder<Potion, Potion> LIGHT_EYE_POTION_1 =
            POTIONS.register("light_eye_potion_1", () -> new Potion("light_eye_potion_1",
                    new MobEffectInstance(PvZEffectRegister.LIGHT_EYE_EFFECT, 1200, 0)));
    public static final DeferredHolder<Potion, Potion> LIGHT_EYE_POTION_2 =
            POTIONS.register("light_eye_potion_2", () -> new Potion("light_eye_potion_2",
                    new MobEffectInstance(PvZEffectRegister.LIGHT_EYE_EFFECT, 9600, 0)));
}