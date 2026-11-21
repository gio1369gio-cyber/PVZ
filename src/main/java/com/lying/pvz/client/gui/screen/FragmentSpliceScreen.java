package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.client.gui.widget.DisplayField;
import com.lying.pvz.common.level.blockentity.FragmentSpliceBlockEntity;
import com.lying.pvz.common.menu.FragmentSpliceMenu;
import com.lying.pvz.common.payload.ClickButtonPayload;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Arrays;

public class FragmentSpliceScreen extends PvZContainerScreen<FragmentSpliceMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/fragment_splice.png");
	protected Button craftButton;

	public FragmentSpliceScreen(FragmentSpliceMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 210, 225);

		this.tips.add(new DisplayField.TipField(3, 3, Arrays.asList(
				Component.translatable("gui.pvz.fragment_splice.tip1"),
				Component.translatable("gui.pvz.fragment_splice.tip2"),
				Component.translatable("gui.pvz.fragment_splice.tip3"),
				Component.translatable("gui.pvz.fragment_splice.tip4")
		)));
	}

	@Override
	protected void init() {
		super.init();
		this.craftButton = this.addRenderableWidget(
				Button.builder(
						Component.translatable("gui.pvz.fragment_splice"),
						(button) -> {
							if (this.craftButton.visible) {
								ClientPacketDistributor.sendToServer(
										new ClickButtonPayload(GuiHandler.FRAGMENT_SPLICE, 0, 0));
							}
						}
				).bounds(this.leftPos + 92, this.topPos + 122, 26, 14).build()
		);
		this.craftButton.visible = false;
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		this.craftButton.visible = this.menu.canCraft();
		super.extractContents(graphics, mouseX, mouseY, a);

		StringUtil.drawCenteredScaledString(graphics, this.font,
				Component.translatable("block.pvz.fragment_splice").getString(),
				this.leftPos + this.imageWidth / 2, this.topPos + 8, Colors.BLACK, 1F);
		StringUtil.drawCenteredScaledString(graphics, this.font,
				String.valueOf(this.menu.getSlotSun()),
				this.leftPos + 15, this.topPos + 67, Colors.BLACK, 0.5F);
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);

		final int maxLen = 88;
		final int len = MathUtil.getBarLen(this.menu.getSlotSun(),
				FragmentSpliceBlockEntity.CRAFT_COST, maxLen);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos + 7, this.topPos + 113 - len + 1,
				210, 0, 16, len, 256, 256);
	}
}