package com.lying.pvz.api.raid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * 生成位置配置。
 */
public interface IPlacementDefinition {

    String type();

    BlockPos getPlacePosition(Level level, BlockPos origin);
}