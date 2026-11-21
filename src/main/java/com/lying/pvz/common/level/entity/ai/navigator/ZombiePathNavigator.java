package com.lying.pvz.common.level.entity.ai.navigator;

import com.lying.pvz.common.level.entity.ai.processor.ZombieNodeProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class ZombiePathNavigator extends GroundPathNavigation {


	public ZombiePathNavigator(Mob p_i45875_1_, Level p_i45875_2_) {
		super(p_i45875_1_, p_i45875_2_);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void followThePath() {


		Vec3 vector3d = this.getTempMobPos();
		this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;

		BlockPos vector3i = this.path.getNextNodePos();
		final double d0 = Math.abs(this.mob.getX() - ((double) vector3i.getX() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
		final double d2 = Math.abs(this.mob.getZ() - ((double) vector3i.getZ() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
		final boolean flag = d0 < (double) this.maxDistanceToWaypoint && d2 < (double) this.maxDistanceToWaypoint;
		if (flag || this.canCutCorner(this.path.getNextNode().type)
				&& this.shouldTargetNextNodeInDirection(vector3d)) {
			this.path.advance();
		}
		this.doStuckDetection(vector3d);
	}

	/**
	 * copy from super.
	 */
	// 1.16.5: Vector3d → 1.21.1: Vec3
	private boolean shouldTargetNextNodeInDirection(Vec3 p_234112_1_) {
		if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
			return false;
		} else {
			Vec3 vector3d = Vec3.atBottomCenterOf(this.path.getNextNodePos());
			if (!p_234112_1_.closerThan(vector3d, 2.0D)) {
				return false;
			} else {
				Vec3 vector3d1 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
				Vec3 vector3d2 = vector3d1.subtract(vector3d);
				Vec3 vector3d3 = p_234112_1_.subtract(vector3d);
				return vector3d2.dot(vector3d3) > 0.0D;
			}
		}
	}

	@Override
	protected PathFinder createPathFinder(int p_179679_1_) {
		this.nodeEvaluator = new ZombieNodeProcessor();
		this.nodeEvaluator.setCanPassDoors(true);
		return new PathFinder(this.nodeEvaluator, p_179679_1_);
	}
}