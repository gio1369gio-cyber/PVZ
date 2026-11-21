package com.lying.pvz.compat.jei;


import com.lying.pvz.PvZMod;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.gui.screen.CardFusionScreen;
import com.lying.pvz.client.gui.screen.FragmentSpliceScreen;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.menu.CardFusionMenu;
import com.lying.pvz.common.menu.FragmentSpliceMenu;
import com.lying.pvz.common.misc.recipe.FragmentRecipe;
import com.lying.pvz.common.misc.recipe.FusionRecipe;
import com.lying.pvz.compat.jei.category.FragmentRecipeCategory;
import com.lying.pvz.compat.jei.category.FusionRecipeCategory;
import com.lying.pvz.event.common.gamebus.PvZRecipeEvents;
import com.lying.pvz.utils.StringUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@JeiPlugin
public class PVZJEIPlugin implements IModPlugin {

    public static final Identifier ID = StringUtil.id("jei");

    private static final IRecipeType<FragmentRecipe> FRAGMENT_JEI_TYPE =
            FragmentRecipeCategory.TYPE;
    private static final IRecipeType<FusionRecipe> FUSION_JEI_TYPE =
            FusionRecipeCategory.TYPE;

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FragmentRecipe> fragmentRecipes = PvZRecipeEvents.getFragmentRecipes().stream()
                .map(RecipeHolder::value)
                .filter(obj -> true)
                .map(FragmentRecipe.class::cast)
                .toList();

        List<FusionRecipe> fusionRecipes = PvZRecipeEvents.getFusionRecipes().stream()
                .map(RecipeHolder::value)
                .filter(obj -> true)
                .map(FusionRecipe.class::cast)
                .toList();

       PvZMod.LOGGER.info("[PVZ JEI] fragment={}, fusion={}", fragmentRecipes.size(), fusionRecipes.size());

        if (!fragmentRecipes.isEmpty()) registration.addRecipes(FRAGMENT_JEI_TYPE, fragmentRecipes);
        if (!fusionRecipes.isEmpty())   registration.addRecipes(FUSION_JEI_TYPE, fusionRecipes);
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new FragmentRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new FusionRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration re) {

        re.addCraftingStation(FRAGMENT_JEI_TYPE, PvZBlockRegister.FRAGMENT_SPLICE.get());
        re.addCraftingStation(FUSION_JEI_TYPE, PvZBlockRegister.CARD_FUSION_TABLE.get());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(
                CardFusionScreen.class,
                83, 80, 11, 15,
                FUSION_JEI_TYPE
        );

        registration.addRecipeClickArea(
                FragmentSpliceScreen.class,
                156, 62, 23, 15,
                FRAGMENT_JEI_TYPE
        );
    }


    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

        registration.addRecipeTransferHandler(
                FragmentSpliceMenu.class,
                PvZMenuRegister.FRAGMENT_SPLICE.get(),
                FRAGMENT_JEI_TYPE,
                2, 25, 27, 36
        );
        registration.addRecipeTransferHandler(
                CardFusionMenu.class,
                PvZMenuRegister.CARD_FUSION.get(),
                FUSION_JEI_TYPE,
                3, 9, 12, 36
        );
    }





    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        PVZJEIHandler.setRuntime(jeiRuntime);
    }

    @Override
    public Identifier getPluginUid() {
        return ID;
    }
}