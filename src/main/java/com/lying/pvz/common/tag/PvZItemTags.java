package com.lying.pvz.common.tag;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class PvZItemTags {

    // minecraft
    public static final TagKey<Item> ARROWS = mcTag("arrows");
    // neoforge
    public static final TagKey<Item> PEAS = neoForge("crops/pea");
    public static final TagKey<Item> NUTS = neoForge("crops/nut");
    public static final TagKey<Item> CHILIPEPPERS = neoForge("crops/chilipepper");
    public static final TagKey<Item> CHILI_SEEDS = neoForge("seeds/chilipepper");
    public static final TagKey<Item> CABBAGES = neoForge("crops/cabbage");
    public static final TagKey<Item> CABBAGE_SEEDS = neoForge("seeds/cabbage");
    public static final TagKey<Item> CORNS = neoForge("crops/corn");
    public static final TagKey<Item> CORN_SEEDS = neoForge("seeds/corn");
    public static final TagKey<Item> GARLIC = neoForge("crops/garlic");
    public static final TagKey<Item> AMETHYST_INGOTS = neoForge("ingots/amethyst");
    public static final TagKey<Item> AMETHYST_ORES = neoForge("ores/amethyst");

    // pvz
    public static final TagKey<Item> PLANT_SUMMON_CARDS = pvzTag("cards/plant_summon_card");
    public static final TagKey<Item> PLANT_ENJOY_CARDS = pvzTag("cards/plant_enjoy_card");
    public static final TagKey<Item> PLANT_CARDS = pvzTag("cards/plant_card");
    public static final TagKey<Item> TEMPLATE_CARDS = pvzTag("cards/template_card");
    public static final TagKey<Item> ESSENCE_ORES = pvzTag("ores/essence");
    public static final TagKey<Item> PEA_GUN_BULLETS = pvzTag("pea_gun_bullets");
    public static final TagKey<Item> REACH_ITEMS = pvzTag("reach_items");
    public static final TagKey<Item> GRAY_MATERIALS = pvzTag("card/gray_materials");
    public static final TagKey<Item> WHITE_MATERIALS = pvzTag("card/white_materials");
    public static final TagKey<Item> GREEN_MATERIALS = pvzTag("card/green_materials");
    public static final TagKey<Item> BLUE_MATERIALS = pvzTag("card/blue_materials");
    public static final TagKey<Item> PURPLE_MATERIALS = pvzTag("card/purple_materials");
    public static final TagKey<Item> GOLD_MATERIALS = pvzTag("card/gold_materials");
    public static final TagKey<Item> RED_MATERIALS = pvzTag("card/red_materials");
    public static final TagKey<Item> BLACK_MATERIALS = pvzTag("card/black_materials");
    public static final TagKey<Item> GRAY_CARDS = pvzTag("card/gray_cards");
    public static final TagKey<Item> WHITE_CARDS = pvzTag("card/white_cards");
    public static final TagKey<Item> GREEN_CARDS = pvzTag("card/green_cards");
    public static final TagKey<Item> BLUE_CARDS = pvzTag("card/blue_cards");
    public static final TagKey<Item> PURPLE_CARDS = pvzTag("card/purple_cards");
    public static final TagKey<Item> GOLD_CARDS = pvzTag("card/gold_cards");
    public static final TagKey<Item> RED_CARDS = pvzTag("card/red_cards");
    public static final TagKey<Item> BLACK_CARDS = pvzTag("card/black_cards");
    public static final TagKey<Item> ORIGIN_TOOL_MATERIALS = pvzTag("origin_tool_materials");
    public static final TagKey<Item> PEN_GUN_REPAIRABLE = pvzTag("pen_gun_repairable");



    private static TagKey<Item> pvzTag(String name){
        return TagKey.create(Registries.ITEM, StringUtil.id(name));
    }

    private static TagKey<Item> neoForge(String name){
        return TagKey.create(Registries.ITEM, StringUtil.idNeoforge(name));
    }

    private static TagKey<Item> mcTag(String name){
        return TagKey.create(Registries.ITEM, Identifier.parse(name));
    }
}
