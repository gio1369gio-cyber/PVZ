package com.lying.pvz.client.gui.screen;

import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.api.lottery.SlotEntry;
import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.client.gui.widget.DisplayField;
import com.lying.pvz.common.level.blockentity.SlotMachineBlockEntity;
import com.lying.pvz.common.menu.SlotMachineMenu;
import com.lying.pvz.common.payload.ClickButtonPayload;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;

public class SlotMachineScreen extends PvZContainerScreen<SlotMachineMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/slot_machine.png");
	protected Button slowLotteryButton;
	protected Button fastLotteryButton;

	// tips 缓存：数值不变就不重建 Component 列表
	private int cachedSun = Integer.MIN_VALUE;
	private int cachedCost = Integer.MIN_VALUE;
	private int cachedRemain = Integer.MIN_VALUE;

	public SlotMachineScreen(SlotMachineMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 176, 227);
		this.tips.add(new DisplayField.TipField(4, 4, new ArrayList<>()));
	}

	@Override
	protected void init() {
		super.init();

		this.slowLotteryButton = this.addRenderableWidget(
				Button.builder(
						Component.translatable("gui.pvz.slot_machine.slow"),
						(button) -> {
							if (this.slowLotteryButton.visible) {
								ClientPacketDistributor.sendToServer(
										new ClickButtonPayload(GuiHandler.SLOT_MACHINE, 0, 0));
							}
						}
				).bounds(this.leftPos + 13, this.topPos + 119, 62, 20).build()
		);
		this.slowLotteryButton.visible = false;

		this.fastLotteryButton = this.addRenderableWidget(
				Button.builder(
						Component.translatable("gui.pvz.slot_machine.fast"),
						(button) -> {
							if (this.fastLotteryButton.visible) {
								ClientPacketDistributor.sendToServer(
										new ClickButtonPayload(GuiHandler.SLOT_MACHINE, 1, 0));
							}
						}
				).bounds(this.leftPos + 102, this.topPos + 119, 62, 20).build()
		);
		this.fastLotteryButton.visible = false;
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);
		// 滚动内容画在背景之后，与原来一致
		this.extractSlotMachineContent(graphics);
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		final boolean canLottery = this.canLottery();
		this.slowLotteryButton.visible = canLottery;
		this.fastLotteryButton.visible = canLottery;

		this.updateTips();

		super.extractContents(graphics, mouseX, mouseY, a);

		// 框架 + 标题（在 slot 之后）
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos + 61, this.topPos + 74, 61, 74, 54, 21, 256, 256);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos + 61, this.topPos + 1, 61, 1, 54, 21, 256, 256);

		StringUtil.drawCenteredScaledString(graphics, this.font, this.title.getString(),
				this.leftPos + this.imageWidth / 2, this.topPos + 6, Colors.BLACK, 1.2F);
	}

	private void updateTips() {
		SlotMachineBlockEntity te = this.menu.getTe();
		Player player = this.menu.getPlayer();
		if (te == null || player == null) return;

		final int sun = PlayerUtil.getResource(player, Resources.SUN_NUM);
		final int cost = te.getSunCost();
		final int chance = PlayerUtil.getResource(player, Resources.LOTTERY_CHANCE);
		final int remain = cost > 0 ? Math.min(chance, sun / cost) : chance;

		if (sun == this.cachedSun && cost == this.cachedCost && remain == this.cachedRemain) return;
		this.cachedSun = sun;
		this.cachedCost = cost;
		this.cachedRemain = remain;

		for (DisplayField e : this.tips) {
			if (e instanceof DisplayField.TipField tf) {
				tf.setTips(Arrays.asList(
						Component.translatable("gui.pvz.slot_machine.tip1"),
						Component.translatable("gui.pvz.slot_machine.tip2", sun, cost),
						Component.translatable("gui.pvz.slot_machine.tip3", remain)
				));
			}
		}
	}

	/**
	 * 画老虎机 4 行 × 3 列的滚动内容（绝对坐标）。
	 */
	private void extractSlotMachineContent(GuiGraphicsExtractor graphics) {
		SlotMachineBlockEntity te = this.menu.getTe();
		if (te == null) return;

		final Lottery lottery = te.getLottery();
		if (lottery == null) return;

		final int currentPos = te.array.get(13);
		final int total = Math.max(1, te.array.get(15));
		final int remaining = Math.max(0, Math.min(total, te.array.get(12)));
		final float percent = remaining / (float) total;

		graphics.pose().pushMatrix();
		graphics.pose().translate(0, (1 - percent) * 18);

		for (int i = 0; i < 4; ++i) {
			final int row = (currentPos + i - 2 + 4) % 4;
			for (int j = 0; j < 3; ++j) {
				SlotEntry entry = lottery.getSlotType(te.array.get(3 * row + j));
				if (entry == null) continue;
				final int x = this.leftPos + 62 + 18 * j;
				final int y = this.topPos + 58 - 18 * i;
				switch (entry.kind()) {
					case ITEM -> {
						ItemStack s = entry.createStack();
						if (s != null) graphics.item(s, x, y);
					}
					case SUN -> graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
							x, y, 176, 16, 16, 16, 256, 256);
					case JEWEL -> graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
							x, y, 176, 0, 16, 16, 256, 256);
					case EVENT -> graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
							x, y, 176, 48, 16, 16, 256, 256);
					default -> { }
				}
			}
		}
		graphics.pose().popMatrix();
	}

	private boolean canLottery() {
		SlotMachineBlockEntity te = this.menu.getTe();
		if (te == null) return false;
		return te.canRun(this.menu.getPlayer());
	}
}