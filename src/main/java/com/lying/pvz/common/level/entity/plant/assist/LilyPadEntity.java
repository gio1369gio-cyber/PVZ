package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 睡莲。纯容器，无 super、无攻击。
 */
public class LilyPadEntity extends PlantEntity {

	public LilyPadEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.LILY_PAD.get();
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}
}