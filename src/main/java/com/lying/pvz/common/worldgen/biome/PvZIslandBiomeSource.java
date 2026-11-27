package com.lying.pvz.common.worldgen.biome;

import com.lying.pvz.common.worldgen.BiomeProgression;
import com.lying.pvz.utils.StringUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import java.util.stream.Stream;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDensityFunctions;

/**
 * PVZ 四岛维度的群系源：按方块坐标到四个岛心的水平距离分配群系，
 * 不依赖气候采样（末地式噪声路由的 temperature/vegetation 恒为 0，MultiNoiseBiomeSource 会失效）。
 *
 * <p>岛心与 {@link PvZDensityFunctions#ISLAND_CENTERS}、{@link BiomeProgression#ORDER} 索引一一对应：
 * 埃及(0,0)、海盗(500,0)、西部(0,500)、未来(-500,0)。某点归属距离最近的岛；
 * 岛外虚空区也归最近岛（那里没有方块，群系只影响环境色，不重要）。
 *
 * <p>同时提供 {@link #centerOf(ResourceKey)} 供 TimeKey 传送直接取固定落点，
 * 彻底删掉运行时 findBiomeHorizontal + 找陆地那套脆弱逻辑。
 */
public final class PvZIslandBiomeSource extends BiomeSource {

    public static final MapCodec<PvZIslandBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    net.minecraft.world.level.biome.Biome.LIST_CODEC
                            .fieldOf("biomes")
                            .forGetter(source -> source.biomes)
            ).apply(instance, PvZIslandBiomeSource::new));

    /** 四个群系，顺序与 ISLAND_CENTERS 对应。 */
    private final net.minecraft.core.HolderSet<Biome> biomes;

    public PvZIslandBiomeSource(net.minecraft.core.HolderSet<Biome> biomes) {
        this.biomes = biomes;
    }

    /** 从注册表上下文构造（datagen 用）。 */
    public static PvZIslandBiomeSource create(HolderGetter<Biome> biomes) {
        return new PvZIslandBiomeSource(net.minecraft.core.HolderSet.direct(
                biomes.getOrThrow(PvZBiome.ANCIENT_EGYPT),
                biomes.getOrThrow(PvZBiome.PIRATE_HARBOR),
                biomes.getOrThrow(PvZBiome.WILD_WEST),
                biomes.getOrThrow(PvZBiome.FUTURE_WORLD)));
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return biomes.stream();
    }

    @Override
    public BiomeResolver createResolver(Climate.Sampler sampler) {
        // quart 坐标 → block 坐标；按最近岛心选群系。X/Z 决定，Y 忽略。
        return (quartX, quartY, quartZ) -> {
            int x = QuartPos.toBlock(quartX);
            int z = QuartPos.toBlock(quartZ);
            return biomes.get(nearestIslandIndex(x, z));
        };
    }

    /** 返回离 (x,z) 最近的岛索引。 */
    public static int nearestIslandIndex(int x, int z) {
        int best = 0;
        long bestDistSq = Long.MAX_VALUE;
        var centers = PvZDensityFunctions.ISLAND_CENTERS;
        for (int i = 0; i < centers.size(); i++) {
            var c = centers.get(i);
            long dx = (long) x - c.getX();
            long dz = (long) z - c.getZ();
            long d2 = dx * dx + dz * dz;
            if (d2 < bestDistSq) {
                bestDistSq = d2;
                best = i;
            }
        }
        return best;
    }

    /** 群系 key → 岛心索引（用于传送落点）。 */
    public static int indexOfBiome(ResourceKey<Biome> key) {
        java.util.List<ResourceKey<Biome>> order = BiomeProgression.ORDER;
        return order.indexOf(key);
    }

    /** 群系 key 对应的岛心方块坐标（岛中心地表，Y 由噪声决定，这里给基准 Y=64）。 */
    public static net.minecraft.core.BlockPos centerOf(ResourceKey<Biome> key) {
        int idx = indexOfBiome(key);
        if (idx < 0) idx = 0;
        var c = PvZDensityFunctions.ISLAND_CENTERS.get(idx);
        return new net.minecraft.core.BlockPos(c.getX(), 64, c.getZ());
    }
}
