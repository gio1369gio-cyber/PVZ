package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.common.worldgen.biome.PvZIslandBiomeSource;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class PvZDimension {

    public  static final ResourceKey<LevelStem> PVZ_2 = createKey("pvz_2");

    public static ResourceKey<LevelStem> createKey(String name) {
        return ResourceKey.create(Registries.LEVEL_STEM, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<LevelStem> context) {
        HolderGetter<NoiseGeneratorSettings> noise = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<DimensionType> dimensionType  = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        Holder<DimensionType> typeHolder = dimensionType.getOrThrow(PvZDimensionType.PVZ_2);

        Holder<NoiseGeneratorSettings> noiseOrThrow = noise.getOrThrow(PvZNoiseSettings.PVZ_NOISE);

        PvZIslandBiomeSource biomeSource = PvZIslandBiomeSource.create(biomes);

        context.register(PVZ_2,new LevelStem(typeHolder, new NoiseBasedChunkGenerator(biomeSource, noiseOrThrow)));
    }
}
