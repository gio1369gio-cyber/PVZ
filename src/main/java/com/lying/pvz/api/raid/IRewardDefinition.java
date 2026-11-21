package com.lying.pvz.api.raid;

import net.minecraft.server.level.ServerPlayer;

/**
 * 挑战奖励配置。
 */
public interface IRewardDefinition {

    String type();

    void reward(ServerPlayer player);

    void rewardGlobally(IChallenge challenge);
}