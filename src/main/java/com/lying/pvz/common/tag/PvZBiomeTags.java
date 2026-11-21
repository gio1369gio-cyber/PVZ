package com.lying.pvz.common.tag;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class PvZBiomeTags {
    // ================= 用于结构生成 =================
    public static final TagKey<Biome> HAS_BUCKET_HOUSE = create("has_structure/bucket_house");
    public static final TagKey<Biome> HAS_YETI_HOUSE = create("has_structure/yeti_house");
    public static final TagKey<Biome> HAS_SUN_TEMPLE = create("has_structure/sun_temple");
    public static final TagKey<Biome> HAS_DOLPHIN_HOUSE = create("has_structure/dolphin_house");
    public static final TagKey<Biome> HAS_DAVE_VILLA = create("has_structure/dave_villa");
    public static final TagKey<Biome> HAS_GRAVE_HOUSE = create("has_structure/grave_house");

    // ================= 用于 BiomeModifier (刷怪/矿物) =================
    public static final TagKey<Biome> IS_LAND = create("is_land");
    public static final TagKey<Biome> IS_SNOWY_LAND = create("is_snowy_land");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, StringUtil.id(name));
    }
}
