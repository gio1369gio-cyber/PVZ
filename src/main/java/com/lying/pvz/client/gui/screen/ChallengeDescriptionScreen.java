package com.lying.pvz.client.gui.screen;

import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.utils.StringUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ChallengeDescriptionScreen extends Screen {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/challenge_envelope.png");
	private static final int LINE_HEIGHT = 20;
	private static final int TEXT_TOP_OFFSET = 30;
	private static final int TEXT_BOTTOM_PADDING = 6;

	private final int xSize = 256;
	private final int ySize = 160;
	private final IChallengeDefinition challengeComponent;

	public ChallengeDescriptionScreen(IChallengeDefinition challengeComponent) {
		super(Component.empty());
		this.challengeComponent = challengeComponent;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);

		final int cornerX = (this.width - this.xSize) / 2;
		final int cornerY = (this.height - this.ySize) / 2;
		final int midX = cornerX + this.xSize / 2;
		final int bottomLimit = cornerY + this.ySize - TEXT_BOTTOM_PADDING;

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				cornerX, cornerY, 0, 0, this.xSize, this.ySize, 256, 256);

		int midY = cornerY + TEXT_TOP_OFFSET;
		final List<Pair<MutableComponent, Integer>> messages = this.challengeComponent.getDescription();
		for (Pair<MutableComponent, Integer> message : messages) {
			if (midY > bottomLimit) break; // 不再画超出 UI 的行
			StringUtil.drawCenteredScaledString(graphics, this.font,
					message.getFirst().getString(), midX, midY, message.getSecond(), 1.2F);
			midY += LINE_HEIGHT;
		}
	}
}