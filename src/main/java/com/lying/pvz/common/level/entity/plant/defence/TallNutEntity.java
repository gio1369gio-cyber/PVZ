package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 高坚果。继承 {@link WallNutEntity}：super 生命 800、护甲 15 / 韧性 10、
 * 更高的碰撞箱与吸引范围。
 */
public class TallNutEntity extends WallNutEntity {

	public TallNutEntity(EntityType<? extends WallNutEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getSuperLife() {
		return 800;
	}

	/** 父类签名是 {@code float getArmor()}，子类覆写必须返回 {@code float}。 */
	@Override
	public float getArmor() {
		return 15F;
	}

	@Override
	public float getArmorToughness() {
		return 10F;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 1.9F);
	}

	@Override
	public float getAttractRange() {
		return 3.5F;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TALL_NUT.get();
	}
}