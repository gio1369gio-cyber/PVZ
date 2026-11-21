package com.lying.pvz.common.level.entity.plant.spear;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZGlobalTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

/**
 * 猫尾草。
 *
 * <p>追踪型穿刺弹发射者。{@link #thorns} 缓存所有"仍在 GUIDE 模式"的弹，
 * 每 tick 更新其追踪目标。
 *
 * <p><b>新架构适配</b>：
 * <ul>
 *   <li>{@code setThornTarget(LivingEntity)} → {@code setTargetId(@Nullable UUID)}</li>
 *   <li>{@code isInControl()} → {@code getMode() == ThornProjectile.Mode.GUIDE}</li>
 *   <li>{@code ThornStates.NORMAL} 字段删除（新架构无此枚举）</li>
 * </ul>
 */
public class CatTailEntity extends PlantShooterEntity {

	public HashSet<BulletEntity> thorns = new HashSet<>();
	private int powerCount = 0;
	private int powerTick = 0;
	private final int POWER_CD = 200;

	public CatTailEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}

	@Override
	protected void addTargetGoals() {
		this.targetSelector.addGoal(0,
				new PVZGlobalTargetGoal(this, true, false, this.getShootRange(), this.getShootRange()));
	}

	@Override
	public void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.powerTick > 0) {
			--this.powerTick;
		}
		final HashSet<BulletEntity> tmp = new HashSet<>();
		final LivingEntity target = this.getTarget();
		this.thorns.forEach(thorn -> {
			if (EntityUtil.isEntityValid(thorn)
					&& thorn.variant() == BulletKinds.THORN_GUIDE) {
				thorn.setTarget(target);
				tmp.add(thorn);
			}
		});
		this.thorns.clear();
		this.thorns = tmp;
	}

	@Override
	public void shootBullet() {
		this.performShoot(0, 0, 0.2, true, FORWARD_SHOOT_ANGLE);
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
		final int variant = this.getThornShootType();
		spec.baseDamage(this.getAttackDamage())
				.variant(variant)
				.pierce(0)
				.lifeTicks(variant == BulletKinds.THORN_AUTO ? 500 : 250);
	}

	@Override
	protected void onBulletLaunched(BulletEntity bullet) {
		if (bullet.variant() == BulletKinds.THORN_GUIDE) {
			this.thorns.add(bullet);
		}
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (entity instanceof BalloonZombieEntity) {
			return true;
		}
		return super.canPAZTarget(entity);
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.powerCount += this.getPowerThornCount();
	}

	protected int getThornShootType() {
		if (this.powerCount > 0 && this.powerTick == 0) {
			this.powerTick = POWER_CD;
			--this.powerCount;
			return BulletKinds.THORN_AUTO;
		}
		return BulletKinds.THORN_GUIDE;
	}

	@Override
	protected boolean canAttackNow() {
		return this.getAttackTime() > 0 && this.getAttackTime() % this.getAnimCD() == 0;
	}

	@Override
	public boolean checkY(Entity target) {
		return true;
	}

	public int getPowerThornCount() {
		return 1;
	}

	public int getExtraAttackCount() {
		return 1;
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_THORN_DAMAGE.get());
	}

	public int getAnimCD() {
		return 8;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(this.getAnimCD() * 2);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1F);
	}

	@Override
	public float getShootRange() {
		return 80;
	}

	@Override
	public float getBulletSpeed() {
		return 0F;
	}

	@Override
	public int getSuperTimeLength() {
		return 40;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.powerCount = input.getIntOr("power_thorn_count", 0);
		this.powerTick = input.getIntOr("power_shoot_tick", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("power_thorn_count", this.powerCount);
		output.putInt("power_shoot_tick", this.powerTick);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CAT_TAIL.get();
	}
}