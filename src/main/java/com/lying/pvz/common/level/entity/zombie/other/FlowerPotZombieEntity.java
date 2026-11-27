package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.OtherZombies;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FlowerPotZombieEntity extends PVZZombieEntity {
    public FlowerPotZombieEntity(EntityType<? extends PVZZombieEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean canLostHand() {
        return super.canLostHand();
    }

    @Override
    public float getWalkSpeed() {
        return ZombieUtil.WALK_LITTLE_SLOW;
    }

    @Override
    public boolean canBeTargetBy(LivingEntity living) {
        if (living instanceof PlantShooterEntity) {
            return false;
        }
        return super.canBeTargetBy(living);
    }

    @Override
    public float getLife() {
        return 34;
    }

    @Override
    public IZombieType getZombieType() {
        return OtherZombies.FLOWER_POT_ZOMBIE.get();
    }
}