package com.lying.pvz.common.level.entity.plant.arma;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 金瓜投手。
 *
 * <p>继承 {@link MelonPultEntity} 的 {@link com.lying.pvz.common.projectile.BulletKinds#MELON}
 * 抛射与溅射；弹药变体固定为 {@link BulletKinds#MELON_GOLD}，渲染为金块。
 */
public class GoldenMelonPultEntity extends MelonPultEntity {
    public GoldenMelonPultEntity(EntityType<? extends MelonPultEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected int getMelonVariant() {
        return BulletKinds.MELON_GOLD;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.GOLDEN_MELON_PULT.get();
    }
}
