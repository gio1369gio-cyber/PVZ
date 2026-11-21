package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.IIceEffect;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.appease.PeaShooterEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 寒冰射手。
 *
 * <p>发射 {@link com.lying.pvz.common.projectile.BulletKinds#PEA} 的 ICE 变体，
 * 命中与效果由 {@link com.lying.pvz.common.projectile.BulletImpacts#effectFromVariant()} 统一施加。
 */
public class SnowPeaEntity extends PeaShooterEntity implements IIceEffect {

	public SnowPeaEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public Optional<MobEffectInstance> getColdEffect() {
		return Optional.of(new MobEffectInstance(PvZEffectRegister.COLD_EFFECT,
				this.getColdTick(), this.getColdLvl(), false, false));
	}

	@Override
	public Optional<MobEffectInstance> getFrozenEffect() {
		return Optional.empty();
	}

	/**
	 * 新架构：冰豌豆走 {@code Variant.ICE} + 每发效果，
	 * 不再覆写旧 {@code createBullet()}。
	 */
	@Override
	protected void configureShot(ShotSpec spec) {
		super.configureShot(spec);
		spec.variant(BulletKinds.PEA_ICE);
		this.getColdEffect().ifPresent(spec::hitEffect);
		this.getFrozenEffect().ifPresent(spec::hitEffect);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.COLD_LEVEL, this.getColdLvl()),
				Pair.of(PAZAlmanacs.COLD_TIME, this.getColdTick())
		));
	}

	public int getColdLvl() {
		return 5;
	}

	public int getColdTick() {
		return 80;
	}

	@Override
	protected SoundEvent getShootSound() {
		return PvZSoundRegister.SNOW_SHOOT.get();
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SNOW_PEA.get();
	}
}