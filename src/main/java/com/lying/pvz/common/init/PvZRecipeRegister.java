package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.misc.recipe.FragmentRecipe;
import com.lying.pvz.common.misc.recipe.FusionRecipe;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class PvZRecipeRegister {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, PvZMod.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, PvZMod.MOD_ID);

    // ---------- Recipe Types ----------
    public static final DeferredHolder<RecipeType<?>, RecipeType<CraftingRecipe>> FUSION_RECIPE_TYPE =
            RECIPE_TYPES.register("card_fusion",
                    () -> RecipeType.simple(StringUtil.id("card_fusion")));

    public static final DeferredHolder<RecipeType<?>, RecipeType<CraftingRecipe>> FRAGMENT_RECIPE_TYPE =
            RECIPE_TYPES.register("fragment_splice",
                    () -> RecipeType.simple(StringUtil.id("fragment_splice")));

    // ---------- Serializers ----------
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FusionRecipe>> FUSION_SERIALIZER =
            RECIPE_SERIALIZERS.register("card_fusion", () -> FusionRecipe.SERIALIZER);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FragmentRecipe>> FRAGMENT_SERIALIZER =
            RECIPE_SERIALIZERS.register("fragment_splice", () -> FragmentRecipe.SERIALIZER);

}