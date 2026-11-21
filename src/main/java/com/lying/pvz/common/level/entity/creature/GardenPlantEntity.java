package com.lying.pvz.common.level.entity.creature;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.ability.IHasGroup;
import com.lying.pvz.api.entity.ability.IHasOwner;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class GardenPlantEntity extends PathfinderMob implements IHasOwner, IHasGroup {

    private static final EntityDataAccessor<Integer> AGE =
            SynchedEntityData.defineId(GardenPlantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STATE =
            SynchedEntityData.defineId(GardenPlantEntity.class, EntityDataSerializers.INT);

    public GardenPlantEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AGE, 0);
        builder.define(STATE, 0);
    }

    @Override
    public GroupType getEntityGroupType() {
        return GroupType.PLANTS;
    }

    @Override
    public Optional<UUID> getOwnerUUID() {
        return Optional.empty();
    }

    public enum GardenStates {
        NORMAL,
        INSECT,
        MUSIC,
        FERTILIZER,
        WATER,
        BEE
    }
}