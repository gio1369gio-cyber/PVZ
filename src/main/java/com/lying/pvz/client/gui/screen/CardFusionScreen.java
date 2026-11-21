package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.client.gui.widget.DisplayField;
import com.lying.pvz.common.level.blockentity.CardFusionBlockEntity;
import com.lying.pvz.common.menu.CardFusionMenu;
import com.lying.pvz.common.payload.ClickButtonPayload;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Arrays;

public class CardFusionScreen extends PvZContainerScreen<CardFusionMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/card_fusion.png");
	protected Button craftButton;

	public CardFusionScreen(CardFusionMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 178, 255);

		this.tips.add(new DisplayField.TipField(3, 3, Arrays.asList(
				Component.translatable("gui.pvz.card_fusion_table.tip1"),
				Component.translatable("gui.pvz.card_fusion_table.tip2"),
				Component.translatable("gui.pvz.card_fusion_table.tip3")
		)));
	}

	@Override
	protected void init() {
		super.init();
		this.topPos = Math.max(0, (this.height - this.imageHeight) / 2);

		this.craftButton = this.addRenderableWidget(
				Button.builder(
						Component.translatable("gui.pvz.fragment_splice"),
						(button) -> {
							if (this.craftButton.visible) {
								ClientPacketDistributor.sendToServer(
										new ClickButtonPayload(GuiHandler.CARD_FUSION, 0, 0));
							}
						}
				).bounds(this.leftPos + 76, this.topPos + 119, 26, 18).build()
		);
		this.craftButton.visible = false;
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		this.craftButton.visible = this.canCraftNow();
		super.extractContents(graphics, mouseX, mouseY, a);

		StringUtil.drawCenteredScaledString(graphics, this.font,
				Component.translatable("block.pvz.card_fusion_table").getString(),
				this.leftPos + this.imageWidth / 2, this.topPos + 8, Colors.BLACK, 1F);

		final float percent1 = this.percent(this.menu.getSunAmount(), CardFusionBlockEntity.CRAFT_SUN_COST);
		final float percent2 = this.percent(this.menu.getEssenceAmount(), CardFusionBlockEntity.CRAFT_ESSENCE_COST);

		// 阴影 + 主体
		StringUtil.drawCenteredScaledString(graphics, this.font, String.format("%.0f%%", percent1),
				this.leftPos + 19, this.topPos + 53, Colors.BLACK, 0.8F);
		StringUtil.drawCenteredScaledString(graphics, this.font, String.format("%.0f%%", percent2),
				this.leftPos + 161, this.topPos + 53, Colors.BLACK, 0.8F);
		StringUtil.drawCenteredScaledString(graphics, this.font, String.format("%.0f%%", percent1),
				this.leftPos + 18, this.topPos + 52, Colors.WHITE, 0.8F);
		StringUtil.drawCenteredScaledString(graphics, this.font, String.format("%.0f%%", percent2),
				this.leftPos + 160, this.topPos + 52, Colors.WHITE, 0.8F);
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);

		final int maxLen = 52;
		final int len1 = MathUtil.getBarLen(this.menu.getSunAmount(),
				CardFusionBlockEntity.CRAFT_SUN_COST, maxLen);
		final int len2 = MathUtil.getBarLen(this.menu.getEssenceAmount(),
				CardFusionBlockEntity.CRAFT_ESSENCE_COST, maxLen);
		if (len1 > 0) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					this.leftPos + 9, this.topPos + 77 - len1 + 1, 178, 0, 16, len1, 256, 256);
		}
		if (len2 > 0) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					this.leftPos + 153, this.topPos + 77 - len2 + 1, 194, 0, 16, len2, 256, 256);
		}
	}

	/** 百分比，clamp 到 [0, 100]，cost 为 0 时返回 0。 */
	private float percent(int amount, int cost) {
		if (cost <= 0) return 0F;
		return Math.max(0F, Math.min(100F, amount * 100.0F / cost));
	}

	protected boolean canCraftNow() {
		return this.menu.canCraft();
	}
}