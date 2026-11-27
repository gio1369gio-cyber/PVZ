package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.worldgen.biome.PvZIslandBiomeSource;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** 自定义群系源 codec 注册：让 PvZIslandBiomeSource 能写进 LevelStem 的 JSON。 */
public class PvZBiomeSources {

    public static final DeferredRegister<MapCodec<? extends BiomeSource>> SOURCES =
            DeferredRegister.create(Registries.BIOME_SOURCE, PvZMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<PvZIslandBiomeSource>> ISLAND =
            SOURCES.register("islands", () -> PvZIslandBiomeSource.CODEC);

    private PvZBiomeSources() {}
}
