package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 坚果墙。纯血盾，无攻击。super 模式提升 {@link #getSuperLife()}。
 */
public class WallNutEntity extends PlantDefenderEntity {

	public WallNutEntity(EntityType<? extends PlantDefenderEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getLife() {
		return this.getSkillValue(SkillTypes.NUT_MORE_LIFE.get());
	}

	@Override
	public float getSuperLife() {
		return 400;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 1.1F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.WALL_NUT.get();
	}
}