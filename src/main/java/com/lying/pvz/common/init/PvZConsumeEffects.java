package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.level.item.consume.SunReleaseEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZConsumeEffects {

    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPES =
            DeferredRegister.create(Registries.CONSUME_EFFECT_TYPE, PvZMod.MOD_ID);

    public static final DeferredHolder<ConsumeEffect.Type<?>, ConsumeEffect.Type<SunReleaseEffect>> SUN_RELEASE =
            CONSUME_EFFECT_TYPES.register("sun_release",
                    () -> new ConsumeEffect.Type<>(SunReleaseEffect.CODEC, SunReleaseEffect.STREAM_CODEC));
}