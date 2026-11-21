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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 大喷菇。
 *
 * <p>直线发射穿透型 {@link com.lying.pvz.common.projectile.BulletKinds#FUME}（生命周期 10 tick，可穿灌木）。
 * super 模式下每 5 tick 连射一次。
 */
public class FumeShroomEntity extends PlantShooterEntity {

	protected static final double SHOOT_OFFSET = 0.2D;

	public FumeShroomEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void shootBullet() {
		if (this.isPlantInSuperMode()) {
			this.performShoot(SHOOT_OFFSET, 0, 0,
					this.getExistTick() % 5 == 0, FORWARD_SHOOT_ANGLE);
		} else {
			this.performShoot(SHOOT_OFFSET, 0, 0,
					this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
		}
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
		spec.baseDamage(this.getAttackDamage()).lifeTicks(10);
	}

	@Override
	protected SoundEvent getShootSound() {
		return PvZSoundRegister.FUME.get();
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(1);
	}

	@Override
	public int getSuperTimeLength() {
		return 60;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.25F);
	}

	@Override
	public float getShootRange() {
		return 15;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.FUME_SHROOM.get();
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get());
	}
}