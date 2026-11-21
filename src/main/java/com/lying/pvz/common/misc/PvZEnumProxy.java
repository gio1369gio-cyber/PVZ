package com.lying.pvz.common.misc;


import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class PvZEnumProxy {
    public static final EnumProxy<MobCategory> PVZ_PLANT_PROXY = new EnumProxy<>(
            MobCategory.class, "pvz:plant", "PL", 5, true, false, 128);
    public static final EnumProxy<MobCategory> PVZ_ZOMBIE_PROXY = new EnumProxy<>(
            MobCategory.class, "pvz:zombie", "ZO", 10, false, true, 128);

    public static final EnumProxy<Rarity> GRAY = new EnumProxy<>(Rarity.class, -1, "pvz:gray",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.GRAY));
    public static final EnumProxy<Rarity> WHITE = new EnumProxy<>(Rarity.class, -1, "pvz:white",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.WHITE));
    public static final EnumProxy<Rarity> GREEN = new EnumProxy<>(Rarity.class, -1, "pvz:green",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.GREEN));
    public static final EnumProxy<Rarity> BLUE = new EnumProxy<>(Rarity.class, -1, "pvz:blue",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.BLUE));
    public static final EnumProxy<Rarity> PURPLE = new EnumProxy<>(Rarity.class, -1, "pvz:purple",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.LIGHT_PURPLE));
    public static final EnumProxy<Rarity> GOLD = new EnumProxy<>(Rarity.class, -1, "pvz:gold",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.GOLD));
    public static final EnumProxy<Rarity> RED = new EnumProxy<>(Rarity.class, -1, "pvz:red",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.RED));
    public static final EnumProxy<Rarity> BLACK = new EnumProxy<>(Rarity.class, -1, "pvz:black",
            (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.DARK_GRAY));

    /* 用 ID 比较避开 DeferredHolder 的 == 问题与初始化时机问题 */
    private static final Identifier ID_GRAY = StringUtil.id("gray");
    private static final Identifier ID_WHITE = StringUtil.id("white");
    private static final Identifier ID_GREEN = StringUtil.id("green");
    private static final Identifier ID_BLUE = StringUtil.id("blue");
    private static final Identifier ID_PURPLE = StringUtil.id("purple");
    private static final Identifier ID_GOLD = StringUtil.id("gold");
    private static final Identifier ID_RED = StringUtil.id("red");
    private static final Identifier ID_BLACK = StringUtil.id("black");

    public static Rarity getRarityByRank(IRankType rankType) {
        final Identifier id = rankType.getID();
        if (id.equals(ID_GRAY)) return GRAY.getValue();
        if (id.equals(ID_WHITE)) return WHITE.getValue();
        if (id.equals(ID_GREEN)) return GREEN.getValue();
        if (id.equals(ID_BLUE)) return BLUE.getValue();
        if (id.equals(ID_PURPLE)) return PURPLE.getValue();
        if (id.equals(ID_GOLD)) return GOLD.getValue();
        if (id.equals(ID_RED)) return RED.getValue();
        if (id.equals(ID_BLACK)) return BLACK.getValue();
        return Rarity.COMMON;
    }
}