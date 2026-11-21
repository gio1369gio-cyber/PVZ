package com.lying.pvz.event.client.handler;

import com.lying.pvz.api.invasion.IMissionType;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.OverlayPositionStore;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.invasion.InvasionSnapshot;
import com.lying.pvz.common.invasion.type.MissionTypes;
import com.lying.pvz.event.client.gamebus.PvZInputEvents;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Optional;


public class PvZOverlayHandler {

	private static final Identifier RESOURCE = StringUtil.id("textures/gui/overlay/resources.png");
	private static final Identifier INVASION = StringUtil.id("textures/gui/overlay/invasion.png");
	private static final Identifier TARGET = StringUtil.id("textures/gui/overlay/target.png");
	private static final int SUN_BAR_W1 = 157;
	private static final int SUN_BAR_H1 = 32;
	private static final int SUN_BAR_W2 = 122;
	private static final int EACH_W = 26;

	private static InvasionSnapshot lastSnap = null;
	private static long lastSnapReceiveMs = 0;
	private static final int TEX_W = 256;
	private static final int TEX_H = 256;

	public static void renderResources(GuiGraphicsExtractor guiGraphics) {
		// 位置由 OverlayPositionStore 的归一化锚点决定（只动位置，不动大小）。
		// 各 renderXxxBar 内部会 pushMatrix + scale(0.7) + blit(0,0)，因此外层先按屏幕像素平移即可。
		final float anchorX = OverlayPositionStore.x();
		final float anchorY = OverlayPositionStore.y();
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(anchorX * guiGraphics.guiWidth(), anchorY * guiGraphics.guiHeight());

		final int pos = PvZInputEvents.CurrentResourcePos;
		if(pos == 0 && ConfigUtil.renderSunBar()) {
			renderSunBar(guiGraphics);
		} else if(pos == 1 && ConfigUtil.renderMoneyBar()) {
			renderMoneyBar(guiGraphics);
		} else if(pos == 2 && ConfigUtil.renderGemBar()) {
			renderGemBar(guiGraphics);
		} else if(pos == 3 && ConfigUtil.renderTreeLevel()) {
			renderTreeLevel(guiGraphics);
		}

		guiGraphics.pose().popMatrix();
	}
	private static void renderSunBar(GuiGraphicsExtractor  guiGraphics) {
		final int max = PlayerUtil.getPlayerMaxSunNum(PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.TREE_LVL));
		final int now = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.SUN_NUM);
		final int len = MathUtil.getBarLen(now, max, SUN_BAR_W2);
		final float sz = 0.7F;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(sz, sz);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED,RESOURCE, 0, 0, 0, 0, SUN_BAR_W1, SUN_BAR_H1,TEX_W,TEX_H);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, 0, 0, 0, 32, 32 + len, SUN_BAR_H1,TEX_W,TEX_H);
		StringUtil.drawCenteredScaledString(guiGraphics, ClientUtil.getFont(), now + "", 95 + 1, 5 + 1, 6698496, 1.5f);
		StringUtil.drawCenteredScaledString(guiGraphics, ClientUtil.getFont(), now + "", 95, 5, Colors.WHITE, 1.5f);
		guiGraphics.pose().popMatrix();
	}

	public static void renderMoneyBar(GuiGraphicsExtractor guiGraphics) {
		final int now = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.MONEY);
		final float sz = 0.7F;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(sz, sz);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, 0, 0, 0, 96, SUN_BAR_W1, SUN_BAR_H1,TEX_W,TEX_H);
		StringUtil.drawCenteredScaledString(guiGraphics,ClientUtil.getFont(), now + "", 95 + 1, 5 + 1, 3610880, 1.5f);
		StringUtil.drawCenteredScaledString(guiGraphics,ClientUtil.getFont(), now + "", 95, 5, Colors.WHITE, 1.5f);
		guiGraphics.pose().popMatrix();
	}

	public static void renderGemBar(GuiGraphicsExtractor guiGraphics) {
		final int now = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.GEM_NUM);
		final float sz = 0.7F;
		guiGraphics.pose().pushMatrix();

		guiGraphics.pose().scale(sz, sz);

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED,RESOURCE, 0, 0, 0, 128, SUN_BAR_W1, SUN_BAR_H1,TEX_W,TEX_H);
		StringUtil.drawCenteredScaledString(guiGraphics, ClientUtil.getFont(), now + "", 95 + 1, 5, 46545, 1.5f);
		StringUtil.drawCenteredScaledString(guiGraphics, ClientUtil.getFont(), now + "", 95, 5, Colors.WHITE, 1.5f);
		guiGraphics.pose().popMatrix();
	}

	private static void renderTreeLevel(GuiGraphicsExtractor guiGraphics) {
		final int level = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.TREE_LVL);
		final int max = PlayerUtil.getPlayerLevelUpXp(level);
		final int now = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.TREE_XP);
		final int len = MathUtil.getBarLen(now, max, 120);
		final float sz = 0.7F;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(sz, sz);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED,RESOURCE, 0, 0, 0, 160, 157, 34,TEX_W,TEX_H);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED,RESOURCE, 34, 3, 34, 194, len, 8,TEX_W,TEX_H);
		StringUtil.drawCenteredScaledString(guiGraphics, ClientUtil.getFont(), "Lv." + level, 52, 18, Colors.WHITE, 1f);
		guiGraphics.pose().popMatrix();
	}


	public static void renderPlantFood(GuiGraphicsExtractor guiGraphics) {
		final int maxNum = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.MAX_ENERGY_NUM);
		int num = PlayerUtil.getResource(ClientUtil.getClientPlayer(), Resources.ENERGY_NUM);
		int h = guiGraphics.guiHeight();
		final float sz = 0.5F;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(sz, sz);

		/* render head */
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, 0, h * 2 - SUN_BAR_H1,
				0.0F, 64.0F, 34, SUN_BAR_H1, TEX_W, TEX_H);
		if (num > 0) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, 0, h * 2 - SUN_BAR_H1,
					162.0F, 45.0F, SUN_BAR_H1, SUN_BAR_H1, TEX_W, TEX_H);
		}
		/* render body */
		int currentX = 35;
		for (int i = 0; i < maxNum; i++) {
			final int u = (num-- > 0) ? 35 : 61;
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, currentX, h * 2 - SUN_BAR_H1,
					(float) u, 64.0F, EACH_W, SUN_BAR_H1, TEX_W, TEX_H);
			currentX += EACH_W;
		}

		/* render tail */
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RESOURCE, currentX, h * 2 - SUN_BAR_H1,
				153.0F, 64.0F, 4, SUN_BAR_H1, TEX_W, TEX_H);

		guiGraphics.pose().popMatrix();
	}


	public static void renderInvasionProgress(GuiGraphicsExtractor guiGraphics) {
		final InvasionSnapshot snap = ClientUtil.getClientPlayer().getData(PvZAttachments.INVASION_SNAPSHOT);
		if (!snap.running()) { lastSnap = null; return; }

		final List<Integer> waveTimes = snap.waveTimes();
		final int totalWaves = waveTimes.size();
		if (totalWaves == 0) return;

		// 检测到新 snapshot → 重置本地插值锚点
		if (snap != lastSnap) {
			lastSnap = snap;
			lastSnapReceiveMs = Util.getMillis();
		}

		// 本地插值：从快照 currentTick 起按时间推进，最多 +20 tick（两次同步的间隔）
		final long elapsedMs = Util.getMillis() - lastSnapReceiveMs;
		int estimatedTick = snap.currentTick() + (int) (elapsedMs / 50);
		estimatedTick = Math.min(estimatedTick, snap.currentTick() + 20);

		final int maxTime = waveTimes.get(totalWaves - 1);
		if (maxTime <= 0) return;

		final int P_WIDTH = 144, P_HEIGHT = 7;
		final int barLen = MathUtil.getBarLen(estimatedTick, maxTime, P_WIDTH);

		guiGraphics.pose().pushMatrix();
		final float sz = 0.7F;
		guiGraphics.pose().scale(sz, sz);

		final int WIDTH = 158, HEIGHT = 21;
		final int xRight = (int) (guiGraphics.guiWidth() / sz);
		final int yTop   = (int) (guiGraphics.guiHeight() / sz);

		// 背景
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
				xRight - WIDTH, yTop - HEIGHT,
				0.0F, 0.0F, WIDTH, HEIGHT, TEX_W, TEX_H);

		// 进度填充
		if (barLen > 0) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
					xRight - barLen - 7, yTop - HEIGHT + 7,
					(float) (149 - barLen + 1), 31.0F, barLen, P_HEIGHT, TEX_W, TEX_H);
		}

		// 波次标记
		for (int i = 0; i < totalWaves; i++) {
			final int waveLen = MathUtil.getBarLen(waveTimes.get(i), maxTime, P_WIDTH);
			if (i < snap.waveIndex()) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
						xRight - waveLen - 7, yTop - HEIGHT,
						1.0F, 49.0F, 14, 15, TEX_W, TEX_H);
			} else {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
						xRight - waveLen - 7, yTop - HEIGHT + 4,
						1.0F, 65.0F, 14, 11, TEX_W, TEX_H);
			}
		}

		// 指针
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
				xRight - barLen - 11, yTop - HEIGHT + 4,
				17.0F, 52.0F, 15, 12, TEX_W, TEX_H);

		guiGraphics.pose().popMatrix();
	}

	public static void renderMission(GuiGraphicsExtractor guiGraphics) {

		int h = guiGraphics.guiHeight();
		int w = guiGraphics.guiWidth();

		final InvasionSnapshot snap = ClientUtil.getClientPlayer().getData(PvZAttachments.INVASION_SNAPSHOT);
		if (!snap.running()) return;

		final Optional<Identifier> missionIdOpt = snap.missionTypeId();
		if (missionIdOpt.isEmpty()) return;

		final IMissionType mission = MissionTypes.get(missionIdOpt.get());
		if (mission == null) return;

		final int stage = snap.missionStage();
		final int now   = snap.missionValue();
		final int need  = mission.getRequiredValue(stage);
		if (need <= 0 || need == Integer.MAX_VALUE) return;

		guiGraphics.pose().pushMatrix();

		final int WIDTH = 72, HEIGHT = 24;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION, w - WIDTH, h - HEIGHT - 16,
				88.0F, 48.0F, WIDTH, HEIGHT, TEX_W, TEX_H);

		final int barLen = MathUtil.getBarLen(now, need, 66);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION, w - WIDTH + 3, h - HEIGHT + 3,
				91.0F, 75.0F, barLen, 2, TEX_W, TEX_H);

		// 任务图标：按 mission ID path 分支
		switch (missionIdOpt.get().getPath()) {
			case "collect_sun" -> guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
					w - WIDTH + 3, h - HEIGHT - 14,
					40.0F, 48.0F, 8, 8, TEX_W, TEX_H);
			case "kill", "instant_kill" -> guiGraphics.blit(RenderPipelines.GUI_TEXTURED, INVASION,
					w - WIDTH + 3, h - HEIGHT - 14,
					48.0F, 48.0F, 8, 8, TEX_W, TEX_H);
			default -> { }
		}


		StringUtil.drawScaledString(guiGraphics, ClientUtil.getFont(), mission.getName().getString(),
				w - WIDTH + 12, h - HEIGHT - 13, Colors.WHITE, 0.7F);
		StringUtil.drawScaledString(guiGraphics, ClientUtil.getFont(), now + "/" + need,
				w - WIDTH + 4, h - HEIGHT - 3, Colors.WHITE, 0.6F);

		guiGraphics.pose().popMatrix();

	}

	public static void renderTargetAim(GuiGraphicsExtractor guiGraphics) {
		final int WIDTH = 32, HEIGHT = 32;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TARGET,
				(guiGraphics.guiWidth() - WIDTH) / 2, (guiGraphics.guiHeight() - HEIGHT) / 2,
				0.0F, 0.0F, WIDTH, HEIGHT, TEX_W, TEX_H);
	}


	public static void renderFog(GuiGraphicsExtractor guiGraphics, float dep) {
		if (dep <= 0F) return;
		guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), ARGB.white(dep));;
	}


}