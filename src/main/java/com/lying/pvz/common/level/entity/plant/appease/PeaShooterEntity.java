package com.lying.pvz.common.level.entity.plant.appease;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PeaShooterEntity extends PlantShooterEntity {
	protected static final double SHOOT_OFFSET = 0.2D;


	public PeaShooterEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
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
		return BulletKinds.PEA;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.PEA.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.getAttackDamage());
	}
	@Override
	public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.PEA_DAMAGE.get());
	}

	public int getSuperShootCount() {
		return MathUtil.getRandomMinMax(this.getRandom(), 1, 2);
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
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.7F, 1.3F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PEA_SHOOTER.get();
	}
}