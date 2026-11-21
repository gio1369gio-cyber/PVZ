package com.lying.pvz.data.bootstrap.worldgen.biome;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.tag.PvZBiomeTags;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZPlaceFeature;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class PvZBiomeModifiers {
    // ============ 矿物 ============
    public static final ResourceKey<BiomeModifier> ADD_AMETHYST =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_amethyst"));
    public static final ResourceKey<BiomeModifier> ADD_LUNAR_STONE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_lunar_stone"));
    public static final ResourceKey<BiomeModifier> ADD_ORIGIN_ORE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_origin_ore"));
    public static final ResourceKey<BiomeModifier> ADD_DOUBLE_ORIGIN_ORE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_double_origin_ore"));
    public static final ResourceKey<BiomeModifier> ADD_NUT_TREE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_nut_tree"));

    // ============ 刷怪 ============
    public static final ResourceKey<BiomeModifier> ADD_SUN_SPAWNS =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_sun_spawns"));
    public static final ResourceKey<BiomeModifier> ADD_GIGA_TOMB_SPAWNS =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_giga_tomb_spawns"));
    public static final ResourceKey<BiomeModifier> ADD_LAVA_ZOMBIE_SPAWNS =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, StringUtil.id("add_lava_zombie_spawns"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {

        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

        // ================= 矿物 =================
        context.register(ADD_AMETHYST, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placed.getOrThrow(PvZPlaceFeature.ORE_AMETHYST)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_LUNAR_STONE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(PvZPlaceFeature.LUNAR_STONE)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_ORIGIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placed.getOrThrow(PvZPlaceFeature.ORE_ORIGIN)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_DOUBLE_ORIGIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(PvZBiome.ZEN_GARDEN)),
                HolderSet.direct(placed.getOrThrow(PvZPlaceFeature.ORE_DOUBLE_ORIGIN)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        // ================= 树 =================
        context.register(ADD_NUT_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(PvZBiome.ZEN_GARDEN)),
                HolderSet.direct(placed.getOrThrow(PvZPlaceFeature.NUT_TREE)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        // ================= 刷怪 =================

        // ================= 刷怪 =================
        context.register(ADD_SUN_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                WeightedList.<MobSpawnSettings.SpawnerData>of(
                        new Weighted<>(
                                new MobSpawnSettings.SpawnerData(
                                        PvZEntityRegister.SUN.get(),
                                        ConstantInt.of(1)), 150
                        )
                )
        ));

        context.register(ADD_GIGA_TOMB_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(PvZBiomeTags.IS_LAND),
                WeightedList.<MobSpawnSettings.SpawnerData>of(
                        new Weighted<>(
                                new MobSpawnSettings.SpawnerData(
                                        PvZEntityRegister.GIGA_TOMB_STONE.get(),
                                        ConstantInt.of(1)), 5
                        )
                )
        ));

        context.register(ADD_LAVA_ZOMBIE_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                WeightedList.<MobSpawnSettings.SpawnerData>of(
                        new Weighted<>(
                                new MobSpawnSettings.SpawnerData(
                                        PvZEntityRegister.LAVA_ZOMBIE.get(),
                                        UniformInt.of(1, 2)), 10
                        )
                )
        ));
    }
}
