package com.lying.pvz.common.level.entity.plant.enforce;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantCloserEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlantUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 缠绕海草。
 *
 * <p>锁定目标后强制拉上自身（{@code startRiding}），下沉并攻击一次后 {@code discard}。
 * super 模式一次性生成 {@link #getSuperCount()} 个自身，各自拉一个目标。
 */
public class TangleKelpEntity extends PlantCloserEntity {

	public TangleKelpEntity(EntityType<? extends PlantCloserEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void focusOnTarget(LivingEntity target) {
		super.focusOnTarget(target);
		if (target.getVehicle() == null) {
			EntityUtil.playSound(this, PvZSoundRegister.DRAG.get());
			target.startRiding(this, true,true);
		}
		this.setDeltaMovement(0, -0.4F, 0);
	}

	@Override
	public void performAttack(LivingEntity target) {
		target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage());
		this.discard();
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		if (this.level().isClientSide()) {
			return;
		}
		int cnt = this.getSuperCount();
		for (LivingEntity target : EntityUtil.getTargetableLivings(this,
				EntityUtil.getEntityAABB(this, 25, 3))) {
			final TangleKelpEntity entity = PvZEntityRegister.TANGLE_KELP.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (entity == null) {
				return;
			}
			entity.setPos(target.getX(), target.getY(), target.getZ());
			PlantUtil.copyPlantData(entity, this);
			this.level().addFreshEntity(entity);
			entity.setTarget(target);
			target.startRiding(entity, true,true);
			EntityUtil.playSound(entity, PvZSoundRegister.DRAG.get());
			if (--cnt <= 0) {
				break;
			}
		}
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		return super.canPAZTarget(entity)
				&& (entity.getVehicle() == null || entity.getVehicle().is(this));
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()));
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_ENHANCE_STRENGTH.get());
	}

	public int getSuperCount() {
		return 3;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.6F, 1F);
	}

	@Override
	public Vec3 getPassengerRidingPosition(Entity entity) {
		return super.getPassengerRidingPosition(entity).add(0, 0, 0);
	}

	@Override
	public boolean isNoGravity() {
		return this.isInWater();
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	protected boolean canBeImmuneToEnforce(Entity entity) {
		return true;
	}

	@Override
	public int getAttackCD() {
		return 20;
	}

	@Override
	public float getCloseHeight() {
		return 2;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TANGLE_KELP.get();
	}
}