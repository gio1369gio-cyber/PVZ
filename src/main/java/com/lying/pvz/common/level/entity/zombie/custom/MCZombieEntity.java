package com.lying.pvz.common.level.entity.zombie.custom;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.level.entity.zombie.grass.NormalZombieEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MCZombieEntity extends NormalZombieEntity {
    public MCZombieEntity(EntityType<? extends NormalZombieEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getLife() {
        return 20;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.91f;
    }

    @Override
    public float getArmorToughness() {
        return 2;
    }

    @Override
    public IZombieType getZombieType() {
        return CustomZombies.MC_ZOMBIE.get();
    }
}