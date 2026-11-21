package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 巨型坚果（保龄球专用植物类型）。继承 {@link TallNutEntity}，仅替换植物类型 ID。
 */
public class GiantNutEntity extends TallNutEntity {

    public GiantNutEntity(EntityType<? extends TallNutEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(1.2F, 2.0F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.GIANT_NUT.get();
    }
}
