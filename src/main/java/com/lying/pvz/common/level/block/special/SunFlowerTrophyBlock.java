package com.lying.pvz.common.level.block.special;

import com.lying.pvz.common.level.block.AbstractFacingBlock;
import com.lying.pvz.common.level.blockentity.SunFlowerTrophyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SunFlowerTrophyBlock extends AbstractFacingBlock implements EntityBlock {

	public final int lvl;

	public SunFlowerTrophyBlock(Properties properties, int lvl) {
		super(properties.lightLevel(i -> 14));
		this.lvl = lvl;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SunFlowerTrophyBlockEntity(pos, state);
	}

}