package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 原始蘑菇。
 *
 * <p>继承 {@link FumeShroomEntity} 的穿透弹 {@link com.lying.pvz.common.projectile.BulletKinds#FUME}，
 * 命中额外附加虚弱。
 */
public class OriginShroomEntity extends FumeShroomEntity {
    public OriginShroomEntity(EntityType<? extends FumeShroomEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        spec.hitEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80));
    }

    @Override
    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get()) * 0.8F;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.ORIGIN_SHROOM.get();
    }
}
