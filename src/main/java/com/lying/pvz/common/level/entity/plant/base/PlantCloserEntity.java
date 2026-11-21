package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class PlantCloserEntity extends PlantEntity {

	protected float closeWidth = 1.5F;
	protected float closeHeight = 1.5F;

	public PlantCloserEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(
				this, true, false, this.getCloseWidth(), this.getCloseHeight()));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide() && this.canCheckDistance()) {
			if (EntityUtil.isEntityValid(this.getTarget())) {
				this.focusOnTarget(this.getTarget());
				final int time = this.getAttackTime();
				if (time >= this.getAttackCD()) {
					this.performAttack(this.getTarget());
				}
				this.setAttackTime(time + 1);
			} else {
				this.setAttackTime(0);
			}
		}
	}

	public void focusOnTarget(@Nonnull LivingEntity target) {
		this.getLookControl().setLookAt(target, 30F, 30F);
	}

	public boolean canCheckDistance() {
		return true;
	}

	public abstract void performAttack(LivingEntity target);

	@Override
	public boolean isPlantImmuneTo(DamageSource source) {
		if (this.canBeImmuneToEnforce(source.getEntity())) {
			return super.isPlantImmuneTo(source) || PvZEntityDamageSource.isEnforceDamage(source);
		}
		return super.isPlantImmuneTo(source);
	}

	protected boolean canBeImmuneToEnforce(Entity entity) {
		return this.checkCanPAZTarget(entity);
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	public int getAttackCD() {
		return 10;
	}

	public float getCloseWidth() {
		return 1.5F;
	}

	public float getCloseHeight() {
		return 1.5F;
	}
}
