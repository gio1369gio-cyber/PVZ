package com.lying.pvz.data.provider.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lying.pvz.common.misc.recipe.FragmentRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
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
import java.util.Set;

public class FragmentRecipeBuilder implements RecipeBuilder {

    private final HolderGetter<Item> items;
    private final HolderGetter<Recipe<?>> recipes;
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public FragmentRecipeBuilder(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result, int count) {
        this.items = items;
        this.recipes = recipes;
        this.result = result;
        this.count = count;
    }

    public static FragmentRecipeBuilder shaped(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result) {
        return shaped(items, recipes, result, 1);
    }

    public static FragmentRecipeBuilder shaped(HolderGetter<Item> items, HolderGetter<Recipe<?>> recipes, Item result, int count) {
        return new FragmentRecipeBuilder(items, recipes, result, count);
    }

    public FragmentRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(this.items.getOrThrow(tag)));
    }

    public FragmentRecipeBuilder define(Character symbol, Item item) {
        return this.define(symbol, Ingredient.of(item));
    }

    public FragmentRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        }
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(symbol, ingredient);
        return this;
    }

    public FragmentRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.rows.add(pattern);
        return this;
    }

    @Override
    public FragmentRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public FragmentRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    @Override
    public @Nullable ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(new ItemStackTemplate(this.result, this.count));
    }


    private void ensureValid(ResourceKey<Recipe<?>> id) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        }

        Set<Character> unused = Sets.newHashSet(this.key.keySet());
        unused.remove(' ');

        for (String row : this.rows) {
            for (int i = 0; i < row.length(); i++) {
                char c = row.charAt(i);
                if (!this.key.containsKey(c) && c != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c + "'");
                }
                unused.remove(c);
            }
        }

        if (!unused.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        }
        if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
            throw new IllegalStateException("Shaped recipe " + id
                    + " only takes in a single item - should it be a shapeless recipe instead?");
        }
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    // ---------- save ----------

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.ensureValid(id);

        Advancement.Builder advancement = Advancement.Builder.recipeAdvancement()
                .addCriterion("has_the_recipe",
                        RecipeUnlockedTrigger.unlocked(this.recipes.getOrThrow(id)))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        Map<String, Ingredient> stringKey = Maps.newLinkedHashMap();
        this.key.forEach((symbol, ingredient) -> stringKey.put(String.valueOf(symbol), ingredient));
        FragmentRecipe recipe = new FragmentRecipe(
                this.group == null ? "" : this.group,
                this.rows,
                stringKey,
                new  ItemStackTemplate(this.result, this.count),
                true
        );
        Identifier advancementId = id.identifier().withPrefix("recipes/");
        output.accept(id, recipe, advancement.build(advancementId));
    }
}