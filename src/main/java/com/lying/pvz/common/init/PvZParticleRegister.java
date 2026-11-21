package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZParticleRegister {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, PvZMod.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_BOMB =
            PARTICLE_TYPES.register("red_bomb", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_BOMB =
            PARTICLE_TYPES.register("yellow_bomb", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIRT_BURST_OUT =
            PARTICLE_TYPES.register("dirt_burst_out", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_FLAME =
            PARTICLE_TYPES.register("yellow_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLAME =
            PARTICLE_TYPES.register("blue_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SLEEP =
            PARTICLE_TYPES.register("sleep", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPORE =
            PARTICLE_TYPES.register("spore", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FUME =
            PARTICLE_TYPES.register("fume", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW_FLOWER =
            PARTICLE_TYPES.register("snow_flower", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DOOM =
            PARTICLE_TYPES.register("doom", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MELON_SLICE =
            PARTICLE_TYPES.register("melon_slice", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FROZEN_MELON_SLICE =
            PARTICLE_TYPES.register("frozen_melon_slice", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_SWEEP =
            PARTICLE_TYPES.register("green_sweep", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POP_CORN =
            PARTICLE_TYPES.register("pop_corn", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOLD_NUGGET =
            PARTICLE_TYPES.register("gold_nugget", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORIGINAL_FUME =
            PARTICLE_TYPES.register("original_fume", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SONIC_BOOM =
            PARTICLE_TYPES.register("sonic_boom", () -> new SimpleParticleType(false));
}