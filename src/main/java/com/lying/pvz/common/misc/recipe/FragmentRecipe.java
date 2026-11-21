package com.lying.pvz.common.misc.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
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
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import com.google.gson.JsonSyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 5×5 滑动窗口 + 镜像匹配配方，按CraftingRecipe 标准实现。
 */
public class FragmentRecipe implements CraftingRecipe {

    public static final Identifier UID = StringUtil.id("fragment_splice");
    private static final int MAX_WIDTH = 5;
    private static final int MAX_HEIGHT = 5;

    private final String group;
    private final List<String> pattern;
    private final Map<String, Ingredient> key;
    private final ItemStackTemplate result;
    private final int width;
    private final int height;
    private final NonNullList<Optional<Ingredient>> recipeItems;
    private final boolean showNotification;

    // ==================== 构造 ====================


    public FragmentRecipe(String group, List<String> pattern,
                          Map<String, Ingredient> key, ItemStackTemplate result,
                          boolean showNotification) {
        this.group = group;
        this.pattern = List.copyOf(pattern);
        this.key = key;
        this.result = result;
        this.showNotification = showNotification;

        String[] raw = pattern.toArray(new String[0]);
        validatePattern(raw);
        String[] shrunk = shrink(raw);
        if (shrunk.length == 0) {
            throw new JsonSyntaxException("Pattern is empty after trimming whitespace");
        }
        this.width = shrunk[0].length();
        this.height = shrunk.length;
        this.recipeItems = dissolvePattern(shrunk, key, this.width, this.height);
    }

    /** 网络用。客户端不保存原始 pattern / key。 */
    public FragmentRecipe(String group, ItemStackTemplate result,
                          NonNullList<Optional<Ingredient>> recipeItems,
                          int width, int height, boolean showNotification) {
        this.group = group;
        this.pattern = List.of();
        this.key = Map.of();
        this.result = result;
        this.recipeItems = recipeItems;
        this.width = width;
        this.height = height;
        this.showNotification = showNotification;
    }

    // ==================== Recipe / CraftingRecipe ====================

    @Override
    public boolean matches(CraftingInput input, Level level) {
        for (int i = 0; i <= input.width() - this.width; i++) {
            for (int j = 0; j <= input.height() - this.height; j++) {
                if (this.matches(input, i, j, true))  return true;
                if (this.matches(input, i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean matches(CraftingInput input, int offsetX, int offsetY, boolean mirrored) {
        for (int x = 0; x < input.width(); x++) {
            for (int y = 0; y < input.height(); y++) {
                int k = x - offsetX;
                int l = y - offsetY;

                Optional<Ingredient> ingredient = Optional.empty();
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    ingredient = mirrored
                            ? this.recipeItems.get(this.width - k - 1 + l * this.width)
                            : this.recipeItems.get(k + l * this.width);
                }
                if (!Ingredient.testOptionalIngredient(ingredient, input.getItem(x, y))) {
                    return false;
                }
            }
        }
        return true;
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

    /** 5×5 无法在配方书自动摆料。 */
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return PvZRecipeRegister.FRAGMENT_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<FragmentRecipe> getSerializer() {
        return PvZRecipeRegister.FRAGMENT_SERIALIZER.get();
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    // ==================== codec 用 getter ====================

    public List<String> getPattern() { return this.pattern; }
    public Map<String, Ingredient> getKey() { return this.key; }
    public ItemStackTemplate getResult() { return this.result; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public NonNullList<Optional<Ingredient>> getRecipeItems() { return this.recipeItems; }

    // ==================== 校验 / shrink / dissolve ====================

    private static void validatePattern(String[] pattern) {
        if (pattern.length > MAX_HEIGHT) {
            throw new JsonSyntaxException(
                    "Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        }
        if (pattern.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }
        for (int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            if (row.length() > MAX_WIDTH) {
                throw new JsonSyntaxException(
                        "Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
            }
            if (i > 0 && pattern[0].length() != row.length()) {
                throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            }
        }
    }

    @VisibleForTesting
    static String[] shrink(String... pattern) {
        int first = Integer.MAX_VALUE;
        int last = 0;
        int top = 0;
        int bottom = 0;

        for (int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            first = Math.min(first, firstNonSpace(row));
            int lastNon = lastNonSpace(row);
            last = Math.max(last, lastNon);
            if (lastNon < 0) {
                if (top == i) top++;
                bottom++;
            } else {
                bottom = 0;
            }
        }

        if (pattern.length == bottom) {
            return new String[0];
        }

        String[] result = new String[pattern.length - bottom - top];
        for (int i = 0; i < result.length; i++) {
            result[i] = pattern[i + top].substring(first, last + 1);
        }
        return result;
    }

    private static int firstNonSpace(String s) {
        int i;
        for (i = 0; i < s.length() && s.charAt(i) == ' '; i++) { }
        return i;
    }

    private static int lastNonSpace(String s) {
        int i;
        for (i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; i--) { }
        return i;
    }

    private static NonNullList<Optional<Ingredient>> dissolvePattern(
            String[] pattern, Map<String, Ingredient> key, int width, int height) {
        NonNullList<Optional<Ingredient>> ingredients =
                NonNullList.withSize(width * height, Optional.empty());
        Set<String> unused = Sets.newHashSet(key.keySet());
        unused.remove(" ");

        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length(); j++) {
                String symbol = pattern[i].substring(j, j + 1);
                if (" ".equals(symbol)) continue;

                Ingredient ingredient = key.get(symbol);
                if (ingredient == null) {
                    throw new JsonSyntaxException(
                            "Pattern references symbol '" + symbol
                                    + "' but it's not defined in the key");
                }
                unused.remove(symbol);
                ingredients.set(j + width * i, Optional.of(ingredient));
            }
        }

        if (!unused.isEmpty()) {
            throw new JsonSyntaxException(
                    "Key defines symbols that aren't used in pattern: " + unused);
        }
        return ingredients;
    }

    // ==================== Serializer ====================
    public static final MapCodec<FragmentRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.optionalFieldOf("group", "")
                    .forGetter(FragmentRecipe::group),
            Codec.STRING.listOf().fieldOf("pattern")
                    .forGetter(FragmentRecipe::getPattern),
            Codec.unboundedMap(Codec.STRING, Ingredient.CODEC)
                    .fieldOf("key")
                    .forGetter(FragmentRecipe::getKey),
            ItemStackTemplate.CODEC.fieldOf("result")
                    .forGetter(FragmentRecipe::getResult),
            Codec.BOOL.optionalFieldOf("show_notification", true)
                    .forGetter(FragmentRecipe::showNotification)
    ).apply(inst, FragmentRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FragmentRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,  FragmentRecipe::group,
                    ByteBufCodecs.VAR_INT,      FragmentRecipe::getWidth,
                    ByteBufCodecs.VAR_INT,      FragmentRecipe::getHeight,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    FragmentRecipe::getRecipeItems,
                    ItemStackTemplate.STREAM_CODEC,     FragmentRecipe::getResult,
                    ByteBufCodecs.BOOL,         FragmentRecipe::showNotification,
                    (group, width, height, items, result, show) ->
                            new FragmentRecipe(group, result, NonNullList.copyOf(items), width, height, show)
            );

    /** 1.21.5：RecipeSerializer 是 record，直接 new，不能 implements。 */
    public static final RecipeSerializer<FragmentRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);
}