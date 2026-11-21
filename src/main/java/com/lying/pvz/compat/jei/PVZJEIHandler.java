package com.lying.pvz.compat.jei;

import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IJeiRuntime;


public class PVZJEIHandler {

    private static IJeiRuntime jeiRuntime;

    public static void setRuntime(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    public static IRecipeManager getRecipeManager() {
        if (jeiRuntime == null) {
            throw new IllegalStateException("JEI runtime not available yet!");
        }
        return jeiRuntime.getRecipeManager();
    }
}