package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Optional;

/**
 * 撑杆僵尸。
 *
 * <p>撑杆跳机制：
 * <ul>
 *   <li>初始 {@code HAS_POLE = true}，同步到客户端时立即获得
 *       {@link MobEffects#SLOWNESS}（永久、等级 0），模拟“持杆减速”；</li>
 *   <li>{@code PoleJumpGoal} 在目标距离 8~10 格（随剩余跳跃次数放大上限）
 *       且能穿越时启动，{@link #perfromJump()} 按水平/垂直分量起跳；</li>
 *   <li>跳跃次数达 {@link #getMaxJumpCount()} 后 {@link #(false)}，
 *       速度恢复；跳过 {@link PVZPlants#TALL_NUT}
 *       时被吸引（{@link #canBeAttractedBy}），触发 {@link #attractBy} 落地并丢杆；</li>
 *   <li>跳跃期间（{@link #isPoleJumping()}）不可被选为目标、免疫伤害。</li>
 * </ul>
 *
 * <p>本体 50 血、行走 {@link ZombieUtil#WALK_FAST}；跳跃冷却 20 tick。
 */
public class PoleZombieEntity extends PVZZombieEntity {

	private static final EntityDataAccessor<Boolean> HAS_POLE =
			SynchedEntityData.defineId(PoleZombieEntity.class, EntityDataSerializers.BOOLEAN);
	protected final float HorizontalJumpSpeed = 1.5F;
	protected final float VerticalJumpSpeed = 0.7F;
	protected Vec3 jumpDstPoint = Vec3.ZERO;
	protected int pole_jump_cnt;

	public PoleZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_POLE, true);
	}

	@Override
	protected void registerAttackGoals() {
		super.registerAttackGoals();
		this.goalSelector.addGoal(0, new PoleJumpGoal(this));
	}

	@Override
	protected void registerTargetGoals() {
		this.targetSelector.addGoal(0, new PVZRandomTargetGoal(this, true, true,
				ZombieUtil.NORMAL_TARGET_RANGE, ZombieUtil.LOW_TARGET_HEIGHT));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		super.onSyncedDataUpdated(data);
		if (data.equals(HAS_POLE)) {
			this.addEffect(EffectUtil.effect(MobEffects.SLOWNESS, 1000000, 0));
		}
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
		if (this.hasPole()) {
			this.setPole(false);
			this.setDeltaMovement(0, 0, 0);
			EntityUtil.playSound(this, PvZSoundRegister.HAMMER_BONK.get());
		}
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return super.canBeTargetBy(living) && !this.isPoleJumping();
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.isPoleJumping();
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setHandDefence(this.hasPole());
	}

	public void perfromJump() {
		Optional.ofNullable(this.getTarget()).ifPresent(target -> {
			Vec3 vec = MathUtil.getHorizontalNormalizedVec(this.position(), this.jumpDstPoint);
			final double speedXZ = this.HorizontalJumpSpeed + (this.random.nextDouble() - 0.3D) / 2;
			final double speedY = this.VerticalJumpSpeed + (this.random.nextDouble() - 0.3D) / 2;
			this.setDeltaMovement(vec.x * speedXZ, speedY, vec.z * speedXZ);
			EntityUtil.playSound(this, PvZSoundRegister.POLE_JUMP.get());
		});
	}

	public int getMaxJumpCount() {
		return 1;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_FAST;
	}

	@Override
	public float getLife() {
		return 50;
	}

	public int getPoleJumpCD() {
		return 20;
	}

	public boolean isPoleJumping() {
		return this.getAttackTime() > 0;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setPole(input.getBooleanOr("zombie_has_pole", true));
		this.jumpDstPoint = new Vec3(
				input.getDoubleOr("jump_dst_x", 0),
				input.getDoubleOr("jump_dst_y", 0),
				input.getDoubleOr("jump_dst_z", 0));
		this.pole_jump_cnt = input.getIntOr("pole_jump_count", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("zombie_has_pole", this.hasPole());
		output.putDouble("jump_dst_x", this.jumpDstPoint.x);
		output.putDouble("jump_dst_y", this.jumpDstPoint.y);
		output.putDouble("jump_dst_z", this.jumpDstPoint.z);
		output.putInt("pole_jump_count", this.pole_jump_cnt);
	}

	public void setPole(boolean has) {
		this.entityData.set(HAS_POLE, has);
	}

	public boolean hasPole() {
		return this.entityData.get(HAS_POLE);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.POLE_ZOMBIE.get();
	}

	static class PoleJumpGoal extends Goal {

		private final PoleZombieEntity zombie;
		private int delayCnt = 0;

		public PoleJumpGoal(PoleZombieEntity zombie) {
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
			LivingEntity target = this.zombie.getTarget();
			int left_jump_chance = this.zombie.getMaxJumpCount() - this.zombie.pole_jump_cnt;
			if (!EntityUtil.isEntityValid(target) || !this.zombie.onGround() || left_jump_chance <= 0) {
				return false;
			}
			double dis = this.zombie.distanceToSqr(target);
			if (dis < 64 || dis > Math.max(100, 100 * left_jump_chance * left_jump_chance)) {
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
			this.zombie.setAttackTime(zombie.getPoleJumpCD());
			this.zombie.setAggressive(false);
		}

		@Override
		public boolean canContinueToUse() {
			return this.zombie.getAttackTime() > 0;
		}

		@Override
		public void tick() {
			int time = this.zombie.getAttackTime();
			int cd = this.zombie.getPoleJumpCD();
			this.zombie.getLookControl().setLookAt(this.zombie.jumpDstPoint);
			if (time == cd * 3 / 4) {
				this.zombie.perfromJump();
			} else if (time == 1) {
				if (++this.zombie.pole_jump_cnt == this.zombie.getMaxJumpCount()) {
					this.zombie.setPole(false);
				}
			}
			this.zombie.setAttackTime(Math.max(0, time - 1));
		}
	}
}