package com.lying.pvz.client.gui.screen;

import com.lying.pvz.common.menu.CardPackMenu;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;


public class CardPackScreen extends PvZContainerScreen<CardPackMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/card_pack.png");

	public CardPackScreen(CardPackMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn,198,222);

	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);
	}
}