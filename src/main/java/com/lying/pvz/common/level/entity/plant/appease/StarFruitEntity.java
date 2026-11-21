package com.lying.pvz.common.level.entity.plant.appease;


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
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 星星果。
 *
 * <p>5 方向散射，每发间隔 {@link #PER_ANGLE}；super 模式使用 HUGE 星弹。
 * 攻击前随机朝向，避免每次固定方向。
 */
public class StarFruitEntity extends PlantShooterEntity {

	public static final float PER_ANGLE = 360F / 5;
	private static final float SHOOT_HEIGHT = 0.2F;
	public int lightTick = 0;

	public StarFruitEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void normalPlantTick() {
		if (this.level().isClientSide()) {
			if (this.lightTick > 0) {
				--this.lightTick;
			}
			if (this.getAttackTime() > 0) {
				this.lightTick = 8;
			}
		}
		super.normalPlantTick();
	}

	@Override
	protected boolean canAttackNow() {
		return this.getAttackTime() == 2;
	}

	@Override
	public int getSuperTimeLength() {
		return 100;
	}

	@Override
	public void shootBullet() {
		float now = this.getYRot() + 180F;
		for (int i = 0; i < 5; ++i) {
			this.shootByAngle(now, SHOOT_HEIGHT);
			now += PER_ANGLE;
		}
		EntityUtil.playSound(this, PvZSoundRegister.SNOW_SHOOT.get());
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.STAR;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.STAR.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		final boolean superMode = this.isPlantInSuperMode();
		spec.baseDamage(this.getAttackDamage())
				.variant(superMode
						? BulletKinds.STAR_HUGE_YELLOW
						: BulletKinds.STAR_NORMAL_YELLOW)
				.damage(superMode ? 10.0F : 0.0F, 1.0F);
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_STAR_DAMAGE.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 0.5F);
	}

	@Override
	public double getMaxShootAngle() {
		return 80;
	}

	@Override
	public void startShootAttack() {
		this.updateFacing();
		this.setAttackTime(2);
	}

	private void updateFacing() {
		float now = this.getRandom().nextFloat() * 3.14159F * 2F;
		double dx = Math.sin(now);
		double dz = Math.cos(now);
		double dy = 0;
		this.lookAt(EntityAnchorArgument.Anchor.FEET, this.position().add(dx, dy, dz));
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.STAR_FRUIT.get();
	}
}