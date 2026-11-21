package com.lying.pvz.event.client.gamebus;


import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.common.level.entity.plant.explosion.CobCannonEntity;
import com.lying.pvz.event.client.handler.PvZOverlayHandler;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * 客户端 HUD overlay 事件入口。
 */
@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class PvZOverlayEvents {

    // 迷雾完全浓密的持续时间（例如 100 tick，即 5 秒内完全看不见）
    private static final int FOG_MAX_TICK = 200;


    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(StringUtil.id("pvz_overlay"), PvZOverlayEvents::renderOverlayLayer);
    }

    private static void renderOverlayLayer(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        if (!canRender() || ClientUtil.getMinecraft().gui.hud.isHidden()) {
            return;
        }

        // ==========================================
        // 1. 迷雾渲染 (放在 HUD 之前渲染，或者单独放外面)
        // ==========================================
        if (ConfigUtil.get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderFog)) {
            int fogTick = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.NO_FOG_TICK);
            if (fogTick > 0) {
                // 计算透明度：如果 fogTick 大于 FOG_MAX_TICK，则保持 1.0F（完全遮蔽）
                // 如果小于 FOG_MAX_TICK，则按比例逐渐变淡
                float alpha = Math.min((float) fogTick / FOG_MAX_TICK, 1.0F);
                PvZOverlayHandler.renderFog(guiGraphics, alpha);
            }
        }

        if (ClientUtil.getMinecraft().gui.screen() == null && PvZInputEvents.ShowOverlay) {
            if (!ClientUtil.getMinecraft().getDebugOverlay().showDebugScreen()) {
                PvZOverlayHandler.renderResources(guiGraphics);
            }

            if (ConfigUtil.get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderPlantFoodBar)) {
                PvZOverlayHandler.renderPlantFood(guiGraphics);
            }

            if (ConfigUtil.get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderInvasionProgress)) {
                PvZOverlayHandler.renderInvasionProgress(guiGraphics);
                PvZOverlayHandler.renderMission(guiGraphics);
            }

            if (ClientUtil.getClientPlayer().getVehicle() instanceof CobCannonEntity cob) {
                if (cob.getCornNum() > 0) {
                    PvZOverlayHandler.renderTargetAim(guiGraphics);
                }
            }

        }
    }

    private static boolean canRender() {

        return ClientUtil.getClientPlayer() != null && !ClientUtil.getClientPlayer().isSpectator();
    }
}