package com.lying.pvz.event.client.gamebus;


import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.invasion.InvasionSnapshot;
import com.lying.pvz.common.level.entity.plant.explosion.CobCannonEntity;
import com.lying.pvz.event.client.handler.PvZOverlayHandler;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.StringUtil;
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
        // 1. 迷雾渲染（视觉雾）：由入侵快照携带的 fog.strength 驱动
        // ==========================================
        if (ConfigUtil.get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderFog)) {
            // 光眼（路灯花光眼）驱雾：玩家有光眼时不渲染迷雾
            var player = ClientUtil.getClientPlayer();
            if (player != null && !player.hasEffect(PvZEffectRegister.LIGHT_EYE_EFFECT)) {
                final InvasionSnapshot snap = player.getData(PvZAttachments.INVASION_SNAPSHOT);
                if (snap.running()) {
                    snap.fog().ifPresent(fog -> {
                        float alpha = Math.max(0F, Math.min(fog.strength(), 1F));
                        if (alpha > 0F) {
                            PvZOverlayHandler.renderFog(guiGraphics, alpha);
                        }
                    });
                }
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