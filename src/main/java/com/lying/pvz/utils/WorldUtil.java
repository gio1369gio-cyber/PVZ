package com.lying.pvz.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class WorldUtil {

	public static BlockPos getSuitableHeightRandomPos(Level world, BlockPos pos, int minR, int maxR) {
		BlockPos offset = MathUtil.getRandomRangePos(world.getRandom(), minR, maxR);
		return getSuitableHeightPos(world, pos.offset(offset.getX(), 0, offset.getZ()));
	}
	public static BlockPos getSuitableHeightRandomPos(Level world, BlockPos pos, int maxR) {
		return getSuitableHeightRandomPos(world, pos, 0, maxR);
	}


	public static BlockPos getSuitableHeightPos(Level world, BlockPos pos) {
		int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
		return new BlockPos(pos.getX(), y, pos.getZ());
	}


	public static void spawnRandomSpeedParticle(Level world, ParticleOptions type, Vec3 pos, float speed) {
		spawnRandomSpeedParticle(world, type, pos, speed, speed);
	}
	public static void spawnRandomSpeedParticle(Level world, ParticleOptions type, Vec3 pos, float horizontalSpeed, float verticalSpeed) {
		final float speedX = (world.getRandom().nextFloat() - 0.5F) * horizontalSpeed * 2;
		final float speedY = (world.getRandom().nextFloat() - 0.5F) * verticalSpeed * 2;
		final float speedZ = (world.getRandom().nextFloat() - 0.5F) * horizontalSpeed * 2;
		world.addParticle(type, pos.x, pos.y, pos.z, speedX, speedY, speedZ);
	}

}