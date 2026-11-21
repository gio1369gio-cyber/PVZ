package com.lying.pvz.common.projectile;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.resources.Identifier;

/**
 * 弹药 Kind / 视觉键 / 触发器 tag 的公共常量。
 *
 * <p>视觉键是服务端与客户端之间唯一的"外观协议"：
 * {@code VariantDef.visualKey()} → {@code BulletVisualRegistry}。
 */
public final class BulletVisuals {

    /** 豌豆 Kind id。 */
    public static final Identifier PEA_KIND = StringUtil.id("pea");
    public static final Identifier CABBAGE_KIND = StringUtil.id("cabbage");
    public static final Identifier KERNEL_KIND = StringUtil.id("kernel");
    public static final Identifier BUTTER_KIND = StringUtil.id("butter");
    public static final Identifier MELON_KIND = StringUtil.id("melon");
    public static final Identifier CORN_KIND = StringUtil.id("corn");
    public static final Identifier BALL_KIND = StringUtil.id("ball");
    public static final Identifier FIRE_CRACKER_KIND = StringUtil.id("fire_cracker");
    public static final Identifier POTATO_KIND = StringUtil.id("potato");
    public static final Identifier NUT_KIND = StringUtil.id("nut");
    public static final Identifier STAR_KIND = StringUtil.id("star");
    public static final Identifier SPORE_KIND = StringUtil.id("spore");
    public static final Identifier FUME_KIND = StringUtil.id("fume");
    public static final Identifier THORN_KIND = StringUtil.id("thorn");
    public static final Identifier METAL_KIND = StringUtil.id("metal");
    public static final Identifier BEE_KIND = StringUtil.id("bee");
    public static final Identifier STONE_PEA_KIND = StringUtil.id("stone_pea");
    public static final Identifier LIGHT_BEAM_KIND = StringUtil.id("light_beam");
    public static final Identifier CHORUS_FRUIT_KIND = StringUtil.id("chorus_fruit");

    public static final Identifier PEA = StringUtil.id("bullet/pea");
    public static final Identifier SNOW_PEA = StringUtil.id("bullet/snow_pea");
    public static final Identifier FLAME_PEA = StringUtil.id("bullet/flame_pea");
    public static final Identifier BLUE_FLAME_PEA = StringUtil.id("bullet/blue_flame_pea");

    public static final Identifier CABBAGE = StringUtil.id("bullet/cabbage");
    public static final Identifier ICE_CABBAGE = StringUtil.id("bullet/ice_cabbage");
    public static final Identifier KERNEL = StringUtil.id("bullet/kernel");
    public static final Identifier BUTTER = StringUtil.id("bullet/butter");
    public static final Identifier MELON = StringUtil.id("bullet/melon");
    public static final Identifier FROZEN_MELON = StringUtil.id("bullet/frozen_melon");
    public static final Identifier GOLDEN_MELON = StringUtil.id("bullet/golden_melon");
    public static final Identifier CORN = StringUtil.id("bullet/corn");
    public static final Identifier BALL = StringUtil.id("bullet/ball");
    public static final Identifier FIRE_CRACKER = StringUtil.id("bullet/fire_cracker");
    public static final Identifier POTATO = StringUtil.id("bullet/potato");
    public static final Identifier NUT = StringUtil.id("bullet/nut");
    public static final Identifier STAR_YELLOW = StringUtil.id("bullet/star_yellow");
    public static final Identifier STAR_PINK = StringUtil.id("bullet/star_pink");
    public static final Identifier STAR_BIG_YELLOW = StringUtil.id("bullet/star_big_yellow");
    public static final Identifier STAR_BIG_PINK = StringUtil.id("bullet/star_big_pink");
    public static final Identifier STAR_HUGE_YELLOW = StringUtil.id("bullet/star_huge_yellow");
    public static final Identifier STAR_HUGE_PINK = StringUtil.id("bullet/star_huge_pink");
    public static final Identifier SPORE = StringUtil.id("bullet/spore");
    public static final Identifier THORN = StringUtil.id("bullet/thorn");
    public static final Identifier POWER_THORN = StringUtil.id("bullet/power_thorn");
    public static final Identifier THORN_AUTO = StringUtil.id("bullet/thorn_auto");
    public static final Identifier POWER_THORN_AUTO = StringUtil.id("bullet/power_thorn_auto");
    public static final Identifier METAL = StringUtil.id("bullet/metal");
    public static final Identifier BEE = StringUtil.id("bullet/bee");
    public static final Identifier STONE_PEA = StringUtil.id("bullet/stone_pea");
    public static final Identifier MAGMA_PEA = StringUtil.id("bullet/magma_pea");
    public static final Identifier LIGHT_BEAM = StringUtil.id("bullet/light_beam");
    public static final Identifier CHORUS_FRUIT = StringUtil.id("bullet/chorus_fruit");
    public static final Identifier POPPED_CHORUS_FRUIT = StringUtil.id("bullet/popped_chorus_fruit");

    /** 可被加热类触发器作用。 */
    public static final Identifier HEATABLE = StringUtil.id("heatable");

    private BulletVisuals() {
    }
}
