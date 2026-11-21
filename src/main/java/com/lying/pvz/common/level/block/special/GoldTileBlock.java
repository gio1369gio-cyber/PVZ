package com.lying.pvz.common.level.block.special;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class GoldTileBlock extends Block {

	public final int lvl;

	public GoldTileBlock(Identifier id,int lvl) {
		super(Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(6.0F, 6.0F)
				.setId(ResourceKey.create(Registries.BLOCK, id)));
		this.lvl = lvl;
	}


}