package com.lying.pvz.common.level.entity.plant.ice;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 双发寒冰（寒冰连发）。
 *
 * <p>继承 {@link SnowPeaEntity} 的冰豌豆弹（{@link com.lying.pvz.common.projectile.BulletKinds#PEA_ICE}），
 * 一次攻击连发两发，命中概率附加冰冻。
 */
public class ReIcePeaEntity extends SnowPeaEntity {
    private static final int FROZEN_TICK = 10;

    public ReIcePeaEntity(EntityType<? extends SnowPeaEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void startShootAttack() {
        this.setAttackTime(2);
    }

    @Override
    public Optional<MobEffectInstance> getFrozenEffect() {
        return this.getRandom().nextBoolean()
                ? Optional.of(new MobEffectInstance(PvZEffectRegister.FROZEN_EFFECT, FROZEN_TICK, 2, false, false))
                : Optional.empty();
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.RE_ICEPEA.get();
    }
}
