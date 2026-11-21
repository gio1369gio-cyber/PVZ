package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.challenge.ChallengeTicker;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.event.common.handler.BiomeUnlockHandler;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.plant.magic.StrangeCatEntity;
import com.lying.pvz.event.common.handler.LivingEventHandler;
import com.lying.pvz.event.common.handler.PlayerEventHandler;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZLivingEvents {


    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent ev) {
        // BOSS 击杀 → 群系解锁推进（在击杀者判定之前，独立处理）
        BiomeUnlockHandler.onBossDeath(ev.getEntity(), ev.getSource().getEntity());

        if (ev.getEntity() instanceof ServerPlayer sp) {
            ChallengeTicker.onPlayerDeath(sp);
        }
        if (!ev.getEntity().level().isClientSide()) {
            final Entity killer = ev.getSource().getEntity();
            Player player = EntityUtil.getEntityOwner(ev.getEntity().level(), killer);
            if (player == null) {
                if (killer instanceof Player sourcePlayer) {
                    PlayerEventHandler.onPlayerKillEntity(sourcePlayer, ev.getSource(), ev.getEntity());
                }
            } else {
                PlayerEventHandler.onPlayerKillEntity(player, ev.getSource(), ev.getEntity());
                if (player instanceof ServerPlayer sp) {
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(sp, ev.getEntity(), ev.getSource());
                }
            }
        }

        if (ev.getEntity() instanceof Player player) {
            PlayerEventHandler.handlePlayerDeath(ev, player);
        }

        StrangeCatEntity.handleCopyCat(ev);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDamageOuter(LivingDamageEvent.Pre ev) {
        if (!ev.getEntity().level().isClientSide()) {
            AbstractPAZEntity.damageOuterDefence(ev);
            if (ev.getSource() instanceof PvZEntityDamageSource pds) {
                LivingEventHandler.handleHurtEffects(ev.getEntity(), pds);
                LivingEventHandler.handleHurtSounds(ev.getEntity(), pds);
            }
            LivingEventHandler.handleHurtDamage(ev);
        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingDamageInner(LivingDamageEvent.Pre ev) {
        AbstractPAZEntity.damageInnerDefence(ev);
    }
}
