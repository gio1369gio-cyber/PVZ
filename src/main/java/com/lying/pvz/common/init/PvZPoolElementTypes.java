package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.worldgen.structure.PvZSinglePoolElement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZPoolElementTypes {

    public static final DeferredRegister<StructurePoolElementType<?>> POOL_ELEMENT_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, PvZMod.MOD_ID);

    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<PvZSinglePoolElement>> PVZ_SINGLE =
            POOL_ELEMENT_TYPES.register("pvz_single",
                    () -> (StructurePoolElementType<PvZSinglePoolElement>) () -> PvZSinglePoolElement.CODEC);
}