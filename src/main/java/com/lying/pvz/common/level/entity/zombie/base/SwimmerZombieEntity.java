package com.lying.pvz.common.level.entity.zombie.base;

import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

/**
 * 游泳僵尸基类。
 *
 * <p>姿态由水陆与目标距离决定，见 {@link #tick()}：
 * <ul>
 *   <li>水中且目标距离平方 ≤ {@code UP_DISTANCE} → {@link Pose#SPIN_ATTACK}（旋转攻击，碰撞箱 0.7×1.4）；</li>
 *   <li>水中其余情况 → {@link Pose#SWIMMING}（碰撞箱 0.7×0.9）；</li>
 *   <li>上岸 → {@link Pose#STANDING}（碰撞箱 0.7×1.9）。</li>
 * </ul>
 * 迷你化时统一使用 0.5×0.6 的小碰撞箱，优先于上述判断。
 *
 * <p>水中减速 0.94（略高于陆地），路径成本 {@link PathType#WATER} 置 0，避免游泳僵尸绕开水路。
 * 具体僵尸（海豚骑士、浮潜、潜水、海豚等）继承本类决定攀爬/攻击等细节。
 */
public abstract class SwimmerZombieEntity extends ZombieEntity {

	private static final float UP_DISTANCE = 10;

	public SwimmerZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(PathType.WATER, 0);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (this.isInWater()) {
				if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) <= UP_DISTANCE) {
					this.setPose(Pose.SPIN_ATTACK);
				} else {
					this.setPose(Pose.SWIMMING);
				}
			} else {
				this.setPose(Pose.STANDING);
			}
		}
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.5F, 0.6F);
		}
		if (this.getPose() == Pose.SPIN_ATTACK) {
			return EntityDimensions.scalable(0.7F, 1.4F);
		} else if (this.getPose() == Pose.SWIMMING) {
			return EntityDimensions.scalable(0.7F, 0.9F);
		}
		return EntityDimensions.scalable(0.7F, 1.9F);
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.94F;
	}
}