package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.search.CategoryToggleWidget;
import com.lying.pvz.client.gui.search.OptionSearchGui;
import com.lying.pvz.client.gui.search.SearchOption;
import com.lying.pvz.common.menu.AbstractOptionMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;

import java.util.List;


public abstract class AbstractOptionScreen<T extends AbstractOptionMenu> extends PvZContainerScreen<T> {

	protected final OptionSearchGui searchGui = new OptionSearchGui();

	public AbstractOptionScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	public AbstractOptionScreen(T screenContainer, Inventory inv, Component titleIn, int imageWidth, int imageHeight) {
		super(screenContainer, inv, titleIn, imageWidth, imageHeight);
	}
	@Override
	protected void init() {
		super.init();
		this.searchGui.init(this.minecraft, this, this.menu, this.width, this.height);
		this.leftPos = this.searchGui.updateScreenPosition(this.imageWidth, this.imageHeight);
		this.searchGui.initSearchBar();
		this.addRenderableWidget(this.searchGui);

		this.setInitialFocus(this.searchGui);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.searchGui.tick();
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractContents(graphics, mouseX, mouseY, a);
		this.searchGui.getRecipeManager().extract(this.minecraft, graphics, this.leftPos, this.topPos, a);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.searchGui.isMouseOver(mouseX, mouseY)) {
			this.searchGui.extractTooltip(graphics, this.leftPos, this.topPos, mouseX, mouseY);
		} else {
			super.extractTooltip(graphics, mouseX, mouseY);
		}
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn) {
		boolean flag = mouseX < guiLeftIn || mouseY < guiTopIn
				|| mouseX >= guiLeftIn + this.imageWidth || mouseY >= guiTopIn + this.imageHeight;
		return this.searchGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos) && flag;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.searchGui.mouseClicked(event, doubleClick)) {
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
		super.slotClicked(slot, slotId, buttonNum, containerInput);
		this.searchGui.slotClicked(slot);
	}

	@Override
	public void removed() {
		this.searchGui.removed();
		super.removed();
	}

	public abstract boolean isOptionUnLocked(SearchOption option);

	public abstract List<CategoryToggleWidget.SearchCategories> getSearchCategories();
}