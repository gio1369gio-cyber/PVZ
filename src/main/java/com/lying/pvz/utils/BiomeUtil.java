package com.lying.pvz.utils;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class BiomeUtil {
    public static boolean isDesert(Holder<Biome> biome) {
        return biome.is(BiomeTags.HAS_DESERT_PYRAMID);
    }
    /** 主世界群系（#minecraft:is_overworld） */
    public static boolean isOverworld(Holder<Biome> biome) {
       return biome.is(BiomeTags.IS_OVERWORLD);
    }

    public static boolean isLand(Holder<Biome> biome) {
        return !biome.is(BiomeTags.IS_OCEAN)
                && !biome.is(BiomeTags.IS_DEEP_OCEAN)
                && !biome.is(BiomeTags.IS_RIVER);
    }
    public static boolean isOcean(Holder<Biome> biome) {
        return biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN);
    }
    public static boolean isConiferous(Holder<Biome> biome) {
        return biome.is(BiomeTags.IS_TAIGA);
    }
    public static boolean isSnowy(Holder<Biome> biome) {
        return biome.is(Biomes.SNOWY_PLAINS)
                || biome.is(Biomes.ICE_SPIKES)
                || biome.is(Biomes.SNOWY_TAIGA)
                || biome.is(Biomes.SNOWY_BEACH)
                || biome.is(Biomes.SNOWY_SLOPES)
                || biome.is(Biomes.FROZEN_PEAKS)
                || biome.is(Biomes.JAGGED_PEAKS)
                || biome.is(Biomes.GROVE);
    }
}

