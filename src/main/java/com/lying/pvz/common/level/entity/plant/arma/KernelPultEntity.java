package com.lying.pvz.common.level.entity.plant.arma;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantPultEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 玉米投手。
 *
 * <p>旧弹药 {@code KernelEntity} / {@code ButterEntity} 在新架构**不存在**，
 * 需要先创建 {@code KernelProjectile} / {@code ButterProjectile}
 * （均基于 {@code CabbageProjectile} 扩展）。
 *
 * <p>当前弹类型由 {@link #CURRENT_BULLET} 同步；每次 {@link #startPultAttack()} 通过
 * {@link #changeBullet()} 按概率或 super 状态切换。
 */
public class KernelPultEntity extends PlantPultEntity {

	private static final EntityDataAccessor<Integer> CURRENT_BULLET =
			SynchedEntityData.defineId(KernelPultEntity.class, EntityDataSerializers.INT);
	private static final int BUTTER_CHANCE = 10;
	private KernelPultEntity upgradeEntity;

	public KernelPultEntity(EntityType<? extends PlantPultEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CURRENT_BULLET, CornTypes.KERNEL.ordinal());
	}

	@Override
	public void onPlantUpgrade(PlantEntity plantEntity) {
		super.onPlantUpgrade(plantEntity);
		if (this.upgradeEntity != null) {
			this.upgradeEntity.discard();
		}
	}

	@Override
	public boolean canBeUpgrade(Player player) {
		this.upgradeEntity = this.getNearByPult(player);
		return super.canBeUpgrade(player) && EntityUtil.isEntityValid(this.upgradeEntity);
	}

	private KernelPultEntity getNearByPult(Player player) {
		final float range = 1.5F;
		final List<KernelPultEntity> list = this.level().getEntitiesOfClass(
				KernelPultEntity.class,
				EntityUtil.getEntityAABB(this, range, range),
				pult -> !pult.is(this)
						&& pult.getPlantType() == PVZPlants.KERNEL_PULT.get()
						&& !EntityUtil.canAttackEntity(pult, player));
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void startPultAttack() {
		super.startPultAttack();
		this.changeBullet();
	}

	protected void changeBullet() {
		if (this.isPlantInSuperMode() && !this.isSuperOut) {
			this.setCurrentBullet(CornTypes.BUTTER);
			return;
		}
		this.setCurrentBullet(this.getRandom().nextInt(BUTTER_CHANCE) == 0
				? CornTypes.BUTTER : CornTypes.KERNEL);
	}

	@Override
	public void performPult(LivingEntity target1) {
		super.performPult(target1);
		this.setCurrentBullet(CornTypes.KERNEL);
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return (this.isPlantInSuperMode() || this.getCurrentBullet() == CornTypes.BUTTER)
				? BulletKinds.BUTTER
				: BulletKinds.KERNEL;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return this.getBulletKind() == BulletKinds.BUTTER
				? PvZEntityRegister.BUTTER.get()
				: PvZEntityRegister.KERNEL.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage());
		if (this.getBulletKind() == BulletKinds.BUTTER) {
			spec.hitEffect(this.getButterEffect());
		}
	}

	@Override
	public float getSuperDamage() {
		return 2 * this.getAttackDamage();
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_KERNEL_DAMAGE.get());
	}

	public MobEffectInstance getButterEffect() {
		return new MobEffectInstance(PvZEffectRegister.BUTTER_EFFECT,
				this.getButterDuration(), 1, false, false);
	}

	public int getButterDuration() {
		return 100;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1F);
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		final CornTypes[] values = CornTypes.values();
		final int ord = input.getIntOr("current_bullet_type", CornTypes.KERNEL.ordinal());
		this.setCurrentBullet((ord >= 0 && ord < values.length) ? values[ord] : CornTypes.KERNEL);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("current_bullet_type", this.getCurrentBullet().ordinal());
	}

	public void setCurrentBullet(CornTypes type) {
		this.entityData.set(CURRENT_BULLET, type.ordinal());
	}

	public CornTypes getCurrentBullet() {
		final CornTypes[] values = CornTypes.values();
		final int ord = this.entityData.get(CURRENT_BULLET);
		return (ord >= 0 && ord < values.length) ? values[ord] : CornTypes.KERNEL;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.KERNEL_PULT.get();
	}

	public enum CornTypes {
		KERNEL,
		BUTTER,
		ROCKET
	}
}