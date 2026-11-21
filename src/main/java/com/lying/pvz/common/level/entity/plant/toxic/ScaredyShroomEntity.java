package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 胆小菇。
 *
 * <p>目标距离 ≤ {@link #getScareDistance()} 时进入 {@link #SCARE_TIME} 恐惧状态：
 * 停止射击、身体缩矮（{@link #getDefaultDimensions} 用 {@code SCARE_TIME} 压缩高度）。
 * 发射生命周期 24 tick 的 {@link com.lying.pvz.common.projectile.BulletKinds#SPORE}。super 模式 2 连发。
 */
public class ScaredyShroomEntity extends PlantShooterEntity {

	private static final EntityDataAccessor<Integer> SCARE_TIME =
			SynchedEntityData.defineId(ScaredyShroomEntity.class, EntityDataSerializers.INT);
	protected static final double SHOOT_OFFSET = 0.2D;
	public static final int SCARE_ANIM_CD = 15;

	public ScaredyShroomEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SCARE_TIME, 0);
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get());
	}

	@Override
	public void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (EntityUtil.isEntityValid(this.getTarget())) {
			final double dis = this.getScareDistance();
			if (this.distanceToSqr(this.getTarget()) <= dis * dis) {
				this.setScareTime(Mth.clamp(this.getScareTime() + 1, 0, SCARE_ANIM_CD));
				return;
			}
		}
		this.setScareTime(Mth.clamp(this.getScareTime() - 1, 0, SCARE_ANIM_CD));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		super.onSyncedDataUpdated(data);
		if (data.equals(SCARE_TIME)) {
			this.refreshDimensions();
		}
	}

	@Override
	public boolean canShoot() {
		return super.canShoot() && !this.isScared();
	}

	@Override
	public void shootBullet() {
		if (this.isPlantInSuperMode()) {
			final int cnt = this.getSuperShootCount();
			for (int i = 0; i < cnt; ++i) {
				final float offset = MathUtil.getRandomFloat(this.getRandom()) / 3;
				final float offsetH = MathUtil.getRandomFloat(this.getRandom()) / 3;
				this.performShoot(SHOOT_OFFSET, offset, offsetH,
						this.getExistTick() % 10 == 0, FORWARD_SHOOT_ANGLE);
			}
		} else {
			this.performShoot(SHOOT_OFFSET, 0, 0,
					this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
		}
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.SPORE;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.SPORE.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.getAttackDamage());
	}

	@Override
	protected SoundEvent getShootSound() {
		return PvZSoundRegister.PUFF.get();
	}

	public int getSuperShootCount() {
		return 2;
	}

	public float getScareDistance() {
		return 5;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.6F,
				1.6F - this.getScareTime() * 1.0F / SCARE_ANIM_CD);
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(1);
	}

	@Override
	public int getSuperTimeLength() {
		return 100;
	}

	@Override
	public float getShootRange() {
		return 35;
	}

	public boolean isScared() {
		return this.getScareTime() > 0;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("scare_time", this.getScareTime());
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setScareTime(input.getIntOr("scare_time", 0));
	}

	public int getScareTime() {
		return this.entityData.get(SCARE_TIME);
	}

	public void setScareTime(int time) {
		this.entityData.set(SCARE_TIME, time);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SCAREDY_SHROOM.get();
	}
}