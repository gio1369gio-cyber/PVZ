package com.lying.pvz.common.level.entity.plant.light;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
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
 * 阳光菇。
 *
 * <p>三阶段产出（15 / 25 / 35 阳光），{@link #GROW_CD} = 24000 tick（= 1 个游戏日）。
 * super 模式下强制升级到第 3 阶段并一次性产出 {@link #getSuperSunAmount()} 阳光。
 */
public class SunShroomEntity extends PlantProducerEntity {

	public static final int GROW_CD = 24000;
	public static final int GROW_ANIM_CD = 10;

	public SunShroomEntity(EntityType<? extends PlantProducerEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.SMALL_GEN_SUN_AMOUNT, this.getSunAmountInStage(1)),
				Pair.of(PAZAlmanacs.GEN_SUN_AMOUNT, this.getSunAmountInStage(2))
		));
	}

	@Override
	protected void genSomething() {
		this.genSun(this.getCurrentSunAmount(), 1);
	}

	@Override
	protected void genSuper() {
		SunEntity.spawnSunsByAmount(this.level(), this.blockPosition(),
				this.getSuperSunAmount(), 100, 3);
		EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		if (!this.isInGrowStage(3)) {
			this.growUpTo(3);
		}
	}

	protected void growUpTo(int stage) {
		this.setExistTick(GROW_CD * (stage - 1) - GROW_ANIM_CD - 2);
		EntityUtil.playSound(this, PvZSoundRegister.PLANT_GROW.get());
	}

	public boolean isInGrowStage(int stage) {
		return this.getExistTick() > GROW_CD * (stage - 1);
	}

	protected int getCurrentSunAmount() {
		return this.isInGrowStage(3) ? this.getSunAmountInStage(3)
				: this.isInGrowStage(2) ? this.getSunAmountInStage(2)
				: this.getSunAmountInStage(1);
	}

	public int getSunAmountInStage(int stage) {
		return stage == 3 ? 35 : stage == 2 ? 25 : 15;
	}

	public int getSuperSunAmount() {
		return 500;
	}

	@Override
	public int getGenCD() {
		return 600;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return this.isInGrowStage(3) ? EntityDimensions.scalable(0.8F, 1.2F)
				: this.isInGrowStage(2) ? EntityDimensions.scalable(0.6F, 1F)
				: EntityDimensions.scalable(0.4F, 0.4F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SUN_SHROOM.get();
	}
}