package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.invasion.Invasion;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.event.common.handler.ChallengeEventHandler;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZlevelEvents {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        ChallengeEventHandler.onLevelTick(event);
    }
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel sl)) return;
        Invasion inv = InvasionManager.findOwning(event.getEntity());
        if (inv != null) inv.forgetEntity(event.getEntity().getUUID());
    }
}
