package com.lying.pvz.common.level.entity.plant.light;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantProducerEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

/**
 * 向日葵。
 *
 * <p>产出 25 阳光，{@link #getGenCD()} 按白天/雨天/夜晚折算为 250/500/750 tick。
 * super 一次性产出 {@link #getSuperSunAmount()} 阳光。
 */
public class SunFlowerEntity extends PlantProducerEntity {

	public SunFlowerEntity(EntityType<? extends PlantProducerEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void genSomething() {
		this.genSun(this.getSunAmount(), 1);
	}

	@Override
	protected void genSuper() {
		SunEntity.spawnSunsByAmount(this.level(), this.blockPosition(),
				this.getSuperSunAmount(), 100, 1);EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.GEN_SUN_AMOUNT, this.getSunAmount())
		));
	}

	public int getSunAmount() {
		return 25;
	}

	public int getSuperSunAmount() {
		return 500;
	}

	@Override
	public int getGenCD() {
		final int time = 250;
		return this.level().isBrightOutside()
				? (this.level().isRaining() ? 2 * time : time)
				: 3 * time;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.3F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SUN_FLOWER.get();
	}
}