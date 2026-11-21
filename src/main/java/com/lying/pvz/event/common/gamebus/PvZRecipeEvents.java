package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.common.misc.recipe.FragmentRecipe;
import com.lying.pvz.common.misc.recipe.FusionRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.ArrayList;
import java.util.List;


@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZRecipeEvents {
    // 游戏事件总线
    @SubscribeEvent
    public static void datapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(PvZRecipeRegister.FUSION_RECIPE_TYPE.get());
        event.sendRecipes(PvZRecipeRegister.FRAGMENT_RECIPE_TYPE.get());
    }

    // 仅物理客户端
    private static final List<RecipeHolder<FusionRecipe>> CLIENT_FUSION_RECIPES = new ArrayList<>();
    private static final List<RecipeHolder<FragmentRecipe>> CLIENT_FRAGMENT_RECIPES = new ArrayList<>();

    public static List<RecipeHolder<FragmentRecipe>> getFragmentRecipes() { return CLIENT_FRAGMENT_RECIPES; }
    public static List<RecipeHolder<FusionRecipe>> getFusionRecipes()   { return CLIENT_FUSION_RECIPES; }


    @SubscribeEvent
    public static void recipesReceived(RecipesReceivedEvent event) {
        CLIENT_FUSION_RECIPES.clear();
        event.getRecipeMap().byType(PvZRecipeRegister.FUSION_RECIPE_TYPE.get()).forEach(h -> {
            if (h.value() instanceof FusionRecipe f) {
                CLIENT_FUSION_RECIPES.add(new RecipeHolder<>(h.id(), f));
            }
        });
        CLIENT_FRAGMENT_RECIPES.clear();
        event.getRecipeMap().byType(PvZRecipeRegister.FRAGMENT_RECIPE_TYPE.get()).forEach(h -> {
            if (h.value() instanceof FragmentRecipe f) {
                CLIENT_FRAGMENT_RECIPES.add(new RecipeHolder<>(h.id(), f));
            }
        });


    }

    @SubscribeEvent
    public static void clientLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        CLIENT_FRAGMENT_RECIPES.clear();
        CLIENT_FUSION_RECIPES.clear();
    }


}
