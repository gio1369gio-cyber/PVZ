package com.lying.pvz.common.level.entity.plant.magic;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.impl.plant.MemePlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlantUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Arrays;
import java.util.List;

/**
 * 怪猫。
 *
 * <p>{@link #REST_CD} = 1000 tick 休息，攻击间隔 {@link #ANIM_CD} = 10；
 * 每次攻击伤害 {@link #getAttackDamage()} = 200。
 * super 模式下对 20 格范围随机 {@link #getSuperAttackCount()} 个目标造成一次伤害。
 *
 * <p>被击杀时若击杀者为 {@link StrangeCatEntity} 且附近同类数量未达
 * 则在尸体位置复制一只新怪猫。
 */
public class StrangeCatEntity extends PlantEntity {

	public static final int REST_CD = 1000;
	public static final int ANIM_CD = 10;
	private int restTick = REST_CD;

	public StrangeCatEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
		this.isImmuneToWeak = true;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, false, 5, 5));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (EntityUtil.isEntityValid(this.getTarget())) {
			this.getLookControl().setLookAt(this.getTarget(), 30F, 30F);
		}
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
		} else if (this.restTick == 0) {
			this.setAttackTime(0);
			if (EntityUtil.isEntityValid(this.getTarget())) {
				this.performAttack(this.getTarget());
			}
		} else {
			this.setAttackTime(-1);
		}
		this.restTick = Math.max(0, this.restTick - 1);
	}

	protected void performAttack(LivingEntity target) {
		target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage());
		EntityUtil.playSound(this, PvZSoundRegister.BRUH.get());
		this.setAttackTime(ANIM_CD);
		this.restTick = REST_CD;
	}

	public static void handleCopyCat(final LivingDeathEvent ev) {
		if (ev.getEntity().level().isClientSide()) {
			return;
		}
		if (!(ev.getSource().getEntity() instanceof StrangeCatEntity cat)) {
			return;
		}
		final float range = 10F;
		final int count = ev.getEntity().level().getEntitiesOfClass(StrangeCatEntity.class,
				EntityUtil.getEntityAABB(ev.getEntity(), range, range)).size();
		if (count < PvZConfig.LOCAL_CONFIG.EntitySettings.PlantSetting.StrangeCatCount.get()) {
			cat.onSelfCopy(ev.getEntity());
		}
	}

	public void onSelfCopy(LivingEntity target) {
		final StrangeCatEntity cat = PvZEntityRegister.STRANGE_CAT.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		if (cat == null) {
			return;
		}
		PlantUtil.copyPlantData(this, cat);
		EntityUtil.onEntitySpawn(this.level(), cat, target.blockPosition());
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		EntityUtil.playSound(this, PvZSoundRegister.BRUH.get());
		this.setAttackTime(ANIM_CD);
		EntityUtil.getRandomLivingInRange(this.level(), this,
						EntityUtil.getEntityAABB(this, 20, 20), this.getSuperAttackCount())
				.forEach(target ->
						target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage()));
	}

	public boolean isResting() {
		return this.getAttackTime() < 0;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.REST_TIME, REST_CD)
		));
	}

	public float getAttackDamage() {
		return 200;
	}

	public int getSuperAttackCount() {
		return 4;
	}

	@Override
	public int getSuperTimeLength() {
		return 30;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.8F, 1F);
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("rest_tick", this.restTick);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.restTick = input.getIntOr("rest_tick", REST_CD);
	}

	@Override
	public IPlantType getPlantType() {
		return MemePlants.STRANGE_CAT.get();
	}
}