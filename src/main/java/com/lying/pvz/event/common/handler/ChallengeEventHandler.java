package com.lying.pvz.event.common.handler;

import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.common.invasion.SafePeriod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * 挑战 + 入侵 统一入口。
 *
 */
public final class ChallengeEventHandler {

    private ChallengeEventHandler() { }


    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        ChallengeManager.tickChallenges(level);
        if (level.dimension() != Level.OVERWORLD) return;
        InvasionManager.tickActive();
        SafePeriod.tick(level);

    }

}