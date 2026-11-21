package com.lying.pvz.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockUtil {


	public static AABB getAABB(BlockPos pos, double w, double h) {
		return new AABB(pos.getX() - w, pos.getY() - h, pos.getZ() - w,
				pos.getX() + w, pos.getY() + h, pos.getZ() + w);
	}

	public static double getBlockPosOffset(LevelReader worldReader, BlockPos pos, AABB aabb) {
		AABB blockAABB = new AABB(pos);
		Iterable<VoxelShape> shapes = worldReader.getCollisions(null, blockAABB);
		return 1.0D + Shapes.collide(Direction.Axis.Y, aabb, shapes, -1.0D);
	}
}