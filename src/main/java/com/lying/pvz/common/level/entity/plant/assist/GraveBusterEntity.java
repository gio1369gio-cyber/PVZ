package com.lying.pvz.common.level.entity.plant.assist;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.grass.AbstractTombStoneEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlantUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

/**
 * 墓碑吞噬者（Grave Buster）。
 *
 * <p>骑上墓碑后累计 {@link #getAttackTime()}，达到 {@link #getEatTombCD()} 造成一次
 * 最大生命值伤害；累计 {@link #getMaxKillCnt()} 次后 {@code discard}。
 * super 模式下一次性生成 {@link #getSuperAttackCnt()} 个自身骑到附近墓碑上。
 */
public class GraveBusterEntity extends PlantEntity {

	private static final int MAX_LIVE_TICK = 100;
	private int killCount = 0;

	public GraveBusterEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithPlant = false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PVZNearestTargetGoal(this, true, false, 5, 5));
		this.goalSelector.addGoal(0, new EatTombStoneGoal(this));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.isEatingTomb()) {
			if (this.getAttackTime() % 20 == 10) {
				EntityUtil.playSound(this, PvZSoundRegister.CHOMP.get());
			}
			this.setExistTick(0);
		}
		if (this.getExistTick() > MAX_LIVE_TICK) {
			this.discard();
		}
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		final float range = 10;
		int cnt = this.getSuperAttackCnt();
		for (LivingEntity target : EntityUtil.getTargetableLivings(this,
				EntityUtil.getEntityAABB(this, range, range))) {
			if (!(target instanceof AbstractTombStoneEntity)) {
				continue;
			}
			final GraveBusterEntity buster = PvZEntityRegister.GRAVE_BUSTER.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (buster == null) {
				return;
			}
			PlantUtil.copyPlantData(this, buster);
			EntityUtil.onEntitySpawn(this.level(), buster, target.blockPosition());
			buster.startRiding(target);
			buster.setTarget(target);
			if (--cnt == 0) {
				break;
			}
		}
	}

	public boolean isEatingTomb() {
		return this.getAttackTime() > 0 && this.getVehicle() instanceof AbstractTombStoneEntity;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.WORK_TIME, this.getEatTombCD()));
	}

	public int getEatTombCD() {
		return 100;
	}

	public int getMaxKillCnt() {
		return 1;
	}

	public int getSuperAttackCnt() {
		return 2;
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (!(entity instanceof AbstractTombStoneEntity)) {
			return false;
		}
		if (entity.getPassengers().isEmpty()) {
			return true;
		}
		Entity vehicle = this.getVehicle();
		return vehicle != null && entity.is(vehicle);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1F, 1.6F);
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("kill_cnt", this.killCount);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.killCount = input.getIntOr("kill_cnt", 0);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GRAVE_BUSTER.get();
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	static class EatTombStoneGoal extends Goal {

		private final GraveBusterEntity buster;
		private LivingEntity target;

		public EatTombStoneGoal(GraveBusterEntity buster) {
			this.buster = buster;
		}

		@Override
		public boolean canUse() {
			if (this.buster.isEatingTomb()) {
				return true;
			}
			LivingEntity target = this.buster.getTarget();
			if (!EntityUtil.isEntityValid(target) || !this.buster.canPAZTarget(target)) {
				this.buster.setTarget(null);
				this.target = null;
				return false;
			}
			this.target = target;
			this.buster.startRiding(target, true,true);
			return true;
		}

		@Override
		public void stop() {
			this.target = null;
		}

		@Override
		public boolean canContinueToUse() {
			return this.buster.isEatingTomb();
		}

		@Override
		public void tick() {
			if (!this.buster.canNormalUpdate()) {
				return;
			}
			final int tick = this.buster.getAttackTime();
			if (tick >= this.buster.getEatTombCD()) {
				this.buster.setAttackTime(0);
				++this.buster.killCount;
				this.target.hurt(PvZEntityDamageSource.eat(this.buster),
						EntityUtil.getMaxHealthDamage(this.buster.getTarget(), 1.5F));
				if (this.buster.killCount >= this.buster.getMaxKillCnt()) {
					this.buster.discard();
				}
			} else {
				this.buster.setAttackTime(tick + 1);
			}
		}
	}
}