package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.worldgen.structure.shop.DaveVillaStructure;
import com.lying.pvz.common.worldgen.structure.shop.SunTempleStructure;
import com.lying.pvz.common.worldgen.structure.zombie.BucketHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.DolphinHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.GraveHouseStructure;
import com.lying.pvz.common.worldgen.structure.zombie.YetiHouseStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZStructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, PvZMod.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<DaveVillaStructure>> DAVE_VILLA =
            STRUCTURE_TYPES.register("dave_villa", () -> () -> DaveVillaStructure.CODEC);

    public static final DeferredHolder<StructureType<?>, StructureType<GraveHouseStructure>> GRAVE_HOUSE =
            STRUCTURE_TYPES.register("grave_house", () -> () -> GraveHouseStructure.CODEC);

    public static final DeferredHolder<StructureType<?>, StructureType<BucketHouseStructure>> BUCKET_HOUSE =
            STRUCTURE_TYPES.register("bucket_house", () -> () -> BucketHouseStructure.CODEC);

    public static final DeferredHolder<StructureType<?>, StructureType<YetiHouseStructure>> YETI_HOUSE =
            STRUCTURE_TYPES.register("yeti_house", () -> () -> YetiHouseStructure.CODEC);

    // 新增 Dolphin 和 SunTemple 的注册
    public static final DeferredHolder<StructureType<?>, StructureType<DolphinHouseStructure>> DOLPHIN_HOUSE =
            STRUCTURE_TYPES.register("dolphin_house", () -> () -> DolphinHouseStructure.CODEC);

    public static final DeferredHolder<StructureType<?>, StructureType<SunTempleStructure>> SUN_TEMPLE =
            STRUCTURE_TYPES.register("sun_temple", () -> () -> SunTempleStructure.CODEC);
}