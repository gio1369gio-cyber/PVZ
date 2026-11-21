package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.*;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;

import java.util.Optional;

public class PvZDimensionType {
    public  static final ResourceKey<DimensionType> PVZ_2 = createKey("pvz_2");

    public static ResourceKey<DimensionType> createKey(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<DimensionType> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
        HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);

        // 维度层是常量基准，群系层会按位置覆盖（EnvironmentAttributeSystem.addStaticLayers）。
        // 主题色由四个 PVZ 群系各自的 setAttribute 决定；这里只给群系未覆盖时的回退值。
        // 已移除原下界模板残留：NETHER_PORTAL_SPAWNS_PIGLINS=true、AMBIENT_SOUNDS=LEGACY_CAVE_SETTINGS，
        // 以及与主世界默认值重复的 BED_RULE / STRAW_BED_RULE / RESPAWN_ANCHOR_WORKS。
        EnvironmentAttributeMap pvzAttributes = EnvironmentAttributeMap.builder()
                .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(OverworldBiomes.calculateSkyColor(0.8F)))
                .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0xC0D8E8))
                .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ARGB.vector3fFromRGB24(0xB8C8D8))
                .set(EnvironmentAttributes.CLOUD_COLOR, ARGB.vector4fFromARGB32(ARGB.white(0.8F)))
                .set(EnvironmentAttributes.CLOUD_HEIGHT, 192.33F)
                .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
                .build();

        // 参数取向：保留昼夜循环 / 天空光 / 主世界天空盒，给四个主题群系共用；
        // coordinateScale=4 放大维度坐标尺度（仅影响传送门换算），为多主题分区留足空间。
        context.register(PVZ_2,new DimensionType(
                false,true,false,false,
                4.0, -64, 384, 384,
                blocks.getOrThrow(BlockTags.INFINIBURN_OVERWORLD), 0.2F,
                new DimensionType.MonsterSettings(UniformInt.of(0, 7), 0),
                DimensionType.Skybox.OVERWORLD, CardinalLighting.Type.DEFAULT,
                pvzAttributes,
                timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
                Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))
                ));
    }
}
