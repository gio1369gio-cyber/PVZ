package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.grass.TombStoneEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 哀悼僵尸。
 *
 * <p>棺材僵尸召唤的护柩者，本体 48 血，行走 {@link ZombieUtil#WALK_LITTLE_SLOW}、
 * 攻击 {@link ZombieUtil#VERY_LOW}。{@code canLostHand = false} 避免断手影响护柩姿态。
 *
 * <p>攻击时把目标挑起到空中（垂直速度与随机数平方根成正比），若目标本来就在空中，
 * 额外按 20% 最大生命造成伤害。{@link #SHAKE_CD} 控制攻击动画时长，
 * 由 {@link #tick()} 每 tick 递减。
 *
 * <p>死亡时召唤 {@link TombStoneEntity}（波次墓碑），复用 {@code ZombieUtil.onZombieSpawn}
 * 完成宿主数据复制与 rising 标记。
 */
public class MournerZombieEntity extends ZombieEntity {

	private static final EntityDataAccessor<Boolean> RIGHT_SHAKE =
			SynchedEntityData.defineId(MournerZombieEntity.class, EntityDataSerializers.BOOLEAN);
	public static final int SHAKE_CD = 10;

	public MournerZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setRightShake(this.getRandom().nextInt(2) == 0);
		this.canLostHand = false;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(RIGHT_SHAKE, true);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_SLOW);
		}
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(ZombieUtil.VERY_LOW);
		}
	}

	@Override
	public float getLife() {
		return 48;
	}

	@Override
	protected int getDeathTime() {
		return 1;
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entityIn) {
		this.setAttackTime(SHAKE_CD);
		final float scale = 3;
		if (!entityIn.onGround() && entityIn instanceof LivingEntity living) {
			living.hurtServer(level, PvZEntityDamageSource.normal(this),
					EntityUtil.getMaxHealthDamage(living, 0.2F));
		}
		entityIn.setDeltaMovement(0, Math.sqrt(this.getRandom().nextFloat()) * scale, 0);
		return super.doHurtTarget(level, entityIn);
	}

	@Override
	protected PvZEntityDamageSource getZombieAttackDamageSource() {
		return PvZEntityDamageSource.normal(this);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
		}
	}

	@Override
	protected void onRemoveWhenDeath() {
		if (!this.level().isClientSide()) {
			TombStoneEntity tomb = PvZEntityRegister.TOMB_STONE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			ZombieUtil.onZombieSpawn(this, tomb, this.blockPosition());
		}
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setRightShake(input.getBooleanOr("is_right_shake", false));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("is_right_shake", this.isRightShake());
	}

	public boolean isRightShake() {
		return this.entityData.get(RIGHT_SHAKE);
	}

	public void setRightShake(boolean is) {
		this.entityData.set(RIGHT_SHAKE, is);
	}

	@Override
	public IZombieType getZombieType() {
		return CustomZombies.MOURNER_ZOMBIE.get();
	}
}