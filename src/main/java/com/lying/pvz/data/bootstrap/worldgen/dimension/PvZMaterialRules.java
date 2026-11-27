package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.material.OverworldMaterialRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

/**
 * PVZ 维度地表规则。
 *
 * <p>26.3 地表规则挂在维度级 {@code NoiseGeneratorSettings.materialRule}，
 * 用 {@link MaterialRules#isBiome} + {@link MaterialRules#stoneDepthCheck} 分支，
 * 只在四个 PVZ 群系的<strong>最表层</strong>铺主题方块，地下与其余群系全部兜底原版地表规则。
 *
 * <p>方块选择：
 * <ul>
 *   <li>远古埃及：砂岩（沙漠文明）；</li>
 *   <li>海盗港湾：沙子（海滩）；</li>
 *   <li>狂野西部：红沙（荒漠）；</li>
 *   <li>未来世界：铁块（科技金属）。</li>
 * </ul>
 */
public final class PvZMaterialRules {

    public static final ResourceKey<MaterialRule> PVZ_SURFACE =
            ResourceKey.create(Registries.MATERIAL_RULE, StringUtil.id("pvz_surface"));

    private PvZMaterialRules() {}

    public static void bootstrap(BootstrapContext<MaterialRule> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<MaterialRule> rules = context.lookup(Registries.MATERIAL_RULE);

        // 原版地表兜底（非 PVZ 群系 / 非表层 全部走它）
        MaterialRule vanilla = MaterialRules.getRule(rules, OverworldMaterialRules.OVERWORLD);

        // 最表层判定：地表第一层（FLOOR 顶面）
        MaterialRule egypt = surfaceOverBiome(biomes, PvZBiome.ANCIENT_EGYPT,
                Blocks.SANDSTONE.defaultBlockState());
        MaterialRule harbor = surfaceOverBiome(biomes, PvZBiome.PIRATE_HARBOR,
                Blocks.SAND.defaultBlockState());
        MaterialRule west = surfaceOverBiome(biomes, PvZBiome.WILD_WEST,
                Blocks.RED_SAND.defaultBlockState());
        MaterialRule future = surfaceOverBiome(biomes, PvZBiome.FUTURE_WORLD,
                Blocks.IRON_BLOCK.defaultBlockState());

        MaterialRule rule = MaterialRules.sequence(egypt, harbor, west, future, vanilla);
        context.register(PVZ_SURFACE, rule);
    }

    /** 仅在指定群系的地表第一层铺方块；条件不满足时返回 null 让 sequence 继续。 */
    private static MaterialRule surfaceOverBiome(HolderGetter<Biome> biomes,
                                                 ResourceKey<Biome> biome,
                                                 net.minecraft.world.level.block.state.BlockState block) {
        // 嵌套 ifTrue 实现「群系 && 表层」双条件：任一不满足则跳过，sequence 继续
        return MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, biome),
                MaterialRules.ifTrue(
                        MaterialRules.stoneDepthCheck(0, true, CaveSurface.FLOOR),
                        MaterialRules.state(block)));
    }
}
