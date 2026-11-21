package com.lying.pvz.data;

import com.lying.pvz.PvZMod;
import com.lying.pvz.compat.modonomicon.datagen.ModonomiconDataGen;
import com.lying.pvz.data.provider.PvZDataMapProvider;
import com.lying.pvz.data.provider.PvZLangProvider;
import com.lying.pvz.data.provider.PvZParticleProvider;
import com.lying.pvz.data.provider.model.PvZModelProvider;
import com.lying.pvz.data.provider.model.PvZSpriteSourceProvider;
import com.lying.pvz.data.provider.tag.PvZBiomeTagsProvider;
import com.lying.pvz.data.provider.tag.PvZBlockTagsProvider;
import com.lying.pvz.data.provider.tag.PvZEntityTypeTagsProvider;
import com.lying.pvz.data.provider.tag.PvZItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class DataGen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> reloadLookup = event.getReloadableLookupProvider();
        CompletableFuture<HolderLookup.Provider> worldLookup = event.getWorldLookupProvider();

        gen.addProvider(true, new PvZSpriteSourceProvider(output, reloadLookup));
        gen.addProvider(true, new PvZModelProvider(output));
        gen.addProvider(true, new PvZParticleProvider(output));

        gen.addProvider(true, new PvZDataMapProvider(output,reloadLookup));

        gen.addProvider(true,new PvZLangProvider(output));

        gen.addProvider(true, new PvZBlockTagsProvider(output, reloadLookup));

        gen.addProvider(true, new PvZItemTagsProvider(output, reloadLookup));

        gen.addProvider(true, new PvZEntityTypeTagsProvider(output, reloadLookup));

        gen.addProvider(true,new PvZBiomeTagsProvider(output,reloadLookup));

        // Modonomicon 是软依赖：仅在已安装时注册指南书的 datagen provider。
        if (ModList.get().isLoaded("modonomicon")) {
            ModonomiconDataGen.register(event);
        }

        DatapackBuiltinEntriesProvider worldGen =  PvZDataPack.worldGen(output,worldLookup);

        gen.addProvider(true,worldGen);

        gen.addProvider(true, PvZDataPack.reloadable(output, worldGen.getRegistryProvider(), reloadLookup));

    }

}
