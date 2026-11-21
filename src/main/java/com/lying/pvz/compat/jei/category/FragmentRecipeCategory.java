package com.lying.pvz.compat.jei.category;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.misc.recipe.FragmentRecipe;
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

import java.util.Optional;

public class FragmentRecipeCategory implements IRecipeCategory<FragmentRecipe> {

    private final IDrawable iconDraw;
    private final IDrawable arrowDraw;

    public static final IRecipeType<FragmentRecipe> TYPE =
            IRecipeType.create("pvz", "fragment_splice", FragmentRecipe.class);

    public FragmentRecipeCategory(IGuiHelper helper) {
        this.arrowDraw = helper.drawableBuilder(StringUtil.WIDGETS, 44, 64, 22, 15).build();
        this.iconDraw = helper.createDrawableItemStack(
                new ItemStack(PvZBlockRegister.FRAGMENT_SPLICE.get())
        );
    }

    @Override public int getWidth()  { return 176; }
    @Override public int getHeight() { return 116; }

    @Override
    public IRecipeType<FragmentRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.pvz.fragment_splice");
    }

    @Override
    public IDrawable getIcon() {
        return this.iconDraw;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FragmentRecipe recipe, IFocusGroup focuses) {
        var items = recipe.getRecipeItems();

        // 5x5 输入格：空槽跳过（Optional.empty()）
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int slotId = i * 5 + j;
                var slot = builder.addInputSlot(i * 20 + 10, j * 20 + 10)
                        .setStandardSlotBackground();
                Optional<Ingredient> ingredient = items.get(slotId);
                ingredient.ifPresent(slot::add);
            }
        }

        // 输出格：ItemStackTemplate 直接交给 add(...)
        builder.addOutputSlot(150, 50)
                .add(recipe.getResult())
                .setOutputSlotBackground();
    }

    @Override
    public void draw(FragmentRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.arrowDraw.draw(graphics, 110, 52);
    }
}