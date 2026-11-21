package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.widget.DisplayField.TipField;
import com.lying.pvz.common.menu.SunConverterMenu;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;

public class SunConverterScreen extends PvZContainerScreen<SunConverterMenu> {

	private static final Identifier TEXTURE =
			Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");

	public SunConverterScreen(SunConverterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title, 176, 166);
		this.tips.add(new TipField(2, 2, Arrays.asList(
				Component.translatable("gui.pvz.sun_converter.tip1"),
				Component.translatable("gui.pvz.sun_converter.tip2")
		)));
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractContents(graphics, mouseX, mouseY, a);
		StringUtil.drawCenteredScaledString(graphics, this.font,
				Component.translatable("block.pvz.sun_converter").getString(),
				this.leftPos + this.imageWidth / 2, this.topPos + 4, Colors.BLACK, 1F);
	}
}