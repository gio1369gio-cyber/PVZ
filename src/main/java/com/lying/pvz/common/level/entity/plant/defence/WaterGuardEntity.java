package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.CustomPlants;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;

/**
 * 水兵（自定义植物）。
 *
 * <p>水生防御植物，额外注册 {@link FloatGoal}。
 * 生命 200，super 生命 300、时长 20 tick。
 */
public class WaterGuardEntity extends PlantDefenderEntity {

	public WaterGuardEntity(EntityType<? extends PlantDefenderEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}

	@Override
	public float getLife() {
		return 200;
	}

	@Override
	public float getSuperLife() {
		return 300;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 0.8F);
	}

	@Override
	public IPlantType getPlantType() {
		return CustomPlants.WATER_GUARD.get();
	}
}