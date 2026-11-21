package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.IIceEffect;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity.ElementTypes;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 寒冰菇。
 *
 * <p>爆炸范围 20 格，对范围内 {@link LivingEntity} 施加寒冷 + 冰冻；
 * 顺带清除 {@link ElementTypes#FLAME} 系元素球。
 * 客户端播放 3 个原版爆炸粒子 + 15 个雪花粒子。
 */
public class IceShroomEntity extends PlantBomberEntity implements IIceEffect {

	public IceShroomEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void startBomb(boolean server) {
		if (server) {
			final float len = this.getExplodeRange();
			final AABB aabb = EntityUtil.getEntityAABB(this, len, len);
			int cnt = 0;
			for (LivingEntity entity : EntityUtil.getTargetableLivings(this, aabb)) {
				final PvZEntityDamageSource source = PvZEntityDamageSource.causeIceDamage(this, this);
				this.getColdEffect().ifPresent(source::addEffect);
				this.getFrozenEffect().ifPresent(source::addEffect);
				entity.hurt(source, this.getExplodeDamage());
				if (EntityUtil.isEntityCold(entity)) {
					++cnt;
				}
			}
			EntityUtil.playSound(this, PvZSoundRegister.FROZEN.get());
			final Player player = EntityUtil.getEntityOwner(this.level(), this);
			if (player instanceof ServerPlayer sp) {
				PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(sp, this, cnt);
			}
			ElementBallEntity.killElementBalls(this, 40, ElementTypes.FLAME);
		} else {
			for (int i = 0; i < 3; ++i) {
				this.level().addParticle(ParticleTypes.EXPLOSION,
						this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
			for (int i = 0; i < 15; ++i) {
				this.level().addParticle(PvZParticleRegister.SNOW_FLOWER.get(),
						this.getX(), this.getY(), this.getZ(),
						(this.getRandom().nextFloat() - 0.5F) / 4,
						this.getRandom().nextFloat() / 5,
						(this.getRandom().nextFloat() - 0.5F) / 4);
			}
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.COLD_LEVEL, this.getColdLvl()),
				Pair.of(PAZAlmanacs.COLD_TIME, this.getColdTick()),
				Pair.of(PAZAlmanacs.FROZEN_LEVEL, this.getFrozenLvl()),
				Pair.of(PAZAlmanacs.FROZEN_TIME, this.getFrozenTick())
		));
	}

	@Override
	public int getReadyTime() {
		return 20;
	}

	@Override
	public float getExplodeRange() {
		return 20;
	}

	@Override
	public float getExplodeDamage() {
		return 0.1F;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.85F, 1.35F);
	}

	public int getColdLvl() {
		return 1;
	}

	public int getColdTick() {
		return 0;
	}

	public int getFrozenLvl() {
		return 0;
	}

	public int getFrozenTick() {
		return 100;
	}

	@Override
	public Optional<MobEffectInstance> getColdEffect() {
		return Optional.of(new MobEffectInstance(PvZEffectRegister.COLD_EFFECT,
				this.getColdTick() + this.getFrozenTick(), this.getColdLvl(), false, false));
	}

	@Override
	public Optional<MobEffectInstance> getFrozenEffect() {
		return Optional.of(new MobEffectInstance(PvZEffectRegister.FROZEN_EFFECT,
				this.getFrozenTick(), this.getFrozenLvl(), false, false));
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.ICE_SHROOM.get();
	}
}