package com.lying.pvz.api.invasion;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * 任务类型。
 */
public interface IMissionType {

    Identifier getID();

    MutableComponent getName();

    int getMaxStage();

    int getRequiredValue(int stage);

    int getWeight();

    default void onStageComplete(ServerPlayer player, int stage) {
    }
}