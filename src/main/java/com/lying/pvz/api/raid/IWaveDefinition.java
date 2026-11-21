package com.lying.pvz.api.raid;

import java.util.List;

/**
 * 单个波次配置。
 */
public interface IWaveDefinition {

    String type();

    int getPrepareCD();

    int getLastDuration();

    List<ISpawnDefinition> getSpawns();

    IPlacementDefinition getPlacement();
}