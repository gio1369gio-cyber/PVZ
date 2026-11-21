package com.lying.pvz.compat.jei.category;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.misc.recipe.FusionRecipe;
import com.lying.pvz.utils.StringUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FusionRecipeCategory implements IRecipeCategory<FusionRecipe> {

    private final IDrawable iconDraw;
    private final IDrawable arrowDraw;

    public static final IRecipeType<FusionRecipe> TYPE =
            IRecipeType.create("pvz", "card_fusion", FusionRecipe.class);

    public FusionRecipeCategory(IGuiHelper helper) {
        this.arrowDraw = helper.drawableBuilder(StringUtil.WIDGETS, 44, 64, 22, 15).build();
        this.iconDraw = helper.createDrawableItemStack(
                new ItemStack(PvZBlockRegister.CARD_FUSION_TABLE.get())
        );
    }

    @Override
    public int getWidth() {
        return 166;
    }

    @Override
    public int getHeight() {
        return 96;
    }

    @Override
    public IRecipeType<FusionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.pvz.card_fusion_table");
    }

    @Override
    public IDrawable getIcon() {
        return this.iconDraw;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FusionRecipe recipe, IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();

        // 3×3 输入格：始终建 9 个槽位，仅前 ingredients.size() 个填充
        for (int i = 0; i < 9; i++) {
            int slotX = (i % 3) * 20 + 30;
            int slotY = (i / 3) * 20 + 30;

            var slot = builder.addInputSlot(slotX, slotY)
                    .setStandardSlotBackground();

            if (i < ingredients.size()) {
                Ingredient ing = ingredients.get(i);
                slot.add(ing);          // add(Ingredient)，非弃用
            }
        }

        // 输出格：ItemStackTemplate 直接交给 add(...)
        builder.addOutputSlot(140, 50)
                .setOutputSlotBackground()
                .add(recipe.getResult());
    }

    @Override
    public void draw(FusionRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.arrowDraw.draw(graphics, 105, 52);
    }
}