package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PVZZombieAttackGoal;
import com.lying.pvz.common.level.entity.ai.navigator.ZombieWaterPathNavigator;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Optional;

/**
 * 海豚骑士僵尸（骑乘态）。
 *
 * <p>本体 60 血，行走 {@link ZombieUtil#WATER_FAST}，在水下通过
 * {@link #shouldUp()} 自动上浮。核心机制：
 * <ul>
 *   <li>{@code DolphinJumpGoal} 在水下且目标距离 8~12 格时起跳，
 *       跳跃次数达上限、上岸、被 {@link PVZPlants#TALL_NUT} 吸引时触发
 *       {@link #separate()}；</li>
 *   <li>{@link #separate()} 分裂为 {@link DolphinRiderZombieEntity}（骑手）
 *       与 {@link ZombieDolphinEntity}（坐骑），宿主 {@code discard()}；</li>
 *   <li>跳跃期间免疫伤害（{@link #isZombieInvulnerableTo}）。</li>
 * </ul>
 *
 * <p>使用 { ZombieWaterPathNavigator} 保证水中寻路；迷你化禁用。
 */
public class DolphinRiderEntity extends PVZZombieEntity {

	private static final float UP_SPEED = 0.05f;
	protected final float HorizontalJumpSpeed = 1.5F;
	protected final float VerticalJumpSpeed = 0.7F;
	protected Vec3 jumpDstPoint = Vec3.ZERO;
	protected int dolphin_jump_cnt;

	public DolphinRiderEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(PathType.WATER, 0);
		this.setIsWholeBody();
		this.canBeMini = false;
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WATER_FAST);
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new PVZZombieAttackGoal(this, true));
		this.goalSelector.addGoal(0, new DolphinJumpGoal(this));
		this.registerTargetGoals();
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide()) {
			if (this.isInWater()) {
				if (this.shouldUp()) {
					Vec3 v = this.getDeltaMovement();
					this.setDeltaMovement(v.x(), UP_SPEED, v.z());
				}
				if (this.dolphin_jump_cnt == this.getMaxJumpCount()) {
					this.separate();
					return;
				}
			}
			if (this.onGround()) {
				this.separate();
				return;
			}
		}
	}

	public void perfromJump() {
		Optional.ofNullable(this.getTarget()).ifPresent(target -> {
			Vec3 vec = MathUtil.getHorizontalNormalizedVec(this.position(), this.jumpDstPoint);
			final double speedXZ = this.HorizontalJumpSpeed + (this.random.nextDouble() - 0.6D) / 3;
			final double speedY = this.VerticalJumpSpeed + (this.random.nextDouble() - 0.3D) / 2;
			this.setDeltaMovement(vec.x * speedXZ, speedY, vec.z * speedXZ);
			EntityUtil.playSound(this, PvZSoundRegister.DOLPHIN_JUMP.get());
		});
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		if (defender instanceof PlantEntity plant) {
			final IPlantType type = plant.getPlantType();
			return type == PVZPlants.TALL_NUT.get();
		}
		return true;
	}

	@Override
	public void attractBy(ICanAttract defender) {
		super.attractBy(defender);
		this.separate();
		EntityUtil.playSound(this, PvZSoundRegister.HAMMER_BONK.get());
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.isDolphinJumping();
	}

	public void separate() {
		DolphinRiderZombieEntity zombie = PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		if (zombie == null) {
			return;
		}
		ZombieUtil.copySummonZombieData(this, zombie);
		EntityUtil.onEntityRandomPosSpawn(this.level(), zombie, this.blockPosition(), 1);
		zombie.setZombieType(VariantType.NORMAL);
		ZombieDolphinEntity dolphin = PvZEntityRegister.ZOMBIE_DOLPHIN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		if (dolphin == null) {
			return;
		}
		ZombieUtil.copySummonZombieData(this, dolphin);
		EntityUtil.onEntityRandomPosSpawn(this.level(), dolphin, this.blockPosition(), 3);
		this.discard();
	}

	public int getMaxJumpCount() {
		return 1;
	}

	protected boolean shouldUp() {
		return this.getFluidHeight(FluidTags.WATER) > this.getEyeHeight() / 2;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.7f, 1.6f);
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.94f;
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new ZombieWaterPathNavigator(this, worldIn);
	}

	@Override
	public float getLife() {
		return 60;
	}

	public boolean isDolphinJumping() {
		return this.getAttackTime() > 0;
	}

	public int getJumpCD() {
		return 20;
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.DOLPHIN_RIDER.get();
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.jumpDstPoint = new Vec3(
				input.getDoubleOr("jump_dst_x", 0),
				input.getDoubleOr("jump_dst_y", 0),
				input.getDoubleOr("jump_dst_z", 0));
		this.dolphin_jump_cnt = input.getIntOr("dolphin_jump_count", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putDouble("jump_dst_x", this.jumpDstPoint.x);
		output.putDouble("jump_dst_y", this.jumpDstPoint.y);
		output.putDouble("jump_dst_z", this.jumpDstPoint.z);
		output.putInt("dolphin_jump_count", this.dolphin_jump_cnt);
	}

	static class DolphinJumpGoal extends Goal {

		private final DolphinRiderEntity zombie;
		private int delayCnt = 0;

		public DolphinJumpGoal(DolphinRiderEntity zombie) {
			this.zombie = zombie;
			this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			if (this.zombie.getAttackTime() > 0) {
				return true;
			}
			if (this.delayCnt > 0) {
				--this.delayCnt;
				return false;
			}
			LivingEntity target = zombie.getTarget();
			int left_jump_chance = this.zombie.getMaxJumpCount() - this.zombie.dolphin_jump_cnt;
			if (!EntityUtil.isEntityValid(target) || !this.zombie.isInWater() || left_jump_chance <= 0) {
				return false;
			}
			double dis = this.zombie.distanceToSqr(target);
			if (dis < 64 || dis > Math.max(120, 120 * left_jump_chance * left_jump_chance)) {
				return false;
			}
			Vec3 vec = MathUtil.getHorizontalNormalizedVec(zombie.position(), target.position())
					.scale(this.zombie.HorizontalJumpSpeed)
					.add(0, this.zombie.VerticalJumpSpeed * 2, 0);
			if (!EntityUtil.canEntityPass(zombie, vec, 10)) {
				this.delayCnt = this.zombie.random.nextInt(50);
				return false;
			}
			this.zombie.jumpDstPoint = target.position();
			this.zombie.setTarget(target);
			return true;
		}

		@Override
		public void start() {
			this.zombie.setAttackTime(zombie.getJumpCD());
			this.zombie.setAggressive(false);
		}

		@Override
		public boolean canContinueToUse() {
			return this.zombie.getAttackTime() > 0;
		}

		@Override
		public void tick() {
			int time = this.zombie.getAttackTime();
			int cd = this.zombie.getJumpCD();
			this.zombie.getLookControl().setLookAt(this.zombie.jumpDstPoint);
			if (time == cd * 3 / 4) {
				this.zombie.perfromJump();
			} else if (time == 1) {
				++this.zombie.dolphin_jump_cnt;
			}
			this.zombie.setAttackTime(Math.max(0, time - 1));
		}
	}
}