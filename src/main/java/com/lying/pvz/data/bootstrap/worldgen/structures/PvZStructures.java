package com.lying.pvz.data.bootstrap.worldgen.structures;

import com.lying.pvz.common.worldgen.structure.shop.DaveVillaStructure;
import com.lying.pvz.common.worldgen.structure.shop.SunTempleStructure;
import com.lying.pvz.common.worldgen.structure.zombie.BucketHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.DolphinHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.GraveHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.YetiHouseStructure;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class PvZStructures {

    public static final ResourceKey<Structure> BUCKET_HOUSE = register("bucket_house");
    public static final ResourceKey<Structure> DOLPHIN_HOUSE = register("dolphin_house");
    public static final ResourceKey<Structure> GRAVE_HOUSE = register("grave_house");
    public static final ResourceKey<Structure> YETI_HOUSE = register("yeti_house");
    public static final ResourceKey<Structure> DAVE_VILLA = register("dave_villa");
    public static final ResourceKey<Structure> SUN_TEMPLE = register("sun_temple");

    private static ResourceKey<Structure> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        Structure.StructureSettings surfaceSettings = new Structure.StructureSettings(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);

        context.register(BUCKET_HOUSE, new BucketHouseStructure(surfaceSettings));
        context.register(DOLPHIN_HOUSE, new DolphinHouseStructure(surfaceSettings));
        context.register(GRAVE_HOUSE, new GraveHouseStructure(surfaceSettings));
        context.register(YETI_HOUSE, new YetiHouseStructure(surfaceSettings));
        context.register(DAVE_VILLA, new DaveVillaStructure(surfaceSettings));
        context.register(SUN_TEMPLE, new SunTempleStructure(surfaceSettings));
    }
}