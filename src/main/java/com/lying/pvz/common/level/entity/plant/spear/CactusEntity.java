package com.lying.pvz.common.level.entity.plant.spear;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * 仙人掌。
 *
 * <p>随目标高度动态调整身体高度（{@link #CACTUS_HEIGHT}，最多 {@link #MAX_SEGMENT_NUM} 段），
 * super 状态下进入 {@link #POWERED}，发射穿透三目标的 ThornProjectile 并伤害 +10。
 *
 * <p>新架构 {@link com.lying.pvz.common.projectile.BulletKinds#THORN} 无 {@code ThornStates.POWER}；旧 POWER 语义
 * （伤害 +10）通过覆写 {@link #getAttackDamage()} 实现。
 */
public class CactusEntity extends PlantShooterEntity {

	private static final EntityDataAccessor<Float> CACTUS_HEIGHT =
			SynchedEntityData.defineId(CactusEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> POWERED =
			SynchedEntityData.defineId(CactusEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float MAX_SEGMENT_NUM = 4;
	public static final float SEGMENT_HEIGHT = 0.54F;
	private static final float MIN_SHOOT_HEIGHT = 1.25F;
	private static final float MAX_SHOOT_HEIGHT = MIN_SHOOT_HEIGHT + MAX_SEGMENT_NUM * SEGMENT_HEIGHT;
	protected static final double SHOOT_OFFSET = 0.3D;

	public CactusEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CACTUS_HEIGHT, 0F);
		builder.define(POWERED, false);
	}

	@Override
	public void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		final LivingEntity target = this.getTarget();
		if (EntityUtil.isEntityValid(target)) {
			if (!this.isSuitableHeight(target)) {
				final float dh = SEGMENT_HEIGHT;
				if (this.getY() < target.getY()) {
					this.setCactusHeight(Math.min(this.getCactusHeight() + dh,
							SEGMENT_HEIGHT * MAX_SEGMENT_NUM));
				} else {
					this.setCactusHeight(Math.max(this.getCactusHeight() - dh, 0));
				}
			}
		} else {
			this.setCactusHeight(0);
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		super.onSyncedDataUpdated(data);
		if (data.equals(CACTUS_HEIGHT)) {
			this.refreshDimensions();
		}
	}

	@Override
	public void shootBullet() {
		this.performShoot(SHOOT_OFFSET, 0, 0,
				this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.THORN;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.THORN.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		final boolean powered = this.isCactusPowered();
		spec.baseDamage(this.getAttackDamage())
				.variant(powered ? BulletKinds.THORN_NORMAL_POWERED : BulletKinds.THORN_NORMAL)
				.pierce(powered ? this.getThornCount() - 1 : 0);
	}

	@Override
	public float getAttackDamage() {
		final float base = this.getSkillValue(SkillTypes.MORE_THORN_DAMAGE.get());
		return this.isCactusPowered() ? base + 10F : base;
	}

	public int getThornCount() {
		return 3;
	}

	public float getCurrentHeight() {
		return this.getCactusHeight() + MIN_SHOOT_HEIGHT;
	}

	private boolean isSuitableHeight(Entity target) {
		final double dx = target.getX() - this.getX();
		final double ly = target.getY() - this.getY() - this.getCurrentHeight();
		final double ry = ly + target.getBbHeight();
		final double dz = target.getZ() - this.getZ();
		final double dis = Math.sqrt(dx * dx + dz * dz);
		final double y = dis / this.getMaxShootAngle();
		return ly <= y && ry >= -y;
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (entity instanceof BalloonZombieEntity) {
			return true;
		}
		return super.canPAZTarget(entity);
	}

	@Override
	public boolean checkY(Entity target) {
		final double dx = target.getX() - this.getX();
		final double dz = target.getZ() - this.getZ();
		final double dis = Math.sqrt(dx * dx + dz * dz);
		final double y = dis / this.getMaxShootAngle();
		return this.getY() + MAX_SHOOT_HEIGHT + y >= target.getY()
				&& this.getY() + MIN_SHOOT_HEIGHT - y <= target.getY() + target.getBbHeight();
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(1);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 2.0F + this.getCactusHeight());
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setCactusPowered(true);
	}

	@Override
	public boolean canStartSuperMode() {
		return super.canStartSuperMode() && !this.isCactusPowered();
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setCactusPowered(input.getBooleanOr("cactus_powered", false));
		this.setCactusHeight(input.getFloatOr("cactus_height", 0F));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("cactus_powered", this.isCactusPowered());
		output.putFloat("cactus_height", this.getCactusHeight());
	}

	public float getCactusHeight() {
		return this.entityData.get(CACTUS_HEIGHT);
	}

	public void setCactusHeight(float h) {
		this.entityData.set(CACTUS_HEIGHT, h);
	}

	public boolean isCactusPowered() {
		return this.entityData.get(POWERED);
	}

	public void setCactusPowered(boolean is) {
		this.entityData.set(POWERED, is);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CACTUS.get();
	}
}