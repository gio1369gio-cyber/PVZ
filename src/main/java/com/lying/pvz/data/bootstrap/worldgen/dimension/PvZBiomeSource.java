package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import java.util.List;

/**
 * PVZ 专用维度的多噪声群系源：用温度/湿度把四个主题群系划成四个象限。
 *
 * <p>26.3 的 MultiNoiseBiomeSourceParameterList.Preset.CODEC 只认 nether/overworld，
 * 无法注册自定义 preset，因此直接用 {@link MultiNoiseBiomeSource#createFromList}，
 * 把参数列表编码进 LevelStem 的 biomes 字段。
 */
public final class PvZBiomeSource {
    private static final Climate.Parameter ALL    = Climate.Parameter.span(-1.0F, 1.0F);
    private static final Climate.Parameter HOT   = Climate.Parameter.span(0.0F, 1.0F);
    private static final Climate.Parameter COLD  = Climate.Parameter.span(-1.0F, 0.0F);
    private static final Climate.Parameter DRY   = Climate.Parameter.span(-1.0F, 0.0F);
    private static final Climate.Parameter WET   = Climate.Parameter.span(0.0F, 1.0F);

    private PvZBiomeSource() {}

    public static MultiNoiseBiomeSource create(HolderGetter<Biome> biomes) {
        Holder<Biome> egypt  = biomes.getOrThrow(PvZBiome.ANCIENT_EGYPT);
        Holder<Biome> harbor = biomes.getOrThrow(PvZBiome.PIRATE_HARBOR);
        Holder<Biome> west   = biomes.getOrThrow(PvZBiome.WILD_WEST);
        Holder<Biome> kungfu = biomes.getOrThrow(PvZBiome.KUNG_FU_WORLD);

        Climate.ParameterList<Holder<Biome>> parameters = new Climate.ParameterList<>(List.of(
                // 干热 → 远古埃及
                Pair.of(point(HOT, DRY), egypt),
                // 干冷 → 狂野西部
                Pair.of(point(COLD, DRY), west),
                // 湿热 → 海盗港湾
                Pair.of(point(HOT, WET), harbor),
                // 湿冷 → 功夫世界
                Pair.of(point(COLD, WET), kungfu)
        ));

        return MultiNoiseBiomeSource.createFromList(parameters);
    }

    private static Climate.ParameterPoint point(Climate.Parameter temperature, Climate.Parameter humidity) {
        // continentalness / erosion / depth / weirdness 全开，只靠 temp + humidity 分四区
        return Climate.parameters(temperature, humidity, ALL, ALL, ALL, ALL, 0.0F);
    }
}
