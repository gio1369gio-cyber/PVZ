package com.lying.pvz.common.level.entity.plant.light;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 双子向日葵。继承 {@link SunFlowerEntity}：产出 50 阳光（双份）、super 产量 750。
 */
public class TwinSunFlowerEntity extends SunFlowerEntity {

	public TwinSunFlowerEntity(EntityType<? extends SunFlowerEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void genSomething() {
		this.genSun(this.getSunAmount(), 2);
	}

	@Override
	public int getSunAmount() {
		return 50;
	}

	@Override
	public int getSuperSunAmount() {
		return 750;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 1.85F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TWIN_SUNFLOWER.get();
	}
}