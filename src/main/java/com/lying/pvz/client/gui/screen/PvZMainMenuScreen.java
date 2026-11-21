package com.lying.pvz.client.gui.screen;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PvZMainMenuScreen extends TitleScreen {

	/** PVZ 静态主菜单背景（全屏铺满）。 */
	private static final Identifier PVZ_BACKGROUND = StringUtil.id("textures/gui/mainmenu/background.png");

	/** 所有文字按钮共用的 PVZ 按钮底图。 */
	private static final Identifier PVZ_BUTTON = StringUtil.id("textures/gui/mainmenu/button.png");

	@Override
	protected void init() {
		super.init();
		this.replaceButtonsWithTextOnly();
	}

	/** 把原版文字按钮换成「只画文字、不画背景」的按钮，点击行为委托回原按钮。 */
	private void replaceButtonsWithTextOnly() {
		final List<Button> buttons = new ArrayList<>();
		for (var widget : this.children()) {
			if (widget instanceof Button button && !(widget instanceof TextOnlyButton)) {
				buttons.add(button);
			}
		}
		for (Button button : buttons) {
			this.removeWidget(button);
			this.addRenderableWidget(new TextOnlyButton(button));
		}
	}

	@Override
	protected void extractPanorama(GuiGraphicsExtractor graphics, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, PVZ_BACKGROUND,
				0, 0, 0, 0, this.width, this.height, this.width, this.height);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
	}

	/** 无背景文字按钮：只渲染标签。 */
	public static class TextOnlyButton extends Button {

		private final Button delegate;

		public TextOnlyButton(Button original) {
			super(original.getX(), original.getY(), original.getWidth(), original.getHeight(),
					original.getMessage(), b -> {}, Button.DEFAULT_NARRATION);
			this.delegate = original;
			this.active = original.active;
			this.visible = original.visible;
		}

		@Override
		public void onPress(InputWithModifiers input) {
			this.delegate.onPress(input);
		}

		@Override
		protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
			
			graphics.blit(RenderPipelines.GUI_TEXTURED, PVZ_BUTTON, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(),
					800, 184, 800, 184);

			if (this.isHoveredOrFocused() && this.active) {
				graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x30FFFFFF);
			}


			this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
		}


	}
}
