package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

/**
 * 生产型植物基类（阳光、金币等）。{@code attackTime} 作为倒计时驱动 {@link #getGenCD()} 节奏。
 */
public abstract class PlantProducerEntity extends PlantEntity {
	public PlantProducerEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setAttackTime(200);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new ProducerGenGoal(this));
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(Pair.of(PAZAlmanacs.GEN_CD, this.getGenCD())));
	}

	protected void genSun(int num, int cnt) {
		SunEntity.spawnSunsByAmount(this.level(), this.blockPosition(), num, num / cnt, 2);
		EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
	}

	protected abstract void genSomething();

	protected abstract void genSuper();

	public abstract int getGenCD();

	public int getAnimGenCD() {
		return 20;
	}

	@Override
	public int getSuperTimeLength() {
		return 30;
	}

	public boolean isPlantInGen() {
		return this.getAttackTime() <= 10
				|| (this.isPlantInSuperMode() && this.getSuperTime() < 10);
	}

	static class ProducerGenGoal extends Goal {

		private final PlantProducerEntity producer;

		public ProducerGenGoal(PlantProducerEntity entity) {
			this.producer = entity;
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			return true;
		}

		@Override
		public void stop() {
		}

		@Override
		public void tick() {
			if (!this.producer.canNormalUpdate()) {
				return;
			}
			if (this.producer.isPlantInSuperMode()) {
				if (this.producer.getSuperTime() == 1) {
					this.producer.genSuper();
				}
				return;
			}
			final int time = this.producer.getAttackTime();
			if (time <= 1) {
				this.producer.genSomething();
				this.producer.setAttackTime(this.producer.getGenCD());
			} else {
				this.producer.setAttackTime(Math.max(0, time - 1));
			}
		}
	}
}