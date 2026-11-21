package com.lying.pvz.api.raid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;

/**
 * 单个生成条目配置。
 */
public interface ISpawnDefinition {
    String type();
    int getSpawnTick();
    int getMinAmount();
    int getMaxAmount();
    /** 服务端掷数；客户端禁止调用。 */
    int rollAmount(RandomSource random);
    IPlacementDefinition getPlacement();
    CompoundTag getNBT();
    EntityType<?> getSpawnType();
}