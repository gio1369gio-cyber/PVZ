package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;

import java.util.List;
import java.util.Optional;

/**
 * PVZ 维度噪声设置：四座实心倒圆锥浮岛 + 主题地表。
 *
 * <p>地形<strong>完全由 {@link PvZDensityFunctions#ISLANDS} 的密度场决定</strong>——
 * 实心倒圆锥、平顶（y=64）、锥尖（y=8），内部不加任何 3D 噪声或洞穴。
 * 不再使用末地的 sloped_cheese / base_3d_noise / squeeze / beardifier。
 *
 * <p>基底方块为普通石头（{@link Blocks#STONE}），海平面 0、无流体；
 * 地表最上层由 {@link PvZMaterialRules#PVZ_SURFACE} 按群系铺主题方块（砂岩/沙/红沙/铁）。
 *
 * <p>其余设置照抄末地：disableMobGeneration=true、spawnTarget=[]、
 * legacyRandomSource=true，noise={minY:0,height:128}。
 */
public final class PvZNoiseSettings {

    public static final ResourceKey<NoiseGeneratorSettings> PVZ_NOISE =
            ResourceKey.create(Registries.NOISE_SETTINGS, StringUtil.id("pvz_noise"));

    private PvZNoiseSettings() {}

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<MaterialRule> materialRules = context.lookup(Registries.MATERIAL_RULE);

        // bootstrap 阶段 Holder 尚未绑定，必须用 HolderHolder 包装延迟解引用，不能 .value()。
        DensityFunction islands = new DensityFunctions.HolderHolder(
                densityFunctions.getOrThrow(PvZDensityFunctions.ISLANDS));

        // 末地式 NoiseRouter：final_density 直接用实心倒锥密度；
        // erosion 仍吃岛掩码（群系源留作备用，当前 PvZIslandBiomeSource 按坐标分区不依赖它）。
        NoiseRouter router = new NoiseRouter(
                DensityFunctions.zero(),   // temperature
                DensityFunctions.zero(),   // vegetation
                DensityFunctions.zero(),   // continents
                islands,                   // erosion
                DensityFunctions.zero(),   // depth
                DensityFunctions.zero(),   // ridges
                DensityFunctions.zero(),   // chunk_surface_level
                islands);                  // final_density —— 实心倒圆锥

        NoiseGeneratorSettings pvz = new NoiseGeneratorSettings(
                net.minecraft.world.level.levelgen.NoiseSettings.create(0, 128),
                Blocks.STONE.defaultBlockState(),       // 岛体基底：普通石头
                Blocks.AIR.defaultBlockState(),         // 无流体
                router,
                materialRules.getOrThrow(PvZMaterialRules.PVZ_SURFACE),
                List.of(),
                0,
                true,                          // disableMobGeneration（末地同款）
                Optional.empty(),              // aquifers：浮岛不需要含水层
                true,                          // legacyRandomSource（末地同款）
                NoiseGeneratorSettings.DebugFunctions.EMPTY);

        context.register(PVZ_NOISE, pvz);
    }
}
