package com.lying.pvz.client;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.client.impl.PVZClientLookupImpl;
import com.lying.pvz.client.registry.PVZModelRegistrations;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 客户端事件注册入口。
 *
 */
public final class PVZClientSetup {

    private PVZClientSetup() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(PVZClientSetup::onClientSetup);
    }

    /* ------------------------------------------------------------------ */
    /* FMLClientSetupEvent                                                 */
    /* ------------------------------------------------------------------ */

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 1) 填充模型 / 贴图 / 缩放注册表
            PVZModelRegistrations.registerAll();
            // 2) 注入客户端查询视图
            PVZAPI.bootstrapClient(new PVZClientLookupImpl());
        });
    }


}