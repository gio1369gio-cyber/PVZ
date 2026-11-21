package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.IIceEffect;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.plant.arma.CabbagePultEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 冰卷心菜投手。
 *
 * <p>抛射 {@link com.lying.pvz.common.projectile.BulletKinds#CABBAGE} 并附加寒冷/冰冻效果
 * （{@link com.lying.pvz.common.init.PvZEffectRegister#COLD_EFFECT} /
 * {@link com.lying.pvz.common.init.PvZEffectRegister#FROZEN_EFFECT}）。
 */
public class IceCabbagePultEntity extends CabbagePultEntity implements IIceEffect {
    private static final int FROZEN_TICK = 15;

    public IceCabbagePultEntity(EntityType<? extends CabbagePultEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        this.getColdEffect().ifPresent(spec::hitEffect);
        this.getFrozenEffect().ifPresent(spec::hitEffect);
    }

    /** 冰卷心菜：弹药变体固定为 {@link com.lying.pvz.common.projectile.BulletKinds#CABBAGE_ICE}。 */
    @Override
    protected int getCabbageVariant() {
        return com.lying.pvz.common.projectile.BulletKinds.CABBAGE_ICE;
    }

    public int getColdDuration() {
        return 50;
    }

    @Override
    public Optional<MobEffectInstance> getColdEffect() {
        return Optional.of(new MobEffectInstance(PvZEffectRegister.COLD_EFFECT, FROZEN_TICK + this.getColdDuration(), 2, false, false));
    }

    @Override
    public Optional<MobEffectInstance> getFrozenEffect() {
        return Optional.of(new MobEffectInstance(PvZEffectRegister.FROZEN_EFFECT, FROZEN_TICK, 2, false, false));
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.ICE_CABBAGE_PULT.get();
    }
}
