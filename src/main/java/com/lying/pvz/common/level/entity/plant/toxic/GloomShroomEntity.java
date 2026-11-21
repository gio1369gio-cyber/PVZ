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
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 忧郁菇。
 *
 * <p>8 方向环形喷发短程 {@link com.lying.pvz.common.projectile.BulletKinds#FUME}（生命周期 3 tick）；super 模式附加击退。
 * 攻速 8 tick，射程仅 4 格。
 */
public class GloomShroomEntity extends PlantShooterEntity {

	private static final float SHOOT_HEIGHT = 0.2F;
	private static final int SHOOT_NUM = 8;

	public GloomShroomEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void shootBullet() {
		float now = this.getYHeadRot();
		for (int i = 0; i < SHOOT_NUM; ++i) {
			this.shootByAngle(now, SHOOT_HEIGHT);
			now += 360F / SHOOT_NUM;
		}
		EntityUtil.playSound(this, PvZSoundRegister.FUME.get());
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.FUME;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.FUME.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.getAttackDamage()).lifeTicks(3);
		if (this.isPlantInSuperMode()) {
			spec.knockback(1);
		}
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(1);
	}

	@Override
	public int getSuperTimeLength() {
		return 80;
	}

	@Override
	public int getShootCD() {
		return 8;
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get());
	}

	@Override
	public float getShootRange() {
		return 4;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 0.8F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GLOOM_SHROOM.get();
	}
}