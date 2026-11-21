package com.lying.pvz.common.level.entity.zombie.roof;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasWheel;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.api.entity.projectile.IPult;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PultAttackGoal;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.zombie.base.CarZombieEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * 投石车僵尸。
 *
 * <p>继承 {@link CarZombieEntity}，使用 {@link IPult} 契约驱动抛物线投掷：
 * <ul>
 *   <li>每 {@link #getPultCD()} tick 一次投掷，动画时长
 *       {@link #getPultAnimTime()}，动画过半时调用 {@link #pultBullet()}；</li>
 *   <li>累计使用 {@link #getMaxBallUse()} 颗球后不再投掷（
 *       {@link #shouldPult()}）；</li>
 *   <li>目标选择在 28 格射程内，且目标不高于本体 12 格（{@link #checkY}）。</li>
 * </ul>
 *
 * <p>发射的是 {@link com.lying.pvz.common.projectile.BulletKinds#BALL}（LOB 轨迹），速度与角度由弹体自身
 * {@code shootPultBullet} 处理。本体 105 血、行走
 * {@link ZombieUtil#WALK_LITTLE_SLOW}。{@link #spikeWheelBy} 让地刺对本体
 * 造成 2 倍最大生命伤害。
 */
public class CatapultZombieEntity extends CarZombieEntity implements IPult, IHasWheel {

	private static final EntityDataAccessor<Integer> BALL_COUNT =
			SynchedEntityData.defineId(CatapultZombieEntity.class, EntityDataSerializers.INT);

	public CatapultZombieEntity(EntityType<? extends CarZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_SLOW);
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BALL_COUNT, 0);
	}

	@Override
	protected void registerAttackGoals() {
		super.registerAttackGoals();
		this.goalSelector.addGoal(2, new CataPultAttackGoal(this));
	}

	@Override
	protected void registerTargetGoals() {
		this.targetSelector.addGoal(0, new PVZRandomTargetGoal(this, true, true,
				ZombieUtil.NORMAL_TARGET_RANGE, ZombieUtil.NORMAL_TARGET_HEIGHT));
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide() && this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
			if (this.getAttackTime() == this.getPultAnimTime() / 2) {
				this.pultBullet();
			}
		}
	}

	@Override
	public void spikeWheelBy(LivingEntity entity) {
		if (this.level() instanceof ServerLevel serverLevel) {
			this.hurtServer(serverLevel, this.damageSources().thorns(entity),
					EntityUtil.getMaxHealthDamage(this, 2));
		}
	}

	@Override
	public void startPultAttack() {
		this.setAttackTime(this.getPultAnimTime());
	}

	@Override
	public int getPultCD() {
		return 60;
	}

	public int getPultAnimTime() {
		return 20;
	}

	@Override
	public float getPultRange() {
		return 28;
	}

	@Override
	public boolean shouldPult() {
		return this.canNormalUpdate() && this.getBallCount() < this.getMaxBallUse();
	}

	public boolean checkY(LivingEntity target) {
		return this.getY() + 12 >= target.getY() + target.getBbHeight();
	}

	@Override
	public void pultBullet() {
		Optional.ofNullable(this.getTarget()).ifPresent(target -> {
			if (!(this.level() instanceof ServerLevel serverLevel)) {
				return;
			}
			final ShotSpec spec = new ShotSpec().baseDamage(this.getAttackDamage());
			BulletLauncher.launch(serverLevel, PvZEntityRegister.BALL.get(), this,
					this.getEntityGroupType(), BulletKinds.BALL, spec,
					new Vec3(this.getX(), this.getY() + 1.7F, this.getZ()),
					target, null);
			EntityUtil.playSound(this, PvZSoundRegister.BASKETBALL.get());
			this.setBallCount(this.getBallCount() + 1);
		});
	}

	public int getMaxBallUse() {
		return 20;
	}

	public float getAttackDamage() {
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage == null) {
			return (float) Attributes.ATTACK_DAMAGE.value().getDefaultValue();
		}
		return (float) attackDamage.getBaseValue();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 2F);
	}

	@Override
	public float getLife() {
		return 105;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setBallCount(input.getIntOr("ball_count", 0));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("ball_count", this.getBallCount());
	}

	public void setBallCount(int cnt) {
		this.entityData.set(BALL_COUNT, cnt);
	}

	public int getBallCount() {
		return this.entityData.get(BALL_COUNT);
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.CATAPULT_ZOMBIE.get();
	}

	@Override
	public IPAZProjectile createProjectile() {
		throw new UnsupportedOperationException(
				"CatapultZombie uses BulletKind, not IPAZProjectile");
	}

	@Override
	public float getAttackRange() {
		return 5;
	}

	@Override
	public int getAttackInterval() {
		return 5;
	}

	private static final class CataPultAttackGoal extends PultAttackGoal {

		private final CatapultZombieEntity zombie;

		public CataPultAttackGoal(CatapultZombieEntity zombie) {
			super(zombie);
			this.zombie = zombie;
		}

		@Override
		public void stop() {
			this.target = null;
		}

		@Override
		protected boolean checkTarget(LivingEntity target) {
			return super.checkTarget(target)
					&& this.attacker.distanceToSqr(target) <= this.zombie.getPultRange() * this.zombie.getPultRange()
					&& this.zombie.checkY(target);
		}
	}
}