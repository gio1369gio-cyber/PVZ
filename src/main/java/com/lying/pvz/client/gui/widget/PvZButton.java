package com.lying.pvz.client.gui.widget;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * 使用固定纹理 + UV 偏移的按钮。
 * <p>不画默认按钮底图、不画默认文本；文本 / 图标由屏幕自行绘制。</p>
 */
public abstract class PvZButton extends Button {

	protected final Identifier WIDGETS;

	public PvZButton(Identifier location, int x, int y, int width, int height, OnPress onPress) {
		this(location, x, y, width, height, "", onPress);
	}

	public PvZButton(Identifier location, int x, int y, int width, int height, String text, OnPress onPress) {
		super(x, y, width, height, Component.literal(text), onPress, Button.DEFAULT_NARRATION);
		this.WIDGETS = location;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		final Pair<Integer, Integer> uv = this.getButtonUV();
		final Pair<Integer, Integer> offset = this.getButtonUVOffset();

		int u = uv.getFirst() + (this.isHovered() ? offset.getFirst() : 0);
		int v = uv.getSecond() + (this.isHovered() ? offset.getSecond() : 0);

		graphics.blit(RenderPipelines.GUI_TEXTURED, this.WIDGETS,
				this.getX(), this.getY(),
				u, v,
				this.getWidth(), this.getHeight(),
				256, 256);
	}

	protected abstract Pair<Integer, Integer> getButtonUV();

	protected abstract Pair<Integer, Integer> getButtonUVOffset();
}