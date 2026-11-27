package com.lying.pvz.event.client.gamebus;

import com.google.common.reflect.TypeToken;
import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.gui.screen.PvZMainMenuScreen;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.roof.BungeeZombieEntity;
import com.lying.pvz.event.client.handler.PvZEntityRenderHandler;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

/**
 * 客户端 game bus 事件入口（渲染、tooltip、主菜单替换）。
 * 配置读取收敛到 ConfigUtil.get(...)；value = Dist.CLIENT 保持客户端侧限制。
 */
@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class PVZClientEvents {
	/** 每帧由 RenderStateExtensions 写回，RenderLivingEvent.Pre 时读取。 */
	public static final ContextKey<LivingEntity> ENTITY = new ContextKey<>(StringUtil.id("render_entity"));


	@SubscribeEvent
	public static void onLivingRender(RenderLivingEvent.Pre<?, ?, ?> ev) {
		final PoseStack stack = ev.getPoseStack();
		final LivingEntityRenderState rs = ev.getRenderState();
		final SubmitNodeCollector collector = ev.getSubmitNodeCollector();

		PvZEntityRenderHandler.checkAndRenderFrozenIce(rs, stack, collector);
		PvZEntityRenderHandler.checkAndRenderButter(rs, stack, collector);
	}


	@SubscribeEvent
	public static void onRegister(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(
				new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {},
				(LivingEntity entity, LivingEntityRenderState rs) -> {
					// 1) entity 塞进 extensions，供 RenderLivingEvent.Pre 里读
					rs.setRenderData(ENTITY, entity);
					// 2) 迷雾隐怪：雾隐标记 + 本地玩家无光眼 → 渲染态置为不可见（仅客户端，不影响服务端 AI）
					if (entity.hasEffect(PvZEffectRegister.FOG_HIDDEN)) {
						var local = ClientUtil.getClientPlayer();
						if (local == null || !local.hasEffect(PvZEffectRegister.LIGHT_EYE_EFFECT)) {
							rs.isInvisible = true;
							rs.isInvisibleToPlayer = true;
						}
					}
					// 3) 蹦极僵尸倒立（替代旧的 checkBungeeHandStand）
					if (entity instanceof PlantEntity || entity instanceof PVZZombieEntity) return;
					if (entity.getVehicle() instanceof BungeeZombieEntity && !rs.isUpsideDown) {
						rs.isUpsideDown = true;
						rs.xRot *= -1.0F;
						rs.yRot *= -1.0F;
					}
				});
	}



	@SubscribeEvent
	public static void onGuiOpened(ScreenEvent.Opening event) {
		if (ConfigUtil.get(PvZConfig.CLIENT_CONFIG.OtherSettings.ShowPVZMainMenu)) {//show pvz menu.
			if (event.getNewScreen() instanceof TitleScreen && ! (event.getNewScreen() instanceof PvZMainMenuScreen)) {
				event.setNewScreen(new PvZMainMenuScreen());
			}
		}
	}
}