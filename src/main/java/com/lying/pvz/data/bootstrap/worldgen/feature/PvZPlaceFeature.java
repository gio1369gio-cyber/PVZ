package com.lying.pvz.data.bootstrap.worldgen.feature;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PvZPlaceFeature {

    public static final ResourceKey<PlacedFeature> ORE_AMETHYST = createKey("ore_amethyst");

    public static final ResourceKey<PlacedFeature> LUNAR_STONE = createKey("lunar_stone");

    public static final ResourceKey<PlacedFeature> ORE_DOUBLE_ORIGIN = createKey("ore_double_origin");

    public static final ResourceKey<PlacedFeature> ORE_ORIGIN = createKey("ore_origin");

    public static final ResourceKey<PlacedFeature> NUT_TREE =createKey("nut_tree");

    public static final ResourceKey<PlacedFeature> PATCH_CHOMPER_PLANT = createKey("patch_chomper_plant");


    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);
        Holder<Feature> oreAmethyst = configuredFeatures.getOrThrow(PvZFeatures.ORE_AMETHYST);
        Holder<Feature> lunarStone = configuredFeatures.getOrThrow(PvZFeatures.LUNAR_STONE);
        Holder<Feature> oreDoubleOrigin = configuredFeatures.getOrThrow(PvZFeatures.ORE_DOUBLE_ORIGIN);
        Holder<Feature> oreOrigin = configuredFeatures.getOrThrow(PvZFeatures.ORE_ORIGIN);
        Holder<Feature> nutTree = configuredFeatures.getOrThrow(PvZFeatures.NUT_TREE);
        Holder<Feature> patchChomperPlant = configuredFeatures.getOrThrow(PvZFeatures.PATCH_CHOMPER_PLANT);

        /**
         * 矿石
         */
        context.register(ORE_AMETHYST,new PlacedFeature(oreAmethyst,
                List.of(
                        CountPlacement.of(15),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)),
                        BiomeFilter.biome()
                )));

        context.register(LUNAR_STONE,new PlacedFeature(lunarStone,
                List.of(
                        CountPlacement.of(30),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)),
                        BiomeFilter.biome()
                )));

        context.register(ORE_DOUBLE_ORIGIN,new PlacedFeature(oreDoubleOrigin,
                List.of(
                        CountPlacement.of(10),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(200)),
                        BiomeFilter.biome()
                )));

        context.register(ORE_ORIGIN,new PlacedFeature(oreOrigin,
                List.of(
                        CountPlacement.of(5),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(200)),
                        BiomeFilter.biome()
                )));

        /**
         * 树
         */
        context.register(NUT_TREE, new PlacedFeature(nutTree,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.matchesBlocks(Direction.DOWN, Blocks.GRASS_BLOCK, Blocks.DIRT),
                                BlockPredicate.replaceable()
                        )),
                        BiomeFilter.biome()
                )
        ));

        /**
         *食人花丛
         */
        context.register(PATCH_CHOMPER_PLANT, new PlacedFeature(patchChomperPlant,
                List.of(
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(32),
                        CountPlacement.of(64),
                        OffsetPlacement.ofTriangle(2, 2),
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.matchesBlocks(Direction.DOWN, new Block[]{Blocks.GRASS_BLOCK}),
                                BlockPredicate.replaceable()
                        )),
                        BiomeFilter.biome()
                )
        ));

    }


}
