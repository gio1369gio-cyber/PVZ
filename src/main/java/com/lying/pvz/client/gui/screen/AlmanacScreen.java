package com.lying.pvz.client.gui.screen;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.client.gui.search.CategoryToggleWidget;
import com.lying.pvz.client.gui.search.SearchOption;
import com.lying.pvz.common.menu.AlmanacMenu;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AlmanacScreen extends AbstractOptionScreen<AlmanacMenu> {

	public static final Identifier TEXTURE = StringUtil.id("textures/gui/container/almanac.png");
	private static final int MAX_ENTRY_COUNT = 7;

	private final List<Pair<IAlmanacEntry, Number>> entries = new ArrayList<>();
	private @Nullable SearchOption option;
	private @Nullable Mob renderEntity;
	private int currentPos = 0;

	public AlmanacScreen(AlmanacMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 150, 200);
	}

	private int maxScroll() {
		return Math.max(0, this.entries.size() - MAX_ENTRY_COUNT);
	}

	/**
	 * 每帧刷新 option / renderEntity / entries / 滚动位置。
	 *
	 * <p>调用点放在 {@link #extractBackgroundTexture} 开头，确保<b>滚动条</b>与<b>条目内容</b>
	 * 使用同一帧数据（原实现里滚动条读的是上一帧的 {@code entries.size()}）。
	 */
	private void refreshState() {
		if (this.searchGui == null || this.minecraft == null || this.minecraft.level == null) return;

		final Optional<SearchOption> opt = this.searchGui.getCurrentOption();
		if (opt.isEmpty() || !this.isOptionUnLocked(opt.get())) {
			this.option = null;
			this.renderEntity = null;
			this.entries.clear();
			this.currentPos = 0;
			return;
		}

		final SearchOption current = opt.get();
		if (!current.equals(this.option)) {
			// option 结构变化 → 重建实体并重置滚动
			this.option = current;
			this.renderEntity = this.createRenderEntity(current);
			this.currentPos = 0;
		} else if (this.renderEntity == null) {
			// 上一帧因 level 未就绪等原因创建失败，补建
			this.renderEntity = this.createRenderEntity(current);
		}

		// 每帧重建 entries：IPAZEntity.addAlmanacEntries 可能包含运行时动态数据（击杀数等）
		this.entries.clear();
		if (this.renderEntity instanceof IPAZEntity paz) {
			paz.addAlmanacEntries(this.entries);
		}

		// 统一使用 maxScroll 作为上限，避免列表缩小时越界
		this.currentPos = Mth.clamp(this.currentPos, 0, this.maxScroll());
	}

	private @Nullable Mob createRenderEntity(SearchOption option) {
		if (this.minecraft == null || this.minecraft.level == null) return null;
		final var created = option.getType().getEntityType()
				.map(type -> type.create(this.minecraft.level, EntitySpawnReason.COMMAND))
				.orElse(null);
		return created instanceof Mob mob ? mob : null;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		// 只在 UI 内响应
		final int cornerX = this.leftPos;
		final int cornerY = this.topPos;
		if (mouseX < cornerX || mouseX >= cornerX + this.imageWidth
				|| mouseY < cornerY || mouseY >= cornerY + this.imageHeight) {
			return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		}
		final int max = this.maxScroll();
		if (max <= 0) return true;
		this.currentPos = Mth.clamp(this.currentPos - (int) Math.signum(scrollY), 0, max);
		return true;
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractContents(graphics, mouseX, mouseY, a);
		this.extractAlmanac(graphics, mouseX, mouseY);
	}

	private void extractAlmanac(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.option == null) return;

		this.extractTitle(graphics, this.option);
		this.extractLogo(graphics, this.option);
		//this.extractBar(graphics, this.option);

		if (!this.entries.isEmpty()) {
			// 绝对索引迭代 + 相对 y 偏移，结构性排除越界
			final int start = Mth.clamp(this.currentPos, 0, this.maxScroll());
			final int end = Math.min(this.entries.size(), start + MAX_ENTRY_COUNT);
			for (int i = start; i < end; ++i) {
				this.extractEntryAt(graphics, this.entries.get(i), i - start);
			}
		}
	}

	private void extractEntryAt(GuiGraphicsExtractor graphics, Pair<IAlmanacEntry, Number> pair, int pos) {
		final int incHeight = 20;
		final int posX = this.leftPos + 8 + 2;
		final int posY = this.topPos + 49 + 5;

		Number value = pair.getSecond();
		final String valueText;
		if (value instanceof Float f) {
			valueText = String.format("%.2f", f);
		} else if (value instanceof Double d) {
			valueText = String.format("%.2f", d);
		} else {
			valueText = String.format("%d", value.longValue());
		}

		final String text = pair.getFirst().getText().getString() + " : " + valueText;
		StringUtil.drawScaledString(graphics, this.font, text, posX, posY + incHeight * pos, Colors.WHITE, 1f);
	}

	private void extractTitle(GuiGraphicsExtractor graphics, SearchOption a) {
		int dx = this.leftPos + 49 + 94 / 2, dy = this.topPos + 9;
		StringUtil.drawCenteredScaledString(graphics, this.font,
				a.getType().getText().getString(), dx, dy, Colors.WHITE, 1f);
	}

	private void extractLogo(GuiGraphicsExtractor graphics, SearchOption a) {
		int dx = this.leftPos + 9, dy = this.topPos + 9;
		int scale = 2;
		float tx = (dx % scale) / (float) scale;
		float ty = (dy % scale) / (float) scale;

		graphics.pose().pushMatrix();
		graphics.pose().scale((float) scale, (float) scale);
		graphics.pose().translate(tx, ty);
		graphics.item(SearchOption.getItemStackByOption(a), dx / scale, dy / scale);
		graphics.pose().popMatrix();
	}

