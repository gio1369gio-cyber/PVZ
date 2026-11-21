package com.lying.pvz.client.gui.search;

import com.google.common.collect.Lists;
import com.lying.pvz.client.gui.screen.AbstractOptionScreen;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;


public class OptionPage {

	private static final int MAX_NUM_PER_PAGE = 25;
	private static final int NUM_PER_ROW = 5;
	private final int xOffset = 10;
	private final int yOffset = 30;
	private final int size = 25;
	private final List<OptionWidget> buttons = Lists.newArrayListWithCapacity(MAX_NUM_PER_PAGE);
	private OptionWidget hoveredButton;
	private List<SearchOption> optionList;
	private Minecraft mc;
	private PageArrowWidget forwardButton;
	private PageArrowWidget backButton;
	private int totalPages;
	private int currentPage;
	private SearchOption lastClickedButton;

	public OptionPage(AbstractOptionScreen<?> screen) {
		for (int i = 0; i < MAX_NUM_PER_PAGE; ++i) {
			this.buttons.add(new OptionWidget(screen));
		}
	}

	public void init(Minecraft minecraft, int x, int y) {
		this.mc = minecraft;
		for (int i = 0; i < this.buttons.size(); ++i) {
			int xx = 2 + x + this.xOffset + this.size * (i % NUM_PER_ROW);
			int yy = 2 + y + this.yOffset + this.size * (i / NUM_PER_ROW);
			this.buttons.get(i).setPosition(xx, yy);
		}
		int width = 150, height = 170, dis = 90;
		int side = (width - dis) / 2;
		this.forwardButton = new PageArrowWidget(
				x + width / 2 + side, y + height, 12, 17,
				OptionSearchGui.TEXTURE, 1, 208);
		this.backButton = new PageArrowWidget(
				x + width / 2 - side - 13, y + height, 12, 17,
				OptionSearchGui.TEXTURE, 1, 208);
		this.forwardButton.visible = true;
		this.backButton.visible = true;
	}

	public void extract(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
		if (this.totalPages > 1) {
			String s = (this.currentPage + 1) + "/" + this.totalPages;
			StringUtil.drawCenteredScaledString(graphics, this.mc.font, s, x + 75, y + 175, Colors.WHITE, 1.5f);
		}
		this.hoveredButton = null;
		for (OptionWidget card : this.buttons) {
			card.extractRenderState(graphics, mouseX, mouseY, partialTicks);
			if (card.visible && card.isHovered()) {
				this.hoveredButton = card;
			}
		}
		this.backButton.extractRenderState(graphics, mouseX, mouseY, partialTicks);
		this.forwardButton.extractRenderState(graphics, mouseX, mouseY, partialTicks);
	}

	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.forwardButton.mouseClicked(event, doubleClick)) {
			++this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else if (this.backButton.mouseClicked(event, doubleClick)) {
			--this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else {
			for (OptionWidget card : this.buttons) {
				if (card.mouseClicked(event, doubleClick)) {
					this.lastClickedButton = card.getSearchOption();
					return true;
				}
			}
			return false;
		}
	}

	public void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.mc.gui.screen() != null && this.hoveredButton != null) {
			graphics.setComponentTooltipForNextFrame(
					this.mc.font,
					this.hoveredButton.getToolTipText(this.mc.gui.screen()),
					mouseX, mouseY);
		}
	}

	public void updateLists(List<SearchOption> list, boolean flag) {
		this.optionList = list;
		this.totalPages = (int) Math.ceil((double) list.size() / MAX_NUM_PER_PAGE);
		if (this.totalPages <= this.currentPage || flag) {
			this.currentPage = 0;
		}
		this.updateButtonsForPage();
	}

	private void updateButtonsForPage() {
		int i = MAX_NUM_PER_PAGE * this.currentPage;
		for (int j = 0; j < this.buttons.size(); ++j) {
			OptionWidget card = this.buttons.get(j);
			if (i + j < this.optionList.size()) {
				card.init(this.optionList.get(i + j));
				card.visible = true;
			} else {
				card.visible = false;
			}
		}
		this.updateArrowButtons();
	}

	private void updateArrowButtons() {
		this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
		this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
	}

	public List<SearchOption> getCurrentList(CategoryToggleWidget.SearchCategories category) {
		return SearchOption.getSearchOptionsByCategory(category);
	}

	public SearchOption getCurrentOption() { return this.lastClickedButton; }
	public void resetCurrentOption() { this.lastClickedButton = null; }

	private static class PageArrowWidget extends AbstractWidget {
		private final Identifier texture;
		private final int texX;
		private final int texY;

		PageArrowWidget(int x, int y, int width, int height,
						Identifier texture, int texX, int texY) {
			super(x, y, width, height, Component.empty());
			this.texture = texture;
			this.texX = texX;
			this.texY = texY;
		}

		@Override
		protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture,
					this.getX(), this.getY(), this.texX, this.texY,
					this.getWidth(), this.getHeight(), 256, 256);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput output) {
		}
	}
}