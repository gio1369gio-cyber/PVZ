package com.lying.pvz.common.level.entity.plant.enforce;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * 窝瓜。
 *
 * <p>锁定目标后跳向目标位置，落地造成 1 格 AOE 碾压伤害。
 * 命中后按 {@link #getAgainChance()} 概率重生（{@code extraChance} 计数在 super 时增加）。
 * 免疫 {@code ENFORCE} 类伤害，跳跃期间禁用植物碰撞并免疫枯萎。
 */
public class SquashEntity extends PlantEntity {

	private static final int TARGET_RANGE = 3;
	protected int extraChance = 0;

	public SquashEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZRandomTargetGoal(
				this, true, false, TARGET_RANGE, TARGET_RANGE));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (EntityUtil.isEntityValid(this.getTarget())) {
			this.getLookControl().setLookAt(this.getTarget(), 30F, 30F);
		}
		if (this.getAttackTime() > 0) {
			if (this.onGround() || this.isInWater()) {
				this.dealDamage();
				if (this.extraChance > 0) {
					--this.extraChance;
				} else if (this.getRandom().nextFloat() > this.getAgainChance()) {
					this.discard();
				}
			}
		} else if (this.getTarget() != null) {
			EntityUtil.playSound(this, PvZSoundRegister.SQUASH_HMM.get());
			this.jumpToTarget(this.getTarget());
		}
	}

	@Override
	protected boolean shouldCollideWithEntity(LivingEntity target) {
		if (EntityUtil.canTargetEntity(this, target)) {
			return false;
		}
		return super.shouldCollideWithEntity(target);
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		if (!this.level().isClientSide()) {
			this.extraChance += this.getSuperBonusChance();
		}
	}

	protected void dealDamage() {
		this.setAttackTime(0);
		this.canCollideWithPlant = true;
		this.isImmuneToWeak = false;
		EntityUtil.playSound(this, PvZSoundRegister.GROUND_SHAKE.get());
		final float range = 1F;
		for (Entity entity : EntityUtil.getWholeTargetableEntities(this,
				EntityUtil.getEntityAABB(this, range, range))) {
			entity.hurt(PvZEntityDamageSource.causeCrushDamage(this), this.getAttackDamage());
		}
	}

	private void jumpToTarget(LivingEntity target) {
		final int tick = 10;
		this.canCollideWithPlant = false;
		this.isImmuneToWeak = true;
		final Vec3 pos = target.position()
				.add(target.getDeltaMovement().scale(tick * 0.8D));
		this.setPos(pos.x(), pos.y() + target.getBbHeight() + 1, pos.z());
		this.setDeltaMovement(this.getDeltaMovement().x(), 0, this.getDeltaMovement().z());
		this.setAttackTime(1);
	}

	@Override
	protected boolean shouldLockXZ() {
		return this.onGround();
	}

	protected int getSuperBonusChance() {
		return 3;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.AGAIN_CHANCE, this.getAgainChance())
		));
	}

	public float getAgainChance() {
		return this.getSkillValue(SkillTypes.SQUASH_AGAIN.get());
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_ENHANCE_STRENGTH.get());
	}

	@Override
	public boolean isPlantImmuneTo(DamageSource source) {
		return super.isPlantImmuneTo(source) || PvZEntityDamageSource.isEnforceDamage(source);
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 1.5F);
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.extraChance = input.getIntOr("extra_chance", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("extra_chance", this.extraChance);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SQUASH.get();
	}
}