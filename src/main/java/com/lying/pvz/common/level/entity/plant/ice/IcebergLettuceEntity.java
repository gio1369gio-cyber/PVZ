package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.IIceEffect;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.plant.base.PlantCloserEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 寒冰莴苣。
 *
 * <p>普通命中：冰冻一次后 {@code discard}；
 * super 模式：范围 20 格对全体目标施加冰冻 + 寒冷，并统计冰冻数量触发
 * {@code ENTITY_EFFECT_AMOUNT} 成就计数。
 */
public class IcebergLettuceEntity extends PlantCloserEntity implements IIceEffect {

	private static final int FROZEN_TICK = 200;

	public IcebergLettuceEntity(EntityType<? extends PlantCloserEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void performAttack(LivingEntity target) {
		for (int i = 0; i < 2; ++i) {
			EntityUtil.spawnParticle(this, 5);
		}
		EntityUtil.playSound(this, PvZSoundRegister.FROZEN.get());
		this.dealDamageTo(target);
		this.discard();
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		final float range = this.getSuperRange();
		for (int i = 0; i < 2; ++i) {
			EntityUtil.spawnParticle(this, 5);
		}
		EntityUtil.playSound(this, PvZSoundRegister.FROZEN.get());
		int cnt = 0;
		for (Entity target : EntityUtil.getTargetableEntities(this,
				EntityUtil.getEntityAABB(this, range, range))) {
			this.dealDamageTo(target);
			if (target instanceof LivingEntity living && EntityUtil.isEntityCold(living)) {
				++cnt;
			}
		}
		final Player player = EntityUtil.getEntityOwner(this.level(), this);
		if (player instanceof ServerPlayer sp) {
			PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(sp, this, cnt);
		}
	}

	private void dealDamageTo(Entity target) {
		final PvZEntityDamageSource source = PvZEntityDamageSource.causeIceDamage(this, this);
		this.getColdEffect().ifPresent(source::addEffect);
		this.getFrozenEffect().ifPresent(source::addEffect);
		target.hurt(source, 0.001F);
	}

	@Override
	public Optional<MobEffectInstance> getColdEffect() {
		return Optional.of(new MobEffectInstance(PvZEffectRegister.COLD_EFFECT,
				FROZEN_TICK + this.getColdDuration(), this.getColdLevel(), false, false));
	}

	@Override
	public Optional<MobEffectInstance> getFrozenEffect() {
		return Optional.of(new MobEffectInstance(PvZEffectRegister.FROZEN_EFFECT,
				FROZEN_TICK, 1, false, false));
	}

	public int getColdLevel() {
		return 1;
	}

	public int getColdDuration() {
		return 100;
	}

	public float getSuperRange() {
		return 20;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.6F, 0.6F);
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.ICEBERG_LETTUCE.get();
	}
}