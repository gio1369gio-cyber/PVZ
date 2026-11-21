package com.lying.pvz.data.provider.recipe;

import com.google.common.collect.Lists;
import com.lying.pvz.common.misc.recipe.FusionRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FusionRecipeBuilder implements RecipeBuilder {

    private final HolderGetter<Item> items;
    private final HolderGetter<Recipe<?>> recipes;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public FusionRecipeBuilder(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result, int count) {
        this.items = items;
        this.recipes = recipes;
        this.result = result;
        this.count = count;
    }

    public static FusionRecipeBuilder shapeless(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result) {
        return new FusionRecipeBuilder(items, recipes, result, 1);
    }

    public static FusionRecipeBuilder shapeless(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result, int count) {
        return new FusionRecipeBuilder(items, recipes, result, count);
    }

    public FusionRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public FusionRecipeBuilder requires(Item item) {
        return this.requires(item, 1);
    }

    public FusionRecipeBuilder requires(Item item, int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }
        return this;
    }

    public FusionRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public FusionRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    @Override
    public FusionRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public FusionRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public @Nullable ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(new ItemStackTemplate(this.result, this.count));
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        Advancement.Builder advancement = Advancement.Builder.recipeAdvancement()
                .addCriterion("has_the_recipe",
                        RecipeUnlockedTrigger.unlocked(this.recipes.getOrThrow(id)))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        ItemStackTemplate resultStack = new ItemStackTemplate(this.result, this.count);
        FusionRecipe recipe = new FusionRecipe(
                this.group == null ? "" : this.group,
                resultStack,
                NonNullList.copyOf(this.ingredients)
        );

        output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/")));
    }
}