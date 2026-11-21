package com.lying.pvz.client.gui.search;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CategoryToggleWidget extends AbstractWidget {

	protected SearchCategories category;

	private boolean stateTriggered;
	private Identifier resourceLocation;
	private int xTexStart;
	private int yTexStart;
	private int xDiffTex;
	private int yDiffTex;

	public CategoryToggleWidget(SearchCategories category) {
		super(0, 0, 35, 26, Component.empty());
		this.category = category;
		this.initTextureValues(153, 2, 35, 0, OptionSearchGui.TEXTURE);
	}

	public void initTextureValues(int xTexStart, int yTexStart, int xDiffTex, int yDiffTex, Identifier texture) {
		this.xTexStart = xTexStart;
		this.yTexStart = yTexStart;
		this.xDiffTex = xDiffTex;
		this.yDiffTex = yDiffTex;
		this.resourceLocation = texture;
	}

	public boolean isStateTriggered() { return this.stateTriggered; }
	public void setStateTriggered(boolean stateTriggered) { this.stateTriggered = stateTriggered; }

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		int posX = this.isStateTriggered() ? this.getX() - 2 : this.getX();
		int posY = this.getY();
		int texX = this.isStateTriggered() ? this.xTexStart + this.xDiffTex : this.xTexStart;
		int texY = this.isStateTriggered() ? this.yTexStart + this.yDiffTex : this.yTexStart;

		graphics.blit(RenderPipelines.GUI_TEXTURED, this.resourceLocation,
				posX, posY, texX, texY,
				this.getWidth(), this.getHeight(), 256, 256);

		this.extractCategoryIcon(graphics);
	}

	private void extractCategoryIcon(GuiGraphicsExtractor graphics) {
		final int posX = this.getX() + 9 + (this.isStateTriggered() ? -2 : 0);
		final int posY = this.getY() + 5;
		if (this.category == SearchCategories.ALL) {
			graphics.item(new ItemStack(Items.COMPASS), posX, posY);
		} else if (this.category == SearchCategories.PLANTS) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, OptionSearchGui.TEXTURE,
					posX, posY, 152, 32, 16, 16, 256, 256);
		} else if (this.category == SearchCategories.ZOMBIES) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, OptionSearchGui.TEXTURE,
					posX, posY, 168, 32, 16, 16, 256, 256);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE,
				Component.translatable("pvz.category." + this.category.name().toLowerCase()));
	}

	public SearchCategories getCategory() { return this.category; }

	public enum SearchCategories { ALL, PLANTS, ZOMBIES }
}