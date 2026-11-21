package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.level.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZBlockEntityRegister {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PvZMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SunConverterBlockEntity>> SUN_CONVERTER = BLOCK_ENTITY_TYPES.register("sun_converter", () ->
          new  BlockEntityType<>(SunConverterBlockEntity::new, PvZBlockRegister.SUN_CONVERTER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FragmentSpliceBlockEntity>> FRAGMENT_SPLICE = BLOCK_ENTITY_TYPES.register("fragment_splice", () ->
            new  BlockEntityType<>(FragmentSpliceBlockEntity::new, PvZBlockRegister.FRAGMENT_SPLICE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SlotMachineBlockEntity>> SLOT_MACHINE = BLOCK_ENTITY_TYPES.register("slot_machine", () ->
            new  BlockEntityType<>(SlotMachineBlockEntity::new, PvZBlockRegister.SLOT_MACHINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CardFusionBlockEntity>> CARD_FUSION = BLOCK_ENTITY_TYPES.register("card_fusion", () ->
            new  BlockEntityType<>(CardFusionBlockEntity::new, PvZBlockRegister.CARD_FUSION_TABLE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SunFlowerTrophyBlockEntity>> SUNFLOWER_TROPHY = BLOCK_ENTITY_TYPES.register("sunflower_trophy", () ->
            new  BlockEntityType<>(SunFlowerTrophyBlockEntity::new, PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get(), PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get(),
                    PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EssenceAltarBlockEntity>> ESSENCE_ALTAR = BLOCK_ENTITY_TYPES.register("essence_altar", () ->
            new  BlockEntityType<>(EssenceAltarBlockEntity::new, PvZBlockRegister.ESSENCE_ALTAR.get()));

}