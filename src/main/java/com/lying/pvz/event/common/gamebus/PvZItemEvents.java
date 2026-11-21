package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.level.item.PvZEggTint;
import com.lying.pvz.event.common.handler.HotbarCardSlotsHandler;
import com.lying.pvz.utils.StringUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZItemEvents {

    @SubscribeEvent
    public static void  itemPickup(ItemEntityPickupEvent.Pre event){
        HotbarCardSlotsHandler.onItemPickup(event);
    }

    @SubscribeEvent
    public static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(StringUtil.id("spawn_egg_color"), PvZEggTint.MAP_CODEC);
    }

}
