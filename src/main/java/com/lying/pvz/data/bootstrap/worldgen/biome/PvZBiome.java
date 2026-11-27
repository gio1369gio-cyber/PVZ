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
    public static final ResourceKey<Biome> ANCIENT_EGYPT = createKey("ancient_egypt");
    public static final ResourceKey<Biome> PIRATE_HARBOR = createKey("pirate_harbor");
    public static final ResourceKey<Biome> WILD_WEST = createKey("wild_west");
    public static final ResourceKey<Biome> FUTURE_WORLD = createKey("future_world");

    public static ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, StringUtil.id(name));
    }


    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<WorldCarver> worldCarver = context.lookup(Registries.CARVER);

        context.register(ZEN_GARDEN, PvZBiomeData.zenGarden(placedFeature,worldCarver));
        context.register(ANCIENT_EGYPT, PvZBiomeData.ancientEgypt(placedFeature,worldCarver));
        context.register(PIRATE_HARBOR, PvZBiomeData.pirateHarbor(placedFeature,worldCarver));
        context.register(WILD_WEST, PvZBiomeData.wildWest(placedFeature,worldCarver));
        context.register(FUTURE_WORLD, PvZBiomeData.futureWorld(placedFeature,worldCarver));
    }
}