//	private void extractBar(GuiGraphicsExtractor graphics, SearchOption a) {
//		final int len = 22;
//		int posX = 53, posY = 25;
//		{
//			ItemStack guideBook = PVZPatchouliHandler.getPatchouliGuide();
//			if (guideBook != ItemStack.EMPTY) {
//				graphics.item(guideBook, this.leftPos + posX, this.topPos + posY);
//			} else {
//				graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
//						this.leftPos + posX, this.topPos + posY,
//						224, 0, 16, 16, 256, 256);
//			}
//		}
//		posX += len;
//		if (a.getType() instanceof IPlantType plantType) {
//			ItemStack itemStack = new ItemStack(plantType.getEssence().getEssenceItem());
//			graphics.item(itemStack, this.leftPos + posX, this.topPos + posY);
//		}
//		posX += len;
//		graphics.item(new ItemStack(a.getType().getRank().getTemplateCard()),
//				this.leftPos + posX, this.topPos + posY);
//		posX += len;
//		if (a.getType().getSkills().isEmpty()) {
//			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
//					this.leftPos + posX, this.topPos + posY,
//					224, 16, 16, 16, 256, 256);
//		} else {
//			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
//					this.leftPos + posX, this.topPos + posY,
//					240, 16, 16, 16, 256, 256);
//		}
//	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.option != null && this.extractBarTooltip(graphics, mouseX, mouseY)) {
			return;
		}
		super.extractTooltip(graphics, mouseX, mouseY);
	}

	private boolean extractBarTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		final int len = 22;
		int posX = this.leftPos + 53;
		int posY = this.topPos + 25;

		// guide book 无 tooltip
		posX += len;

		if (MathUtil.isInArea(mouseX, mouseY, posX, posY, 16, 16)) {
			if (this.option.getType() instanceof IPlantType) {
				Item item = ((IPlantType) this.option.getType()).getEssence().getEssenceItem();
				graphics.setComponentTooltipForNextFrame(this.font, List.of(
						Component.translatable("item.pvz." + BuiltInRegistries.ITEM.getKey(item).getPath())
				), mouseX, mouseY);
				return true;
			}
		}
		posX += len;

		if (MathUtil.isInArea(mouseX, mouseY, posX, posY, 16, 16)) {
			graphics.setComponentTooltipForNextFrame(this.font, List.of(
					Component.translatable("item.pvz." + BuiltInRegistries.ITEM.getKey(
							this.option.getType().getRank().getTemplateCard()).getPath())
			), mouseX, mouseY);
			return true;
		}
		posX += len;

		if (MathUtil.isInArea(mouseX, mouseY, posX, posY, 16, 16)) {
			List<Component> list = new ArrayList<>();
			this.option.getType().getSkills().forEach(skill ->
					list.add(skill.getText().withStyle(ChatFormatting.GREEN)));
			graphics.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		// 先刷新数据，保证滚动条和条目内容使用同一帧
		this.refreshState();

		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);

		final int max = this.maxScroll();
		if (max <= 0) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					this.leftPos + 130, this.topPos + 49, 162, 0, 12, 15, 256, 256);
		} else {
			final int len = MathUtil.getBarLen(this.currentPos, max, 143 - 15);
			graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
					this.leftPos + 130, this.topPos + 49 + len, 150, 0, 12, 15, 256, 256);
		}
	}

	/**
	 * 画实体预览（跟随鼠标旋转）。
	 *
	 * @param option 目标选项；只有与当前缓存一致时才返回实体，避免渲染过期实体
	 */
	public void extractEntityInInventory(GuiGraphicsExtractor graphics, int offsetX, int offsetY, float scale,
										 float vecX, float vecY, SearchOption option) {
		LivingEntity livingEntity = this.getRenderEntity(option);
		if (livingEntity == null) return;
		scale /= 0.5F;

		float mouseX = this.leftPos - vecX;
		float mouseY = this.topPos - vecY;

		int x1 = offsetX - 25;
		int y1 = offsetY - 37;
		int x2 = x1 + 51;
		int y2 = y1 + 75;

		InventoryScreen.extractEntityInInventoryFollowsMouse(
				graphics, x1, y1, x2, y2,
				(int) scale,
				0.0F,
				mouseX, mouseY,
				livingEntity);
	}

	/** 只读查询：返回当前缓存的实体，不修改任何状态。 */
	public @Nullable Mob getRenderEntity(SearchOption option) {
		if (this.renderEntity != null && option.equals(this.option)) {
			return this.renderEntity;
		}
		return null;
	}

	@Override
	public boolean isOptionUnLocked(SearchOption option) {
		if (this.minecraft == null || this.minecraft.player == null) return false;
		return !PlayerUtil.isPAZLocked(this.minecraft.player, option.getType());
	}

	@Override
	public List<CategoryToggleWidget.SearchCategories> getSearchCategories() {
		return Arrays.asList(CategoryToggleWidget.SearchCategories.ALL,
				CategoryToggleWidget.SearchCategories.PLANTS,
				CategoryToggleWidget.SearchCategories.ZOMBIES);
	}
}