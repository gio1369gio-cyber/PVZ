package com.lying.pvz.client.gui.screen;

import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.client.gui.search.PvZRecipeManager;
import com.lying.pvz.client.gui.widget.PvZButton;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.menu.EssenceAltarMenu;
import com.lying.pvz.common.payload.ClickButtonPayload;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EssenceAltarScreen extends PvZContainerScreen<EssenceAltarMenu> {

	private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/essence_altar.png");
	private static final int MAX_ENTRY_COUNT = 4;
	private final PvZRecipeManager recipeManager = new PvZRecipeManager();
	private final EssenceButton[] buttons = new EssenceButton[MAX_ENTRY_COUNT];
	private IPAZType tmpType;
	private int currentPos = 0;
	private int mouseX;
	private int mouseY;

	public EssenceAltarScreen(EssenceAltarMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 176, 166);
	}

	@Override
	protected void init() {
		super.init();
		for (int i = 0; i < MAX_ENTRY_COUNT; ++i) {
			final int pos = i;
			this.buttons[i] = new EssenceButton(this.leftPos + 69, this.topPos + 5 + 19 * i, button -> {
				if (button.visible) {
					ClientPacketDistributor.sendToServer(
							new ClickButtonPayload(GuiHandler.ESSENCE_ALTAR, this.currentPos + pos, 0));
				}
			});
			this.addRenderableWidget(this.buttons[i]);
			this.buttons[i].visible = true;
		}
	}

	@Override
	protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
				this.leftPos, this.topPos, 0, 0,
				this.imageWidth, this.imageHeight, 256, 256);

		final int count = this.menu.getAvailableSkills().size();
		if (count > MAX_ENTRY_COUNT) {
			final int len = MathUtil.getBarLen(this.currentPos, count - MAX_ENTRY_COUNT, 76 - 15);
			graphics.blit(RenderPipelines.GUI_TEXTURED, StringUtil.WIDGETS,
					this.leftPos + 156, this.topPos + 5 + len,
					15, 0, 12, 15, 256, 256);
		} else {
			graphics.blit(RenderPipelines.GUI_TEXTURED, StringUtil.WIDGETS,
					this.leftPos + 156, this.topPos + 5,
					27, 0, 12, 15, 256, 256);
		}
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;

		// 关键：skills 与 levels 用同一份快照，避免 handler 中途变化导致长度不一致
		final List<ISkillType> skills = this.menu.getAvailableSkills();
		final List<Integer> levels = this.menu.getCurrentSkillLevel(skills);
		this.currentPos = Mth.clamp(this.currentPos, 0, Math.max(0, skills.size() - MAX_ENTRY_COUNT));
		final int count = Math.min(skills.size(), MAX_ENTRY_COUNT);
		for (int i = 0; i < MAX_ENTRY_COUNT; ++i) {
			this.buttons[i].visible = (i < count);
		}

		super.extractContents(graphics, mouseX, mouseY, a);

		for (int i = 0; i < MAX_ENTRY_COUNT; ++i) {
			if (!this.buttons[i].visible) continue;
			final int idx = this.currentPos + i;
			if (idx < 0 || idx >= skills.size() || idx >= levels.size()) continue;
			this.extractEntry(graphics, i, skills.get(idx), levels.get(idx));
		}

		// recipe 同步：只依赖 tmpType 缓存；已填充槽由 PvZRecipeManager 内部动态跳过
		final Optional<IPAZType> pazTypeOpt = this.menu.getPAZType();
		if (pazTypeOpt.isPresent()) {
			final IPAZType pazType = pazTypeOpt.get();
			if (this.tmpType != pazType) {
				this.tmpType = pazType;
				final List<Pair<Ingredient, Slot>> recipe = new ArrayList<>();
				pazType.getEnjoyCard().ifPresent(item ->
						recipe.add(Pair.of(Ingredient.of(item), this.menu.getSlot(1))));
				recipe.add(Pair.of(Ingredient.of(pazType.getEssence().getEssenceItem()), this.menu.getSlot(2)));
				pazType.getRank().getMaterial().ifPresent(tag -> {
					if (this.minecraft.level == null) return;
					final HolderSet<Item> holders = this.minecraft.level.registryAccess()
							.lookupOrThrow(Registries.ITEM)
							.getOrThrow(tag);
					recipe.add(Pair.of(Ingredient.of(holders), this.menu.getSlot(3)));
				});
				this.recipeManager.setRecipe(recipe);   // setRecipe 内部 clear
			}
		} else {
			this.tmpType = null;
			this.recipeManager.clear();
		}

		this.recipeManager.extract(this.minecraft, graphics, this.leftPos, this.topPos, a);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		// 本帧重新算，避免 extractContents 之后 handler 被包更新导致列表缩水
		final List<ISkillType> skills = this.menu.getAvailableSkills();
		final int count = Math.min(skills.size(), MAX_ENTRY_COUNT);
		final int pos = Mth.clamp(this.currentPos, 0, Math.max(0, skills.size() - MAX_ENTRY_COUNT));

		for (int i = 0; i < count; ++i) {
			if (this.buttons[i].visible && this.buttons[i].isHovered()) {
				final int idx = pos + i;
				if (idx < 0 || idx >= skills.size()) continue;
				final ISkillType type = skills.get(idx);
				graphics.setComponentTooltipForNextFrame(this.font,
						List.of(type.getDescription()), mouseX, mouseY);
				return;
			}
		}

		this.recipeManager.extractGhostRecipeTooltip(this.minecraft, graphics,
				this.leftPos, this.topPos, mouseX, mouseY);
		super.extractTooltip(graphics, mouseX, mouseY);
	}

	private void extractEntry(GuiGraphicsExtractor graphics, int buttonPos, ISkillType type, int lvl) {
		this.menu.getPAZType().ifPresent(pazType -> {
			final int x = this.buttons[buttonPos].getX();
			final int y = this.buttons[buttonPos].getY();

			this.extractLogo(graphics, pazType.getRank(), x + 2, y + 2);

			StringUtil.drawCenteredScaledString(graphics, this.minecraft.font,
					String.valueOf(type.getCostAt(lvl)), x + 13, y + 12, Colors.WHITE, 0.6F);
			StringUtil.drawScaledString(graphics, this.minecraft.font,
					type.getText().append(StringUtil.getRomanString(lvl + 1)).getString(),
					x + 18, y + 5, Colors.WHITE, 1F);
		});
	}

	private void extractLogo(GuiGraphicsExtractor graphics, IRankType rank, int posX, int posY) {
		int x = 239, y = 74;
		if (rank == RankTypes.GRAY) { x = 174; y = 58; }
		else if (rank == RankTypes.WHITE) { x = 190; y = 58; }
		else if (rank == RankTypes.GREEN) { x = 206; y = 58; }
		else if (rank == RankTypes.BLUE) { x = 222; y = 58; }
		else if (rank == RankTypes.PURPLE) { x = 174; y = 74; }
		else if (rank == RankTypes.GOLD) { x = 190; y = 74; }
		else if (rank == RankTypes.RED) { x = 206; y = 74; }
		else if (rank == RankTypes.BLACK) { x = 222; y = 74; }
		graphics.blit(RenderPipelines.GUI_TEXTURED, StringUtil.WIDGETS,
				posX, posY, x, y, 11, 10, 256, 256);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (this.menu.getAvailableSkills().size() > MAX_ENTRY_COUNT) {
			final int next = (int) ((double) this.currentPos - scrollY);
			this.currentPos = Mth.clamp(next, 0, this.menu.getAvailableSkills().size() - MAX_ENTRY_COUNT);
		}
		return true;
	}

	private static class EssenceButton extends PvZButton {
		public EssenceButton(int x, int y, Button.OnPress onPress) {
			super(StringUtil.WIDGETS, x, y, 82, 19, onPress);
		}

		@Override
		protected Pair<Integer, Integer> getButtonUV() {
			return Pair.of(174, 0);
		}

		@Override
		protected Pair<Integer, Integer> getButtonUVOffset() {
			return Pair.of(0, 38);
		}
	}
}