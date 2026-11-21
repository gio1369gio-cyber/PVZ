package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Enchantments" entry。原 spotlight 物品带附魔 NBT，这里保留标题/文本，用基础物品展示。
 */
public class EnchantEntry extends BaseGuideEntry {

    public EnchantEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.spotlightPage("enchant1", PvZItemRegister.SUN_FLOWER_CARD.get(),
                "Break Out", "突破",
                "When PlantCards have $(bred)Break Out$() Enchantment, every time the card summons a plant, it will give the plant a chance to be super mode. Its max lvl is 4, $(l)5% for lvl 1, 7.5% for 2, 10% for 3, and 12.5% for lvl 4$().",
                "当植物卡拥有$(bred)突破$()附魔时，卡牌每次召唤植物都会有一定几率为该植物赋予超级模式。其最高等级为 4，$(l)1 级 5%，2 级 7.5%，3 级 10%，4 级 12.5%$()。");

        this.spotlightPage("enchant2", Items.DIAMOND_SWORD,
                "Energy Transfer", "能量转移",
                "When Swords have $(bred)Energy Transfer$() Enchantment, Players can right click plants to consume energy num and make plants in super mode. Its max lvl is 1.",
                "当剑拥有$(bred)能量转移$()附魔时，玩家可以右键点击植物，消耗能量值使植物进入超级模式。其最高等级为 1。");

        this.spotlightPage("enchant3", PvZItemRegister.WALL_NUT_CARD.get(),
                "Charm", "魅惑",
                "When PlantCards have $(bred)Charm$() Enchantment, The plant summoned will be charmed and attack its owner. Its max lvl is 1.",
                "当植物卡拥有$(bred)魅惑$()附魔时，召唤出的植物会被魅惑并攻击其主人。其最高等级为 1。");

        this.spotlightPage("enchant4", Items.IRON_SHOVEL,
                "Sun Shovel", "阳光铲",
                "When Shovels have $(bred)Sun Shovel$() Enchantment, the plants it shoveled will give some sun back. Its max lvl is 5.",
                "当铲子拥有$(bred)阳光铲$()附魔时，铲除的植物会返还一些阳光。其最高等级为 5。");

        this.spotlightPage("enchant5", Items.GOLDEN_AXE,
                "Sun Mending", "阳光修补",
                "Similar to Mending, when tools have $(bred)Sun Mending$() Enchantment, Player can fix their tools when collide with sun. Its max lvl is 3.",
                "与经验修补类似，当工具拥有$(bred)阳光修补$()附魔时，玩家在与阳光碰撞时可以修复工具。其最高等级为 3。");

        this.spotlightPage("enchant6", PvZItemRegister.SQUASH_CARD.get(),
                "Soilless Plant", "无土栽培",
                "When PlantCards have $(bred)Soilless Plant$() Enchantment, the plants summoned are able to survive on every block. Its max lvl is 1.",
                "当植物卡拥有$(bred)无土栽培$()附魔时，召唤出的植物可以存活在任何方块上。其最高等级为 1。");

        this.spotlightPage("enchant7", PvZItemRegister.CABBAGE_PULT_CARD.get(),
                "Densely Plant", "密集种植",
                "When PlantCards have $(bred)Densely Plant$() Enchantment, it can place 10 more each level before reach count limit.",
                "当植物卡拥有$(bred)密集种植$()附魔时，每级可以在达到数量上限前多放置 10 株植物。");

        this.spotlightPage("enchant8", PvZItemRegister.CABBAGE_PULT_CARD.get(),
                "Immediate CD", "即时冷却",
                "When PlantCards have $(bred)Immediate CD$() Enchantment, card have chance to only have 1 second cool down.",
                "当植物卡拥有$(bred)即时冷却$()附魔时，卡牌有几率只需 1 秒冷却。");

        this.addZhEntryName("附魔");
    }

    @Override
    protected String entryName() {
        return "Enchantments";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.ENCHANTED_BOOK);
    }

    @Override
    protected String entryId() {
        return "enchant";
    }
}
