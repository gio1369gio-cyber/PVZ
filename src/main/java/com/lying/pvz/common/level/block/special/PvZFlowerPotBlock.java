package com.lying.pvz.common.level.block.special;

import com.lying.pvz.common.level.block.AbstractFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PvZFlowerPotBlock extends AbstractFacingBlock {

	protected static final VoxelShape LILY_PAD_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);

	public PvZFlowerPotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return LILY_PAD_AABB;
	}

	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return PathType.BLOCKED;
	}



}