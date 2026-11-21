package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.misc.potion.PvZEffect;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZEffectRegister {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, PvZMod.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> COLD_EFFECT =
            EFFECTS.register("cold", () -> new PvZEffect(MobEffectCategory.HARMFUL, Colors.IRIS_BLUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, name("cold"), -0.05f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> FROZEN_EFFECT =
            EFFECTS.register("frozen", () -> new PvZEffect(MobEffectCategory.HARMFUL, Colors.ELECTRIC_BLUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, name("frozen"), -1f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> EXCITE_EFFECT =
            EFFECTS.register("excite", () -> new PvZEffect(MobEffectCategory.BENEFICIAL, Colors.GOLD_YELLOW));

    public static final DeferredHolder<MobEffect, MobEffect> LIGHT_EYE_EFFECT =
            EFFECTS.register("light_eye", () -> new PvZEffect(MobEffectCategory.BENEFICIAL, Colors.LITTLE_YELLOW1));

    public static final DeferredHolder<MobEffect, MobEffect> BUTTER_EFFECT =
            EFFECTS.register("butter", () -> new PvZEffect(MobEffectCategory.HARMFUL, Colors.LITTLE_YELLOW1)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, name("butter"), -1f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> ENERGETIC_EFFECT =
            EFFECTS.register("energetic", () -> new PvZEffect(MobEffectCategory.BENEFICIAL, Colors.CREEPER_GREEN));

    public static final DeferredHolder<MobEffect, MobEffect> HALITOSIS = EFFECTS.register("halitosis", () ->
            new PvZEffect(MobEffectCategory.BENEFICIAL, 7259200));

    public static final DeferredHolder<MobEffect, MobEffect> GOO_POISON = EFFECTS.register("goo_poison", () ->
            new PvZEffect(MobEffectCategory.HARMFUL, 15350204).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    name("goo"), -0.01f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    private static Identifier name(String path) {
        return StringUtil.id(path);
    }


}