package com.lying.pvz.client.gui.search;


import com.lying.pvz.client.gui.screen.AbstractOptionScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;


public class OptionWidget extends AbstractWidget {

	private final AbstractOptionScreen<?> screen;
	private SearchOption option;

	public OptionWidget(AbstractOptionScreen<?> screen) {
		super(0, 0, 25, 25, Component.empty());
		this.screen = screen;
	}

	public void init(SearchOption a) { this.option = a; }

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		int posX = this.isOptionUnLocked() ? 29 : 29 + 25;
		int posY = 206;

		graphics.blit(RenderPipelines.GUI_TEXTURED, OptionSearchGui.TEXTURE,
				this.getX(), this.getY(), posX, posY,
				this.getWidth(), this.getHeight(), 256, 256);

		graphics.item(SearchOption.getItemStackByOption(this.option),
				this.getX() + 4, this.getY() + 4);

		if (!this.isOptionUnLocked()) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, OptionSearchGui.TEXTURE,
					this.getX() + 8, this.getY() + 8,
					81, 225, 7, 7, 256, 256);
		}
	}

	private boolean isOptionUnLocked() {
		return this.screen.isOptionUnLocked(this.option);
	}

	public List<Component> getToolTipText(Screen screen) {
		List<Component> list = new ArrayList<>();
		if (this.option != null) {
			list.add(this.option.getType().getText());
		}
		return list;
	}

	public void setPosition(int xx, int yy) {
		this.setX(xx);
		this.setY(yy);
	}

	public SearchOption getSearchOption() { return this.option; }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		if (this.option != null) {
			output.add(NarratedElementType.TITLE, this.option.getType().getText());
		}
	}
}