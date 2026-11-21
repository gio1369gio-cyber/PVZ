package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.IIceEffect;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.plant.arma.MelonPultEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 寒冰西瓜投手。
 *
 * <p>{@link BulletKinds#MELON_ICE} 变体：
 * <ul>
 *   <li>伤害走 {@code winter_melon} 伤害源；</li>
 *   <li>命中与溅射都追加寒冷效果。</li>
 * </ul>
 */
public class WinterMelonEntity extends MelonPultEntity implements IIceEffect {

	public WinterMelonEntity(EntityType<? extends MelonPultEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	public int getColdLvl() {
		return 7;
	}

	public int getColdTick() {
		return 80;
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

	@Override
	protected int getMelonVariant() {
		return BulletKinds.MELON_ICE;
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		super.configureShot(spec);
		this.getColdEffect().ifPresent(cold -> {
			spec.hitEffect(cold);
			spec.splashEffect(cold);
		});
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.WINTER_MELON.get();
	}
}
