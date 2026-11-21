package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.widget.DisplayField.TipField;
import com.lying.pvz.common.menu.PeaGunMenu;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;

public class PeaGunScreen extends PvZContainerScreen<PeaGunMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/pea_gun.png");

	public PeaGunScreen(PeaGunMenu menu, Inventory inv, Component title) {
		super(menu, inv, title, 176, 187);
		this.tips.add(new TipField(2, 2, Arrays.asList(
				Component.translatable("gui.pvz.pea_gun.tip1"),
				Component.translatable("gui.pvz.pea_gun.tip2")
		)));
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);
	}
}