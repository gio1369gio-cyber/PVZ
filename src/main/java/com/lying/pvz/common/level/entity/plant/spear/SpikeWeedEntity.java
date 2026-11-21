package com.lying.pvz.common.level.entity.plant.spear;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.IHasWheel;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * 地刺。
 *
 * <p>周期性碾压范围内 {@link IHasWheel} 或造成 {@code SPIKE_DAMAGE} 伤害，每次消耗 1 个
 * {@link #getSpikeNum()}，耗尽 {@code discard}。被 {@code PvZEntityDamageSource.isCrushDamage()}
 * 命中时以 1 个尖刺抵消伤害。super 模式补充 {@link #getSuperSpikeCount()} 个尖刺。
 */
public class SpikeWeedEntity extends PlantEntity {

	private static final EntityDataAccessor<Integer> SPIKE_NUM =
			SynchedEntityData.defineId(SpikeWeedEntity.class, EntityDataSerializers.INT);
	public static final int ATTACK_ANIM_CD = 10;

	public SpikeWeedEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setSpikeNum(this.getSpikesCount());
		this.canBeStealByBungee = false;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SPIKE_NUM, 1);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.getSpikeNum() <= 0) {
			this.discard();
			return;
		}
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
		}
		if (this.getExistTick() % this.getAttackCD() == 10) {
			this.spikeAttack();
		}
	}

	public void spikeAttack() {
		final float range = 0.6F;
		EntityUtil.getTargetableEntities(this,
				EntityUtil.getEntityAABB(this, range, range)).forEach(this::spikeNormalAttack);
	}

	public void spikeNormalAttack(@Nonnull Entity target) {
		if (target instanceof IHasWheel wheel) {
			wheel.spikeWheelBy(this);
			this.setSpikeNum(this.getSpikeNum() - 1);
		} else {
			target.hurt(PvZEntityDamageSource.causeThornDamage(this), this.getAttackDamage());
		}
		this.setAttackTime(ATTACK_ANIM_CD);
	}

	/**
	 * 1.21.1 伤害入口覆写点：{@link #hurtServer(ServerLevel, DamageSource, float)}。
	 */
	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		if (source instanceof PvZEntityDamageSource pds && pds.isCrushDamage()) {
			if (this.getSpikeNum() > 0) {
				this.setSpikeNum(this.getSpikeNum() - 1);
				return false;
			}
		}
		return super.hurtServer(level, source, damage);
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setSpikeNum(this.getSpikeNum() + this.getSuperSpikeCount());
	}

	@Override
	public boolean isPlantImmuneTo(DamageSource source) {
		return super.isPlantImmuneTo(source)
				|| (!(source instanceof PvZEntityDamageSource) && !source.is(DamageTypeTags.IS_PROJECTILE));
	}

	@Override
	protected boolean shouldCollideWithEntity(LivingEntity target) {
		if (EntityUtil.canTargetEntity(this, target)) {
			return false;
		}
		return super.shouldCollideWithEntity(target);
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return false;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.SPIKE_COUNT, this.getSpikesCount()),
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.ATTACK_CD, this.getAttackCD())
		));
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SPIKE_DAMAGE.get());
	}

	public int getSuperSpikeCount() {
		return 3;
	}

	public int getAttackCD() {
		return 60;
	}

	public int getSpikesCount() {
		return 1;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setSpikeNum(input.getIntOr("spike_num", 1));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("spike_num", this.getSpikeNum());
	}

	public int getSpikeNum() {
		return this.entityData.get(SPIKE_NUM);
	}

	public void setSpikeNum(int num) {
		this.entityData.set(SPIKE_NUM, num);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.95F, 0.4F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SPIKE_WEED.get();
	}
}