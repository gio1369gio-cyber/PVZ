package com.lying.pvz.common.level.block.ore;


import com.lying.pvz.api.content.IEssenceType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EssenceOreBlock extends PVZOreBlock {
    private final Supplier<IEssenceType> essence;


    public EssenceOreBlock(Identifier id, Supplier<IEssenceType> e, int light) {
        super(Properties.ofFullCopy(Blocks.DIAMOND_ORE)
                .strength(9, 9)
                .requiresCorrectToolForDrops()
                .lightLevel(i -> light)
                .setId(ResourceKey.create(Registries.BLOCK, id)));
        this.essence = e;
    }
    public IEssenceType getEssence() {
        return this.essence.get();
    }
}