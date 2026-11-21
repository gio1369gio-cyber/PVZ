package com.lying.pvz.data.provider.tag;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.tag.PvZBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class PvZBiomeTagsProvider extends BiomeTagsProvider {
    public PvZBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PvZMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(PvZBiomeTags.IS_LAND).addTag(BiomeTags.IS_OVERWORLD).remove(BiomeTags.IS_OCEAN).remove(BiomeTags.IS_RIVER);
        this.tag(PvZBiomeTags.IS_SNOWY_LAND).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_TAIGA).add(Biomes.SNOWY_SLOPES);

        this.tag(PvZBiomeTags.HAS_BUCKET_HOUSE).addTag(PvZBiomeTags.IS_LAND);
        this.tag(PvZBiomeTags.HAS_DAVE_VILLA).add(Biomes.PLAINS);
        this.tag(PvZBiomeTags.HAS_DOLPHIN_HOUSE).addTag(BiomeTags.IS_OCEAN);
        this.tag(PvZBiomeTags.HAS_GRAVE_HOUSE).addTag(BiomeTags.IS_TAIGA);
        this.tag(PvZBiomeTags.HAS_SUN_TEMPLE).add(Biomes.DESERT);
        this.tag(PvZBiomeTags.HAS_YETI_HOUSE).addTag(PvZBiomeTags.IS_SNOWY_LAND);

    }
}
