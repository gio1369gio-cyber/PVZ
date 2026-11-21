package com.lying.pvz.mixin;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.utils.ConfigUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {

    @Inject(method = "addBiomes(Ljava/util/function/Consumer;)V", at = @At("TAIL"), expect = 1)
    private void pvz$addCustomBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> out, CallbackInfo ci) {

        if (isVanillaRegistryLookup()) {
            return;
        }

        if (!ConfigUtil.get(PvZConfig.LOCAL_CONFIG.WorldSettings.EnableBiome)) {
            return;
        }

        addSlice(out, PvZBiome.ZEN_GARDEN,
                Climate.Parameter.span(-0.35F, 0.35F),
                Climate.Parameter.span(-0.35F, 0.35F),
                Climate.Parameter.span(-0.3F, 0.3F),
                Climate.Parameter.span(-0.35F, 0.35F), -0.8F, 0.8F);
    }
    private static void addSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> c, ResourceKey<Biome> biome,
                                 Climate.Parameter temp, Climate.Parameter hum, Climate.Parameter cont, Climate.Parameter ero,
                                 float wepMin, float wepMax) {
        for (float d : new float[]{0.0F, 1.0F}) {
            c.accept(Pair.of(
                    Climate.parameters(temp, hum, cont, ero,
                            Climate.Parameter.point(d),
                            Climate.Parameter.span(wepMin, wepMax), 0.0F),
                    biome));
        }
    }

    private static boolean isVanillaRegistryLookup() {
        return StackWalker.getInstance().walk(frames -> frames.anyMatch(frame -> {
            String cn = frame.getClassName();
            return cn.equals("net.minecraft.data.registries.VanillaRegistries")
                    || cn.equals("net.neoforged.fml.loading.DatagenModLoader")
                    || cn.equals("net.minecraft.data.Main")
                    || cn.equals("net.minecraft.client.data.Main")
                    || cn.contains("RegistryPatchGenerator")
                    || cn.contains("DatapackBuiltinEntriesProvider");
        }));
    }
}

