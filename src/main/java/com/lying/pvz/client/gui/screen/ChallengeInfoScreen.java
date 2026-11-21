package com.lying.pvz.client.gui.screen;

import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.api.raid.ISpawnDefinition;
import com.lying.pvz.api.raid.IWaveDefinition;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChallengeInfoScreen extends Screen {

	public static final Identifier TEXTURE = StringUtil.id("textures/gui/container/almanac.png");
	private static final int MAX_ENTRY_COUNT = 14;

	private static final float TEXT_SCALE = 1.0f;
	private static final int MAX_TEXT_WIDTH = 112;
	private static final int TEXT_START_X = 8 + 2;
	private static final int TEXT_START_Y = 49 + 5;
	private static final int INC_HEIGHT = 10;

	private static final int TITLE_SCROLL_GAP = 30;
	private static final int TITLE_SCROLL_SPEED_MS = 30;

	private final List<MutableComponent> entries = new ArrayList<>();
	private final IChallengeDefinition challengeComponent;
	private final int xSize = 150;
	private final int ySize = 200;
	private int currentPos = 0;
	private String cachedTitle = "";

	public ChallengeInfoScreen(IChallengeDefinition challengeComponent) {
		super(Component.empty());
		this.challengeComponent = challengeComponent;
	}

	@Override
	protected void init() {
		super.init();
		this.entries.clear();
		this.initEntries(this.challengeComponent);
		this.currentPos = Mth.clamp(this.currentPos, 0, this.maxScroll());
		this.cachedTitle = this.challengeComponent.getName().getString();
	}

	private int maxScroll() {
		return Math.max(0, this.entries.size() - MAX_ENTRY_COUNT);
	}

	private void initEntries(IChallengeDefinition challengeComponent) {
		for (int i = 0; i < challengeComponent.getTotalWaveCount(); ++i) {
			final IWaveDefinition waveComponent = challengeComponent.getWaves().get(i);
			this.addWrappedEntry(Component.translatable("gui.pvz.challenge_info.wave",
							i + 1, waveComponent.getLastDuration())
					.withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
			challengeComponent.getSpawns(i).forEach(spawn ->
					this.addWrappedEntry(Component.translatable("gui.pvz.challenge_info.spawn",
							spawn.getSpawnTick(), formatAmount(spawn),
							spawn.getSpawnType().getDescription().getString())));
		}
	}

	private static String formatAmount(ISpawnDefinition spawn) {
		final int min = spawn.getMinAmount();
		final int max = spawn.getMaxAmount();
		return min == max ? String.valueOf(min) : min + "-" + max;
	}

	private void addWrappedEntry(Component component) {
		final String text = component.getString();
		// 先按显式换行拆行，再逐行 wrap
		for (String logicalLine : text.split("\n", -1)) {
			this.addWrappedLine(logicalLine, component.getStyle());
		}
	}

	private void addWrappedLine(String line, Style style) {
		if (line.isEmpty()) {
			this.entries.add(Component.empty().withStyle(style));
			return;
		}

		final String[] tokens = line.split("(?<=\\s)|(?=\\s)");
		final StringBuilder currentLine = new StringBuilder();
		int currentWidth = 0;

		for (String token : tokens) {
			if (token.isEmpty()) continue;
			final int tokenWidth = this.font.width(token);

			if (currentWidth + tokenWidth <= MAX_TEXT_WIDTH) {
				currentLine.append(token);
				currentWidth += tokenWidth;
				continue;
			}

			if (currentLine.length() > 0) {
				this.entries.add(Component.literal(currentLine.toString()).withStyle(style));
				currentLine.setLength(0);
				currentWidth = 0;
			}

			if (tokenWidth <= MAX_TEXT_WIDTH) {
				currentLine.append(token);
				currentWidth = tokenWidth;
				continue;
			}

			// token 自身超宽：按 code point 硬拆
			int lineStart = 0;
			int lineWidth = 0;
			for (int i = 0; i < token.length(); ) {
				final int cp = token.codePointAt(i);
				final int charCount = Character.charCount(cp);
				final int charWidth = this.font.width(new String(Character.toChars(cp)));
				if (lineWidth + charWidth > MAX_TEXT_WIDTH && i > lineStart) {
					this.entries.add(Component.literal(token.substring(lineStart, i)).withStyle(style));
					lineStart = i;
					lineWidth = 0;
				}
				lineWidth += charWidth;
				i += charCount;
			}
			currentLine.append(token, lineStart, token.length());
			currentWidth = lineWidth;
		}

		if (currentLine.length() > 0) {
			this.entries.add(Component.literal(currentLine.toString()).withStyle(style));
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		final int cornerX = (this.width - this.xSize) / 2;
		final int cornerY = (this.height - this.ySize) / 2;
		if (mouseX < cornerX || mouseX >= cornerX + this.xSize
				|| mouseY < cornerY || mouseY >= cornerY + this.ySize) {
			return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		}
		final int max = this.maxScroll();
		if (max <= 0) return true;
		this.currentPos = Mth.clamp(this.currentPos - (int) Math.signum(scrollY), 0, max);
		return true;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);

		final int cornerX = (this.width - this.xSize) / 2;
		final int cornerY = (this.height - this.ySize) / 2;

		// 背景
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				cornerX, cornerY, 0, 0, this.xSize, this.ySize, 256, 256);

		// 滚动条
		final int max = this.maxScroll();
		if (max <= 0) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					cornerX + 130, cornerY + 49, 162, 0, 12, 15, 256, 256);
		} else {
			final int len = MathUtil.getBarLen(this.currentPos, max, 143 - 15);
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					cornerX + 130, cornerY + 49 + len, 150, 0, 12, 15, 256, 256);
		}

		// 标题
		final String title = this.cachedTitle;
		final int titleX = cornerX + 49;
		final int titleY = cornerY + 9;
		final int titleWidth = 141 - 49;
		final int textWidth = this.font.width(title);
		if (textWidth <= titleWidth) {
			StringUtil.drawCenteredScaledString(graphics, this.font, title,
					titleX + titleWidth / 2, titleY, Colors.WHITE, 1f);
		} else {
			graphics.pose().pushMatrix();
			graphics.enableScissor(titleX, cornerY + 8, titleX + titleWidth, cornerY + 19);
			final int totalWidth = textWidth + TITLE_SCROLL_GAP;
			final int offset = (int) ((Util.getMillis() / TITLE_SCROLL_SPEED_MS) % totalWidth);
			final int drawX = titleX - offset;
			graphics.text(this.font, title, drawX, titleY, Colors.WHITE, false);
			graphics.text(this.font, title, drawX + totalWidth, titleY, Colors.WHITE, false);
			graphics.disableScissor();
			graphics.pose().popMatrix();
		}

		// 图标
		graphics.pose().pushMatrix();
		graphics.pose().translate(cornerX + 9, cornerY + 9);
		graphics.pose().scale(2, 2);
		graphics.item(new ItemStack(PvZItemRegister.CHALLENGE_ENVELOPE.get()), 0, 0);
		graphics.pose().popMatrix();

		// 条目：只画 [start, start+MAX)，绝不越界
		final int start = Mth.clamp(this.currentPos, 0, max);
		final int end = Math.min(this.entries.size(), start + MAX_ENTRY_COUNT);
		for (int i = start; i < end; ++i) {
			StringUtil.drawScaledString(graphics, this.font,
					this.entries.get(i).getString(),
					cornerX + TEXT_START_X,
					cornerY + TEXT_START_Y + INC_HEIGHT * (i - start),
					Colors.WHITE, TEXT_SCALE);
		}
	}
}