package com.lying.pvz.data;

import com.lying.pvz.PvZMod;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiomeModifiers;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDimension;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDimensionType;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZFeatures;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZPlaceFeature;
import com.lying.pvz.data.bootstrap.misc.PvZDamageType;
import com.lying.pvz.data.bootstrap.misc.PvZEnchantment;
import com.lying.pvz.data.bootstrap.misc.PvZJukeboxSongs;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZStructureSets;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZStructures;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.lying.pvz.data.provider.PvZAdvancementProvider;
import com.lying.pvz.data.provider.loot.PvZLootTableProvider;
import com.lying.pvz.data.provider.recipe.PvZRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PvZDataPack {

    /** 世界层注册表：biome / feature / structure / enchantment / damage_type 等 */
    public static DatapackBuiltinEntriesProvider worldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> worldLookup) {

        RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(Registries.BIOME, PvZBiome::bootstrap)
                .add(Registries.PLACED_FEATURE, PvZPlaceFeature::bootstrap)
                .add(Registries.FEATURE, PvZFeatures::bootstrap)
                .add(Registries.ENCHANTMENT, PvZEnchantment::bootstrap)
                .add(Registries.JUKEBOX_SONG, PvZJukeboxSongs::bootstrap)
                .add(Registries.STRUCTURE, PvZStructures::bootstrap)
                .add(Registries.STRUCTURE_SET, PvZStructureSets::bootstrap)
                .add(Registries.TEMPLATE_POOL, PvZTemplatePools::bootstrap)
                .add(Registries.DAMAGE_TYPE, PvZDamageType::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, PvZBiomeModifiers::bootstrap)
                .add(Registries.DIMENSION_TYPE, PvZDimensionType::bootstrap)
                .add(Registries.LEVEL_STEM, PvZDimension::bootstrap);

        return DatapackBuiltinEntriesProvider.forWorldLayer(output, "pvz_worldgen",
                worldLookup, builder, Set.of(PvZMod.MOD_ID));
    }

    /** 可重载层注册表：loot_table 以及之后的 advancement / predicate 等 */
    public static DatapackBuiltinEntriesProvider reloadable(PackOutput output, CompletableFuture<HolderLookup.Provider> worldLookup,
            CompletableFuture<HolderLookup.Provider> reloadableLookup) {

        RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(Registries.LOOT_TABLE, new PvZLootTableProvider())
                .add(Registries.ADVANCEMENT, new PvZAdvancementProvider(List.of(PvZAdvancementProvider.Generator::new)))
                .add(RecipeProvider.asBootstrap(PvZRecipeProvider::new));

        return DatapackBuiltinEntriesProvider.forReloadableLayer(output, "pvz_reloadable",
                worldLookup, reloadableLookup, builder, Set.of(PvZMod.MOD_ID));
    }

}