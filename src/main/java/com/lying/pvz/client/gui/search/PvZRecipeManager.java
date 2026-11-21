package com.lying.pvz.client.gui.search;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 配方幽灵图标管理器。
 *
 * <p>每个 {@link RecipeIngredient} 可以绑定一个真实 {@link Slot}。绑定后：
 * 槽内已有物品 → 视为已满足，不画幽灵、不给 tooltip；槽为空 → 正常画幽灵。
 */
public class PvZRecipeManager {

	private final List<RecipeIngredient> ingredients = Lists.newArrayList();
	private float time;

	private static boolean isControlDown() {
		return InputConstants.isKeyDown(InputConstants.KEY_LCONTROL)
				|| InputConstants.isKeyDown(InputConstants.KEY_RCONTROL);
	}

	public void extract(Minecraft mc, GuiGraphicsExtractor graphics,
						int guiLeft, int guiTop, float partialTicks) {
		if (!isControlDown()) {
			this.time += partialTicks;
		}
		for (int i = 0; i < this.ingredients.size(); ++i) {
			RecipeIngredient ingredient = this.ingredients.get(i);
			if (ingredient.isFilled()) continue;

			int x = ingredient.getX() + guiLeft;
			int y = ingredient.getY() + guiTop;

			graphics.fill(x, y, x + 16, y + 16, 822018048);

			ItemStack itemstack = ingredient.getItem();
			graphics.item(itemstack, x, y);
			graphics.fill(x, y, x + 16, y + 16, 822083583);

			if (i == 0) {
				graphics.itemDecorations(mc.font, itemstack, x, y);
			}
		}
	}

	public void extractGhostRecipeTooltip(Minecraft mc, GuiGraphicsExtractor graphics,
										  int guiLeft, int guiTop, int mouseX, int mouseY) {
		ItemStack itemstack = null;
		for (int i = 0; i < this.size(); ++i) {
			RecipeIngredient ingredient = this.get(i);
			if (ingredient.isFilled()) continue;

			int x = ingredient.getX() + guiLeft;
			int y = ingredient.getY() + guiTop;
			if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
				itemstack = ingredient.getItem();
			}
		}
		if (itemstack != null) {
			graphics.setTooltipForNextFrame(
					mc.font,
					Screen.getTooltipFromItem(mc, itemstack),
					itemstack.getTooltipImage(),
					itemstack,
					mouseX, mouseY);
		}
	}

	public void clear() {
		this.ingredients.clear();
		this.time = 0.0F;
	}

	/** 先清空再写入，避免多次 setRecipe 累积旧条目。 */
	public void setRecipe(List<Pair<Ingredient, Slot>> list) {
		this.clear();
		list.forEach(pair -> this.addIngredient(pair.getFirst(), pair.getSecond()));
	}

	public void addIngredient(Ingredient i, Slot slot) {
		this.ingredients.add(new RecipeIngredient(i, slot.x, slot.y, slot));
	}

	/** 无 Slot 的旧调用方式：永远画幽灵。 */
	public void addIngredient(Ingredient i, int x, int y) {
		this.ingredients.add(new RecipeIngredient(i, x, y, null));
	}

	public RecipeIngredient get(int pos) { return this.ingredients.get(pos); }
	public int size() { return this.ingredients.size(); }

	/**
	 * 取 Ingredient 的展示用 ItemStack 列表。
	 *
	 * <p>替代已弃用的 {@link Ingredient#items()}：
	 * <ul>
	 *   <li>非 custom：走 {@link Ingredient#getValues()} 的 {@code HolderSet} 流；</li>
	 *   <li>custom：走 {@link ICustomIngredient#items()}，避免触发
	 *       {@code getValues()} 的 {@code IllegalStateException}。</li>
	 * </ul>
	 */
	private static List<ItemStack> toDisplayStacks(Ingredient ingredient) {
		if (ingredient.isCustom()) {
			final ICustomIngredient custom = ingredient.getCustomIngredient();
			if (custom == null) return List.of();
			return custom.items()
					.map(holder -> holder.value().getDefaultInstance())
					.toList();
		}
		return ingredient.getValues().stream()
				.map(holder -> holder.value().getDefaultInstance())
				.toList();
	}

	public class RecipeIngredient {
		private final Ingredient ingredient;
		private final int x;
		private final int y;
		private final @Nullable Slot slot;
		private final List<ItemStack> displayStacks;

		public RecipeIngredient(Ingredient ingredient, int x, int y, @Nullable Slot slot) {
			this.ingredient = ingredient;
			this.x = x;
			this.y = y;
			this.slot = slot;
			this.displayStacks = toDisplayStacks(ingredient);
		}

		/** 绑定真实槽位且槽内已有物品 → 视为已填充，跳过幽灵。 */
		public boolean isFilled() {
			return this.slot != null && this.slot.hasItem();
		}

		public int getX() { return this.x; }
		public int getY() { return this.y; }

		public ItemStack getItem() {
			if (this.displayStacks.isEmpty()) {
				return ItemStack.EMPTY;
			}
			return this.displayStacks.get(
					Mth.floor(PvZRecipeManager.this.time / 30.0F) % this.displayStacks.size());
		}
	}
}