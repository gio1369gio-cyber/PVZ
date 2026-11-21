package com.lying.pvz.common.level.entity.plant.enforce;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.misc.SmallChomperEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;
import java.util.List;

/**
 * 大嘴花。
 *
 * <p>命中击杀（当前生命 ≤ 伤害）后进入 {@link #getRestCD()} 长休息；
 * 非击杀只造成 {@code 伤害 / 50} 的少量伤害。
 * super 模式下召唤 {@link #getSuperAttackCnt()} 个小食人花（{@link SmallChomperEntity}）。
 */
public class ChomperEntity extends PlantEntity {

	private static final EntityDataAccessor<Integer> REST_TICK =
			SynchedEntityData.defineId(ChomperEntity.class, EntityDataSerializers.INT);
	public static final int ATTACK_ANIM_CD = 30;

	public ChomperEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(REST_TICK, 0);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, false, 3, 2));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide() && this.getTarget() != null) {
			this.getLookControl().setLookAt(this.getTarget(), 30F, 30F);
		}
		if (this.level().isClientSide()) {
			return;
		}
		if (this.getAttackTime() < ATTACK_ANIM_CD / 2) {
			if (this.getRestTick() > 0) {
				this.setAttackTime(0);
				this.setRestTick(this.getRestTick() - 1);
				return;
			}
			if (!EntityUtil.isEntityValid(this.getTarget())) {
				this.setAttackTime(0);
				return;
			}
			this.setAttackTime(this.getAttackTime() + 1);
			if (this.getAttackTime() == ATTACK_ANIM_CD / 2) {
				this.performAttack();
			}
		} else {
			this.setAttackTime(this.getAttackTime() + 1);
			if (this.getAttackTime() > ATTACK_ANIM_CD) {
				this.setAttackTime(0);
			}
		}
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setRestTick(0);
		final float range = 30F;
		int cnt = this.getSuperAttackCnt();
		for (LivingEntity target : EntityUtil.getTargetableLivings(this,
				EntityUtil.getEntityAABB(this, range, range))) {
			if (!target.onGround()) {
				continue;
			}
			final SmallChomperEntity chomper = PvZEntityRegister.SMALL_CHOMPER.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (chomper == null) {
				return;
			}
			chomper.summonByOwner(this);
			EntityUtil.onEntitySpawn(this.level(), chomper, target.blockPosition());
			if (--cnt == 0) {
				break;
			}
		}
	}

	protected void performAttack() {
		final LivingEntity target = this.getTarget();
		if (target == null) {
			return;
		}
		final float damage = this.getAttackDamage();
		if (EntityUtil.getCurrentHealth(target) <= damage) {
			target.hurt(PvZEntityDamageSource.eat(this), damage);
			this.setRestTick(this.getRestCD());
		} else {
			target.hurt(PvZEntityDamageSource.eat(this), damage / 50F);
		}
		EntityUtil.playSound(this, PvZSoundRegister.BIG_CHOMP.get());
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.REST_TIME, this.getRestCD())
		));
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_ENHANCE_STRENGTH.get());
	}

	public int getRestCD() {
		return 800;
	}

	public int getSuperAttackCnt() {
		return 3;
	}

	public boolean isResting() {
		return this.getRestTick() > 0;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.9F, 1.9F);
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("rest_tick", this.getRestTick());
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setRestTick(input.getIntOr("rest_tick", 0));
	}

	public int getRestTick() {
		return this.entityData.get(REST_TICK);
	}

	public void setRestTick(int tick) {
		this.entityData.set(REST_TICK, tick);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CHOMPER.get();
	}
}