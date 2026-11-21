package com.lying.pvz.event.client.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.common.level.entity.plant.explosion.CobCannonEntity;
import com.lying.pvz.common.payload.EntityInteractPayload;
import com.lying.pvz.event.client.modbus.ClientModBusEvents;
import com.lying.pvz.utils.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class PvZInputEvents {

	private static final int SWITCH_NUM = 4;
	public static boolean ShowOverlay = true;
	public static int CurrentResourcePos = 0;


	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.isWindowActive() && ClientUtil.getClientPlayer() != null) {

			/* change display of resource overlay */
			while (ClientModBusEvents.SHOW_OVERLAY.get().consumeClick()) {
				ShowOverlay = ! ShowOverlay;
			}

			/* change resource to display */
			while (ClientModBusEvents.LEFT_TOGGLE.get().consumeClick()) {
				changeToggle(- 1);
			}
			while (ClientModBusEvents.RIGHT_TOGGLE.get().consumeClick()) {
				changeToggle(1);
			}


			if (ClientUtil.getClientPlayer().getVehicle() instanceof CobCannonEntity) {
				CobCannonEntity cob = (CobCannonEntity) ClientUtil.getClientPlayer().getVehicle();
				while (ClientUtil.getMinecraft().options.keyUse.consumeClick()) {
					if (ClientUtil.getClientPlayer().getMainHandItem().isEmpty() && cob.getCornNum() > 0) {
						ClientPacketDistributor.sendToServer(new EntityInteractPayload(cob.getId(), 0, 0));
					}
				}
			}
		}
	}

	/**
	 * {@link #onClientTick(ClientTickEvent.Post)}
	 */
	private static void changeToggle(int offset) {
		int result = (CurrentResourcePos + offset + SWITCH_NUM) % SWITCH_NUM;
		int cnt = 0;//avoid endless loop.
		while(! checkCurrentPos(result)) {
			result = (result + offset + SWITCH_NUM) % SWITCH_NUM;
			if(++ cnt >= SWITCH_NUM + 1) {
				break;
			}
		}
		CurrentResourcePos = result;
	}

	private static boolean checkCurrentPos(int pos) {
		return pos == 0 ? ConfigUtil.renderSunBar() :
				pos == 1 ? ConfigUtil.renderMoneyBar() :
						pos == 2 ? ConfigUtil.renderGemBar() :
								pos == 3 ? ConfigUtil.renderTreeLevel() :
										false;
	}

}