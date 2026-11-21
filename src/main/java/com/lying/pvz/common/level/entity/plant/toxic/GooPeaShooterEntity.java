package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.plant.appease.PeaShooterEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 毒液豌豆。
 *
 * <p>发射 {@link com.lying.pvz.common.projectile.BulletKinds#PEA} 并附加中毒效果
 * （{@link com.lying.pvz.common.init.PvZEffectRegister#GOO_POISON}）。
 */
public class GooPeaShooterEntity extends PeaShooterEntity {
    public GooPeaShooterEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        spec.hitEffect(this.getPoisonEffect());
    }

    public MobEffectInstance getPoisonEffect() {
        return new MobEffectInstance(PvZEffectRegister.GOO_POISON, 100, 0, false, false);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.GOO_PEA_SHOOTER.get();
    }
}
