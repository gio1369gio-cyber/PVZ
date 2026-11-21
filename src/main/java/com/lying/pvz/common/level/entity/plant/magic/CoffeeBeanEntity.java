package com.lying.pvz.common.level.entity.plant.magic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

/**
 * 咖啡豆。炸弹类植物，倒计时结束后唤醒范围内的睡眠植物（主要是蘑菇）。
 *
 * <p>唤醒方式是把目标 {@code sleepTime} 设成大幅负数，让 {@code PlantEntity#tickSleep()}
 * 逐 tick 回正，同时清掉 {@code PAZState.SLEEPING}。同阵营判定走
 * {@code EntityUtil.canTargetEntity} 的反向（不可攻击 = 友方）。
 */
public class CoffeeBeanEntity extends PlantBomberEntity {

	public CoffeeBeanEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithPlant = false;
		this.setImmuneToWeak(true);
		this.hasBombAlamancs = false;
	}

	@Override
	protected void startBomb(boolean server) {
		if (this.level().isClientSide()) {
			return;
		}
		final float len = this.getWorkRange();
		boolean hasEffect = false;
		int awakeCnt = 0;
		for (PlantEntity plant : this.level().getEntitiesOfClass(PlantEntity.class,
				EntityUtil.getEntityAABB(this, len, len))) {
			if (EntityUtil.canTargetEntity(this, plant)) {
				continue;
			}
			if (plant.isPlantSleeping()) {
				++awakeCnt;
			}
			plant.sleepTime = -this.getAwakeTime();
			hasEffect = true;
		}
		final Player player = EntityUtil.getEntityOwner(this.level(), this);
		if (player instanceof ServerPlayer sp) {
			PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(sp, this, awakeCnt);
		}
		if (hasEffect) {
			EntityUtil.playSound(this, PvZSoundRegister.WAKE_UP.get());
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.WORK_RANGE, this.getWorkRange()),
				Pair.of(PAZAlmanacs.AWAKE_TIME, this.getAwakeTime())
		));
	}

	public int getAwakeTime() {
		return 48000;
	}

	public float getWorkRange() {
		return 2.5F;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.6F, 0.8F);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public int getReadyTime() {
		return 30;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.COFFEE_BEAN.get();
	}
}