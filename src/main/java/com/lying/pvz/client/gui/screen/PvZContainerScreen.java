package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.gui.widget.DisplayField;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * PVZ 容器界面基类。
 *
 * <p>26.3 渲染分为三阶段：
 * <ol>
 *   <li>{@link #extractBackground}：透明背景 → {@link #extractBackgroundTexture 子类纹理} → {@link #extractTips tips 图标}</li>
 *   <li>{@link #extractContents}：默认由 {@code AbstractContainerScreen} 画 slot；子类可覆写补充</li>
 *   <li>{@link #extractTooltip}：{@link #tips} tooltip 优先，否则交给 {@code super}（slot tooltip）</li>
 * </ol>
 */
public abstract class PvZContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	private static final Identifier WIDGETS = StringUtil.id("textures/gui/widgets.png");

	protected final List<DisplayField> tips = new ArrayList<>();

	public PvZContainerScreen(T menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	public PvZContainerScreen(T menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
		super(menu, inventory, title, imageWidth, imageHeight);
	}
	// ------------------------------------------------------------------
	// 背景
	// ------------------------------------------------------------------

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);   // 透明背景
		this.extractBackgroundTexture(graphics, mouseX, mouseY, a);
		this.extractTips(graphics);
	}

	/**
	 * 子类覆写此方法画主背景纹理及装饰（进度条、滚动条等）。
	 * <p>此方法在透明背景之后、tips 之前调用。</p>
	 */
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
	}

	/** 画 tips 图标。 */
	protected void extractTips(GuiGraphicsExtractor graphics) {
		for (DisplayField tip : this.tips) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS,
					this.leftPos + tip.getX(), this.topPos + tip.getY(),
					tip.getTexX(), tip.getTexY(),
					tip.getWidth(), tip.getHeight(),
					256, 256);
		}
	}

	// ------------------------------------------------------------------
	// 文本
	// ------------------------------------------------------------------

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		// PVZ 默认不画标题/玩家背包标题，由子类覆写
	}

	// ------------------------------------------------------------------
	// tooltip
	// ------------------------------------------------------------------

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		for (DisplayField tip : this.tips) {
			if (tip.isInField(mouseX - this.leftPos, mouseY - this.topPos)) {
				graphics.setComponentTooltipForNextFrame(this.font, tip.getTexts(), mouseX, mouseY);
				return;
			}
		}
		super.extractTooltip(graphics, mouseX, mouseY);
	}
}