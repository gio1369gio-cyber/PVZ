package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.common.level.entity.zombie.PVZZombieToolBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;

public class DuckyTubeEntity extends PVZZombieToolBase {

	public DuckyTubeEntity(EntityType<? extends PVZZombieToolBase> type, Level worldIn) {
		super(type, worldIn);
	}


	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (this.isInWater() && this.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
				Vec3 motion = this.getDeltaMovement();
				this.setDeltaMovement(motion.x, 0.05F, motion.z);
			}
		}
	}

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.fixed(0.2f, 0.2f);
	}

	@Override
	public Vec3 getPassengerRidingPosition(Entity entity) {
		return super.getPassengerRidingPosition(entity).add(0, -0.7, 0);
	}


	@Override
	protected float getWaterSlowDown() {
		return 0.93f;
	}

	@Deprecated
	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public boolean dismountsUnderwater() {
		return false;
	}


}