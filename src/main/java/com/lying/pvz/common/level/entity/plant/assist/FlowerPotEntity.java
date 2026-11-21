package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 花盆。纯容器，无 super、无攻击。
 */
public class FlowerPotEntity extends PlantEntity {

	public FlowerPotEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.FLOWER_POT.get();
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}
}