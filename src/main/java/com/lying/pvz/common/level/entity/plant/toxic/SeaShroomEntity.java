package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;

/**
 * 海蘑菇。
 *
 * <p>继承 {@link PuffShroomEntity}：额外注册 {@link FloatGoal} 可浮水；
 * 射角更窄（{@link #getMaxShootAngle()} = 5）、碰撞箱更矮。
 */
public class SeaShroomEntity extends PuffShroomEntity {

	public SeaShroomEntity(EntityType<? extends PuffShroomEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}

	@Override
	public double getMaxShootAngle() {
		return 5;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.5F, 0.8F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SEA_SHROOM.get();
	}
}