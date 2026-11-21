package com.lying.pvz.common.level.entity.plant.appease;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.OtherPlants;
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
 * 天使星果。
 *
 * <p>与星星果差异：super 模式下每 tick 朝 5 个方向补射；普通攻击有概率触发第二组 5 发；
 * 子弹类型为 {@code BIG}（super）与 {@code NORMAL}（普通），状态固定 PINK。
 */
public class AngelStarFruitEntity extends PlantShooterEntity {


	public static final float PER_ANGLE = 360F / 5;
	private static final float SHOOT_HEIGHT = 0.2F;
	public int lightTick = 0;

	public AngelStarFruitEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
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
		if (!this.level().isClientSide() && this.isPlantInSuperMode()) {
			float now = this.getSuperTime() * 4;
			for (int i = 0; i < 5; ++i) {
				this.shootByAngle(now, SHOOT_HEIGHT);
				now += PER_ANGLE;
			}
		}
	}

	@Override
	public void shootBullet() {
		float now = this.yHeadRot;
		for (int i = 0; i < 5; ++i) {
			this.shootByAngle(now, SHOOT_HEIGHT);
			now += PER_ANGLE;
		}
		if (this.getRandom().nextInt(100) < this.getExtraAttackChance()) {
			now = this.yHeadRot + 36F;
			for (int i = 0; i < 5; ++i) {
				this.shootByAngle(now, SHOOT_HEIGHT);
				now += PER_ANGLE;
			}
		}
		EntityUtil.playSound(this, PvZSoundRegister.SNOW_SHOOT.get());
	}

	public float getExtraAttackChance() {
		return this.getSkillValue(SkillTypes.TEN_STARS.get());
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_STAR_DAMAGE.get());
	}

	@Override
	protected boolean canAttackNow() {
		return this.getAttackTime() == 2 && !this.isPlantInSuperMode();
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
						? BulletKinds.STAR_BIG_PINK
						: BulletKinds.STAR_NORMAL_PINK)
				.damage(superMode ? 5.0F : 0.0F, 1.0F);
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
		this.setAttackTime(2);
	}

	@Override
	public int getSuperTimeLength() {
		return 150;
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.ANGEL_STAR_FRUIT.get();
	}

}