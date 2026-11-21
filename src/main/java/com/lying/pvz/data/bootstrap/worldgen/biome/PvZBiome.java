package com.lying.pvz.data.bootstrap.worldgen.biome;

import com.lying.pvz.common.worldgen.biome.PvZBiomeData;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PvZBiome {

    public  static final ResourceKey<Biome> ZEN_GARDEN = createKey("zen_garden");

    public static ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, StringUtil.id(name));
    }


    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<WorldCarver> worldCarver = context.lookup(Registries.CARVER);

        context.register(ZEN_GARDEN, PvZBiomeData.zenGarden(placedFeature,worldCarver));
    }
}
