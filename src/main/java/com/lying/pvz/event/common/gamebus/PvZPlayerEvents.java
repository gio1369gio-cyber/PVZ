package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.event.player.PlayerLevelChangeEvent;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.common.level.item.tool.plant.BowlingGloveItem;
import com.lying.pvz.common.level.item.tool.plant.PeaGunItem;
import com.lying.pvz.event.common.handler.HotbarCardSlotsHandler;
import com.lying.pvz.event.common.handler.PlayerEventHandler;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZPlayerEvents {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        HotbarCardSlotsHandler.onPlayerTick(event);
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            PeaGunItem.checkHeadShoot(player);
        }
    }

    @SubscribeEvent
    public static void onPickUp(PlayerInteractEvent.EntityInteract ev) {
        BowlingGloveItem.onPickUp(ev);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer old && event.getEntity() instanceof ServerPlayer neu) {
            PlayerEventHandler.clonePlayerData(old,neu,event.isWasDeath());
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlayerEventHandler.onPlayerLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InvasionManager.handlePlayerLogout(player);
            PlayerUtil.getOptManager(player).ifPresent(PlayerDataManager::saveSummonCardCDs);
        }
    }

    @SubscribeEvent
    public static void onPlayerTreeLevelUp(PlayerLevelChangeEvent ev) {
        if (ev.getEntity() instanceof  ServerPlayer player) {
            if (ev.isLevelUp()) {
                PlayerEventHandler.unLockPAZs(player);
                PlayerUtil.playClientSound(player, PvZSoundRegister.PLANT_GROW.get());
                PlayerUtil.addResource(player, Resources.LOTTERY_CHANCE, 3);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InvasionManager.handlePlayerDimensionChange(player);
        }
    }
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        InvasionManager.clearAll();
    }

    @SubscribeEvent
    public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteract ev) {
        Player player = ev.getEntity();
        if (!ev.getLevel().isClientSide()) {
            if (ev.getHand() == InteractionHand.MAIN_HAND) {
                BowlingGloveItem.onPickUp(ev);
                PlayerEventHandler.quickRemoveByPlayer(player, ev.getTarget(), player.getMainHandItem());
                PlayerEventHandler.makeSuperMode(player, ev.getTarget(), player.getMainHandItem());
            }
        }
    }


}
