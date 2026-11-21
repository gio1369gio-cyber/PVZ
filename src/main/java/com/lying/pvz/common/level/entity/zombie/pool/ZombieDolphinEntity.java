package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PVZZombieAttackGoal;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * 海豚僵尸（坐骑形态）。
 *
 * <p>由 {@link DolphinRiderEntity#separate()} 分裂出的海豚坐骑。本体 12 血，
 * 移动速度 1.2（不受 {@link ZombieUtil} 常量约束），使用
 * {@link WaterBoundPathNavigation} 与自定义 {@link MoveControl} 实现“水中游泳
 * + 跳跃”。{@link ZombieDolphinJumpGoal} 在检测到连续水面与空气时起跳。
 *
 * <p>与普通僵尸的差异：
 * <ul>
 *   <li>{@link #getVariantType()} 固定 {@code NORMAL}，不参与 SUPER/SUN/BEARD 变体；</li>
 *   <li>{@link #getPassengerRidingPosition} 下移 0.5 格，让骑手正确坐在背部；</li>
 *   <li>{@link #canBeRiddenUnderFluidType} 恒为 true，允许水下骑乘；</li>
 *   <li>{@link #checkSpawnObstruction} 委托 {@link LevelReader#isUnobstructed}。</li>
 * </ul>
 */
public class ZombieDolphinEntity extends ZombieEntity {

	public ZombieDolphinEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(PathType.WATER, 0.0F);
		this.moveControl = new MoveHelperController(this);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.canBeMini = false;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BreathAirGoal(this));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 2.0F));
		this.goalSelector.addGoal(5, new ZombieDolphinJumpGoal(this, 10));
		this.goalSelector.addGoal(8, new FollowPlayerRiddenEntityGoal(this, AbstractBoat.class));
		this.goalSelector.addGoal(0, new PVZZombieAttackGoal(this, true));
		this.registerTargetGoals();
	}

	@Override
	public VariantType getVariantType() {
		return VariantType.NORMAL;
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(1.2F);
		}
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1f, 0.7f);
	}

	@Override
	public float getEatDamage() {
		return ZombieUtil.VERY_LOW;
	}

	@Override
	public float getLife() {
		return 12;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.89f;
	}

	@Override
	public Vec3 getPassengerRidingPosition(Entity entity) {
		return super.getPassengerRidingPosition(entity).add(0, -0.5, 0);
	}

	public boolean checkSpawnObstruction(LevelReader worldIn) {
		return worldIn.isUnobstructed(this);
	}

	@Override
	public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
		return true;
	}

	@Override
	public int getAmbientSoundInterval() {
		return 200;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return PvZSoundRegister.DOLPHIN_SAY.get();
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new WaterBoundPathNavigation(this, worldIn);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.ZOMBIE_DOLPHIN.get();
	}

	static class MoveHelperController extends MoveControl {
		private final ZombieDolphinEntity dolphin;

		public MoveHelperController(ZombieDolphinEntity dolphinIn) {
			super(dolphinIn);
			this.dolphin = dolphinIn;
		}

		@Override
		public void tick() {
			if (this.dolphin.isInWater()) {
				this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
			}

			if (this.operation == Operation.MOVE_TO && !this.dolphin.getNavigation().isDone()) {
				final double d0 = this.wantedX - this.dolphin.getX();
				final double d1 = this.wantedY - this.dolphin.getY();
				final double d2 = this.wantedZ - this.dolphin.getZ();
				final double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if (d3 < 2.5000003E-7F) {
					this.mob.setZza(0.0F);
				} else {
					final float f = (float) (Mth.atan2(d2, d0) * (180F / (float) Math.PI)) - 90.0F;
					this.dolphin.setYRot(this.rotlerp(this.dolphin.getYRot(), f, 10.0F));
					this.dolphin.yBodyRot = this.dolphin.getYRot();
					this.dolphin.setYHeadRot(this.dolphin.getYRot());
					final float f1 = (float) (this.speedModifier
							* this.dolphin.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
					if (this.dolphin.isInWater()) {
						this.dolphin.setSpeed(f1 * 0.02F);
						float f2 = -((float) (Mth.atan2(d1,
								Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (180F / (float) Math.PI)));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
						this.dolphin.setXRot(this.rotlerp(this.dolphin.getXRot(), f2, 5.0F));
						final float f3 = Mth.cos(this.dolphin.getXRot() * ((float) Math.PI / 180F));
						final float f4 = Mth.sin(this.dolphin.getXRot() * ((float) Math.PI / 180F));
						this.dolphin.zza = f3 * f1;
						this.dolphin.yya = -f4 * f1;
					} else {
						this.dolphin.setSpeed(f1 * 0.1F);
					}
				}
			} else {
				this.dolphin.setSpeed(0.0F);
				this.dolphin.setXxa(0.0F);
				this.dolphin.setYya(0.0F);
				this.dolphin.setZza(0.0F);
			}
		}
	}

	static class ZombieDolphinJumpGoal extends JumpGoal {
		private static final int[] JUMP_DISTANCES = new int[]{0, 1, 4, 5, 6, 7};
		private final ZombieDolphinEntity dolphin;
		private final int chance;
		private boolean breached;

		public ZombieDolphinJumpGoal(ZombieDolphinEntity zombie, int chance) {
			this.dolphin = zombie;
			this.chance = chance;
		}

		@Override
		public boolean canUse() {
			if (this.dolphin.getRandom().nextInt(this.chance) != 0) {
				return false;
			}
			Direction direction = this.dolphin.getMotionDirection();
			int i = direction.getStepX();
			int j = direction.getStepZ();
			BlockPos blockpos = this.dolphin.blockPosition();
			for (int k : JUMP_DISTANCES) {
				if (!this.canJumpTo(blockpos, i, j, k) || !this.isAirAbove(blockpos, i, j, k)) {
					return false;
				}
			}
			return true;
		}

		private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
			BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
			BlockState state = this.dolphin.level().getBlockState(blockpos);
			return this.dolphin.level().getFluidState(blockpos).is(FluidTags.WATER)
					&& state.getCollisionShape(this.dolphin.level(), blockpos).isEmpty();
		}

		private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
			return this.dolphin.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir()
					&& this.dolphin.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
		}

		@Override
		public boolean canContinueToUse() {
			final double d0 = this.dolphin.getDeltaMovement().y;
			return (!(d0 * d0 < 0.03F) || this.dolphin.getXRot() == 0.0F
					|| !(Math.abs(this.dolphin.getXRot()) < 10.0F) || !this.dolphin.isInWater())
					&& !this.dolphin.onGround();
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}

		@Override
		public void start() {
			Direction direction = this.dolphin.getMotionDirection();
			this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(
					(double) direction.getStepX() * 0.6D, 0.7D,
					(double) direction.getStepZ() * 0.6D));
			this.dolphin.getNavigation().stop();
		}

		@Override
		public void stop() {
			this.dolphin.setXRot(0.0F);
		}

		@Override
		public void tick() {
			final boolean flag = this.breached;
			if (!flag) {
				final FluidState ifluidstate = this.dolphin.level().getFluidState(this.dolphin.blockPosition());
				this.breached = ifluidstate.is(FluidTags.WATER);
			}
			if (this.breached && !flag) {
				this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
			}
			final Vec3 vec3d = this.dolphin.getDeltaMovement();
			if (vec3d.y * vec3d.y < 0.03F && this.dolphin.getXRot() != 0.0F) {
				this.dolphin.setXRot(Mth.rotLerp(0.2F, this.dolphin.getXRot(), 0.0F));
			} else {
				final double d0 = Math.sqrt(vec3d.horizontalDistanceSqr());
				final double d1 = Math.signum(-vec3d.y) * Math.acos(d0 / vec3d.length()) * (180F / (float) Math.PI);
				this.dolphin.setXRot((float) d1);
			}
		}
	}
}