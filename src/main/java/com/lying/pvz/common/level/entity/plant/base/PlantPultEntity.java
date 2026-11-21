package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.projectile.IPult;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PultAttackGoal;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.projectile.AbstractPAZProjectile;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 投掷型植物基类（卷心菜、西瓜、玉米等）。攻击动画由 {@code attackTime} 驱动，
 * 在动画中点放出弹药；超级模式触发一次范围轰炸。
 */
public abstract class PlantPultEntity extends PlantEntity implements IPult {
	protected boolean isSuperOut = false;

	public PlantPultEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PultAttackGoal(this));
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(
				this, true, false, this.getPultRange(), this.getPultHeight()));
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		final AttributeInstance followRange = this.getAttribute(Attributes.FOLLOW_RANGE);
		if (followRange != null) {
			followRange.setBaseValue(this.getPultRange());
		}
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide() && this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
			if (this.getAttackTime() == this.getPultAnimTime() / 2) {
				if (this.isPlantInSuperMode() && !this.isSuperOut) {
					this.isSuperOut = true;
					this.superAttack();
				} else {
					this.pultBullet();
				}
			}
		}
	}

	public void performPult(LivingEntity target) {
		Optional.ofNullable(target).ifPresent(t -> {
			final BulletKind kind = this.getBulletKind();
			if (kind != null && this.level() instanceof ServerLevel serverLevel) {
				final ShotSpec spec = new ShotSpec();
				this.configureShot(spec);
				BulletLauncher.launch(serverLevel, this.getBulletType(), this,
						this.getEntityGroupType(), kind, spec,
						new Vec3(this.getX(), this.getY() + 1.7F, this.getZ()), t, null);
				EntityUtil.playSound(this, PvZSoundRegister.PULT_THROW.get());
				return;
			}

			final AbstractPAZProjectile bullet = this.createProjectile();
			bullet.setPos(this.getX(), this.getY() + 1.7F, this.getZ());
			bullet.shootFrom(this);
			bullet.aimAt(t);
			bullet.setAttackDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage());
			this.level().addFreshEntity(bullet);
			EntityUtil.playSound(this, PvZSoundRegister.PULT_THROW.get());
		});
	}

	protected void superAttack() {
		final float range = this.getSuperRange();
		EntityUtil.getTargetableLivings(this, EntityUtil.getEntityAABB(this, range, range))
				.forEach(this::doSuperAttackToTarget);
	}

	@Override
	public void pultBullet() {
		this.performPult(this.getTarget());
	}

	protected void doSuperAttackToTarget(LivingEntity target) {
		this.performPult(target);
	}

	/* ================================================================== */
	/* ICanShoot（新架构引入）                                              */
	/* ================================================================== */

	@Override
	public AbstractPAZProjectile createProjectile() {
		final AbstractPAZProjectile bullet = this.createBullet();
		if (bullet == null) {
			throw new IllegalStateException(
					"Plant " + this.getPlantType().getID() + " uses BulletKind, not AbstractPAZProjectile");
		}
		return bullet;
	}

	/** 旧弹药工厂；已迁移到 {@link BulletKind} 的植物保持返回 {@code null}。 */
	protected @Nullable AbstractPAZProjectile createBullet() {
		return null;
	}

	/** 新弹药 Kind；返回非 null 时 {@link #performPult} 走新发射路径。 */
	protected @Nullable BulletKind getBulletKind() {
		return null;
	}

	/** 新弹药 EntityType；{@link #getBulletKind()} 非 null 时必须返回非 null。 */
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return null;
	}

	/** 每发配置：伤害覆盖、变体、效果等。 */
	protected void configureShot(ShotSpec spec) {
	}

	@Override
	public float getAttackRange() {
		return this.getPultRange();
	}

	@Override
	public int getAttackInterval() {
		return this.getPultCD();
	}

	public abstract float getSuperDamage();

	@Override
	public boolean shouldPult() {
		return this.canNormalUpdate();
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		return this.checkY(entity) && super.canPAZTarget(entity);
	}

	protected boolean checkY(Entity target) {
		return this.getY() + this.getPultHeight() >= target.getY() + target.getBbHeight();
	}

	@Override
	public void startPultAttack() {
		this.setAttackTime(this.getPultAnimTime());
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.isSuperOut = false;
		this.startPultAttack();
	}

	public int getPultAnimTime() {
		return 20;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.BULLET_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.ATTACK_CD, this.getPultCD()),
				Pair.of(PAZAlmanacs.ATTACK_RANGE, this.getPultRange())
		));
	}

	public abstract float getAttackDamage();

	@Override
	public int getPultCD() {
		return 60;
	}

	@Override
	public int getSuperTimeLength() {
		return 60;
	}

	public float getSuperRange() {
		return 15;
	}

	@Override
	public float getPultRange() {
		return 30;
	}

	public float getPultHeight() {
		return 15;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.isSuperOut = input.getBooleanOr("is_plant_super_out", false);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("is_plant_super_out", this.isSuperOut);
	}
}