package com.lying.pvz.common.misc.recipe;


import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.utils.StringUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class FusionRecipe implements CraftingRecipe {

    public static final Identifier UID = StringUtil.id("card_fusion");

    private final String group;
    private final ItemStackTemplate result;
    private final NonNullList<Ingredient> ingredients;
    /** 保留字段供 datagen / 外部判定使用；matches 已不依赖它。 */
    private final boolean isSimple;
    private final boolean showNotification;

    public FusionRecipe(String group, ItemStackTemplate result, NonNullList<Ingredient> ingredients) {
        this(group, result, ingredients, true);
    }

    public FusionRecipe(String group, ItemStackTemplate result,
                        NonNullList<Ingredient> ingredients, boolean showNotification) {
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.showNotification = showNotification;
    }

    // ---------- Recipe / CraftingRecipe ----------

    @Override
    public boolean matches(CraftingInput input, Level level) {
        // 非空槽数量必须与配方 ingredient 数量一致
        if (input.ingredientCount() != this.ingredients.size()) {
            return false;
        }

        List<ItemStack> nonEmpty = new ArrayList<>(input.ingredientCount());
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                nonEmpty.add(stack);
            }
        }
        // 无序匹配：允许任意排列
        return RecipeMatcher.findMatches(nonEmpty, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return this.result.create();
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    /**
     * 无序配方：若 ingredients 全部非空，交给 PlacementInfo 让配方书自动摆料；
     * 若 ingredients 中混有 Optional 空槽，则返回 NOT_PLACEABLE。
     * 本配方没有空槽概念（ingredients 全为具体 Ingredient），正常路径走 create。
     */
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return PvZRecipeRegister.FUSION_RECIPE_TYPE.get();
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeSerializer<FusionRecipe> getSerializer() {
        return PvZRecipeRegister.FUSION_SERIALIZER.get();
    }

    // ---------- codec / datagen 用 getter ----------

    public NonNullList<Ingredient> getIngredients() { return this.ingredients; }
    public ItemStackTemplate getResult() { return this.result; }
    public boolean isSimple() { return this.isSimple; }

    // ---------- Serializer（1.21.5：RecipeSerializer 是 record，用 new） ----------

    public static final MapCodec<FusionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.optionalFieldOf("group", "")
                    .forGetter(FusionRecipe::group),
            ItemStackTemplate.CODEC.fieldOf("result")
                    .forGetter(r -> r.result),
            Ingredient.CODEC.listOf().fieldOf("ingredients")
                    .forGetter(FusionRecipe::getIngredients),
            Codec.BOOL.optionalFieldOf("show_notification", true)
                    .forGetter(FusionRecipe::showNotification)
    ).apply(inst, (group, result, ingredients, show) ->
            new FusionRecipe(group, result, NonNullList.copyOf(ingredients), show)));

    public static final StreamCodec<RegistryFriendlyByteBuf, FusionRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, FusionRecipe::group,
                    ItemStackTemplate.STREAM_CODEC, r -> r.result,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    r -> r.ingredients,
                    ByteBufCodecs.BOOL, FusionRecipe::showNotification,
                    (group, result, ingredients, show) ->
                            new FusionRecipe(group, result, NonNullList.copyOf(ingredients), show)
            );

    public static final RecipeSerializer<FusionRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);
}