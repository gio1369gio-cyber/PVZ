package com.lying.pvz.data.bootstrap.worldgen.feature;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BlockStateProviders;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class PvZFeatures {

    public static final RuleTest END_STONE = new BlockMatchTest(Blocks.END_STONE);
    public static final RuleTest STONE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest GRASS = new BlockMatchTest(Blocks.GRASS_BLOCK);

    public static final ResourceKey<Feature> ORE_AMETHYST = createKey("ore_amethyst");

    public static final ResourceKey<Feature> LUNAR_STONE = createKey("lunar_stone");

    public static final ResourceKey<Feature> ORE_DOUBLE_ORIGIN = createKey("ore_double_origin");

    public static final ResourceKey<Feature> ORE_ORIGIN = createKey("ore_origin");

    public static final ResourceKey<Feature> NUT_TREE = createKey("nut_tree");

    public static final ResourceKey<Feature> PATCH_CHOMPER_PLANT = createKey("patch_chomper_plant");


    private static ResourceKey<Feature> createKey(String name) {
        return ResourceKey.create(Registries.FEATURE, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<Feature> context) {
        HolderGetter<BlockStateProvider> blockStateProviders = context.lookup(Registries.BLOCK_STATE_PROVIDER);
        Holder<BlockStateProvider> belowTrunkProvider = blockStateProviders.getOrThrow(BlockStateProviders.SOIL_BENEATH_TREE);

        /**
         * 矿石
         */
        context.register(ORE_AMETHYST,  new OreFeature(END_STONE, PvZBlockRegister.AMETHYST_ORE.get().defaultBlockState(), 4));

        context.register(LUNAR_STONE, new OreFeature(STONE, PvZBlockRegister.LUNAR_STONE.get().defaultBlockState(), 4));

        context.register(ORE_DOUBLE_ORIGIN, new OreFeature(GRASS, PvZBlockRegister.ORIGIN_ORE.get().defaultBlockState(), 4));

        context.register(ORE_ORIGIN, new OreFeature(GRASS, PvZBlockRegister.ORIGIN_ORE.get().defaultBlockState(), 4));

        /**
         * 树
         */
        context.register(NUT_TREE,new TreeFeature.Builder(BlockStateProvider.of(PvZBlockRegister.NUT_LOG.get().defaultBlockState()),
                new StraightTrunkPlacer(4,3,3),
                BlockStateProvider.of(PvZBlockRegister.NUT_LEAVES.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2),ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1),belowTrunkProvider).ignoreVines().build());

        /**
         *食人花丛
         */
        context.register(PATCH_CHOMPER_PLANT, new SimpleBlockFeature(BlockStateProvider.of(PvZBlockRegister.CHOMPER.get().defaultBlockState())));
    }

}