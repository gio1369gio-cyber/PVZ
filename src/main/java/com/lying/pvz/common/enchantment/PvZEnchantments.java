package com.lying.pvz.common.enchantment;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.Optional;

/**
 * 十个 PVZ 附魔的 Enchantment 实例工厂。数值从原 1.16.5 十个 Enchantment 子类的
 * getMinCost / getMaxCost / getMaxLevel / Rarity 精确换算而来。
 */
public final class PvZEnchantments {

    // ===== 自定义 item tag =====
    public static final TagKey<Item> ENCHANTABLE_SUMMON_CARD = itemTag("enchantable/summon_card");
    public static final TagKey<Item> ENCHANTABLE_ENTITY_CARD = itemTag("enchantable/entity_card");
    public static final TagKey<Item> ENCHANTABLE_PLANT_OR_OUTER = itemTag("enchantable/plant_or_outer_card");
    public static final TagKey<Item> ENCHANTABLE_NO_OUTER_PLANT = itemTag("enchantable/no_outer_plant_card");
    public static final TagKey<Item> ENCHANTABLE_REACH = itemTag("enchantable/reach");

    private static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registries.ITEM, StringUtil.id(path));
    }

    // ===== 十个附魔工厂 =====

    /** 能量传输 —— Rarity.UNCOMMON, MAINHAND, minCost=30, maxCost=100, maxLevel=1。 */
    public static Enchantment energyTransfer(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.energy_transfer",
                /*weight*/ 5, /*maxLevel*/ 1,
                /*minCost*/ 30, 0, /*maxCost*/ 100, 0,
                /*anvilCost*/ 2,
                List.of(EquipmentSlotGroup.MAINHAND),
                ItemTags.WEAPON_ENCHANTABLE,
                HolderSet.empty());
    }

    /** 范围扩展 —— Rarity.UNCOMMON, MAINHAND, minCost=10L, maxCost=30+10L, maxLevel=3。 */
    public static Enchantment rangeReach(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.range_reach",
                5, 3,
                10, 10, 30, 10,
                2, List.of(EquipmentSlotGroup.MAINHAND),
                ENCHANTABLE_REACH,
                HolderSet.empty());
    }

    /** 阳光修补 —— Rarity.VERY_RARE, 全槽, minCost=30+10L, maxCost=40+10L, maxLevel=3, 排斥 MENDING。 */
    public static Enchantment sunMending(HolderGetter<Item> items, HolderGetter<Enchantment> enchantments) {
        HolderSet<Enchantment> exclusive = HolderSet.direct(
                enchantments.getOrThrow(Enchantments.MENDING));
        return build(items, "enchantment.pvz.sun_mending",
                1, 3,
                30, 10, 40, 10,
                4, List.of(EquipmentSlotGroup.ANY),
                ItemTags.DURABILITY_ENCHANTABLE,
                exclusive);
    }

    /** 阳光铲 —— Rarity.RARE, HAND, minCost=30+10L, maxCost=40+10L, maxLevel=5。 */
    public static Enchantment sunShovel(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.sun_shovel",
                2, 5,
                30, 10, 40, 10,
                3, List.of(EquipmentSlotGroup.HAND),
                ItemTags.SHOVELS,
                HolderSet.empty());
    }

    /** 绷带 —— Rarity.UNCOMMON, MAINHAND, minCost=10L, maxCost=20+10L, maxLevel=4。 */
    public static Enchantment bandage(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.bandage",
                5, 4,
                0, 10, 20, 10,
                2, List.of(EquipmentSlotGroup.MAINHAND),
                ENCHANTABLE_SUMMON_CARD,
                HolderSet.empty());
    }

    /** 魅惑 —— Rarity.VERY_RARE, MAINHAND, minCost=30, maxCost=100, maxLevel=1。 */
    public static Enchantment charm(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.charm",
                1, 1,
                30, 0, 100, 0,
                4, List.of(EquipmentSlotGroup.MAINHAND),
                ENCHANTABLE_ENTITY_CARD,
                HolderSet.empty());
    }

    /** 立即冷却 —— Rarity.UNCOMMON, MAINHAND, minCost=20L-15, maxCost=minCost+15, maxLevel=3。 */
    public static Enchantment immediateCd(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.immediate_cd",
                5, 3,
                5, 20, 20, 20,
                2, List.of(EquipmentSlotGroup.MAINHAND),
                ENCHANTABLE_SUMMON_CARD,
                HolderSet.empty());
    }

    /** 破出 —— Rarity.VERY_RARE, MAINHAND, minCost=25L, maxCost=35+25L, maxLevel=4。 */
    public static Enchantment breakOut(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.break_out",
                1, 4,
                25, 25, 35, 25,
                4, List.of(EquipmentSlotGroup.MAINHAND),
                ENCHANTABLE_PLANT_OR_OUTER,
                HolderSet.empty());
    }

    /** 密集种植 —— Rarity.RARE, HAND, minCost=40, maxCost=100, maxLevel=2。 */
    public static Enchantment denselyPlant(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.densely_plant",
                2, 2,
                40, 0, 100, 0,
                3, List.of(EquipmentSlotGroup.HAND),
                ENCHANTABLE_NO_OUTER_PLANT,
                HolderSet.empty());
    }

    /** 无土种植 —— Rarity.VERY_RARE, HAND, minCost=40, maxCost=100, maxLevel=1。 */
    public static Enchantment soillessPlant(HolderGetter<Item> items) {
        return build(items, "enchantment.pvz.soilless_plant",
                1, 1,
                40, 0, 100, 0,
                4, List.of(EquipmentSlotGroup.HAND),
                ENCHANTABLE_NO_OUTER_PLANT,
                HolderSet.empty());
    }

    // ===== 通用构造 =====

    private static Enchantment build(HolderGetter<Item> itemLookup,
                                     String descriptionKey,
                                     int weight, int maxLevel,
                                     int minCostBase, int minCostPerLevel,
                                     int maxCostBase, int maxCostPerLevel,
                                     int anvilCost,
                                     List<EquipmentSlotGroup> slots,
                                     TagKey<Item> supportedTag,
                                     HolderSet<Enchantment> exclusive) {
        HolderSet<Item> supportedItems = itemLookup.get(supportedTag)
                .<HolderSet<Item>>map(h -> h)
                .orElseGet(HolderSet::empty);

        Enchantment.EnchantmentDefinition definition = new Enchantment.EnchantmentDefinition(
                supportedItems,
                Optional.empty(),
                weight,
                maxLevel,
                new Enchantment.Cost(minCostBase, minCostPerLevel),
                new Enchantment.Cost(maxCostBase, maxCostPerLevel),
                anvilCost,
                slots
        );

        return new Enchantment(
                Component.translatable(descriptionKey).withStyle(ChatFormatting.GRAY),
                definition,
                exclusive,
                DataComponentMap.EMPTY
        );
    }

    private PvZEnchantments() {}
}