package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Crafting Plant Card" entry。
 */
public class PlantCardEntry extends BaseGuideEntry {

    public PlantCardEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("plantcard1", "Crafting Plant Card", "合成植物卡",
                "Crafting Summon Cards generally need $(#ff0000)original materials, card templates, and corresponding special essences$(). The crafting materials can be $(l)seeds, summoning cards$(), etc. the card template is corresponding to the $(l)quality$() of the Summon Card. The special essence is generally the essence corresponding to the creature.",
                "合成召唤卡通常需要$(#ff0000)原始材料、卡牌模板和对应的特殊精华$()。合成材料可以是$(l)种子、召唤卡$()等；卡牌模板与召唤卡的$(l)品质$()相对应。特殊精华通常是该生物对应的精华。");

        this.textPage("plantcard2", "Card Templates", "卡牌模板",
                "Crafting Card templates need $(#ff0000)Origin Essences and specific materials$(). Special essences was introduced earlier. The original materials are complicated, because different Summon Cards require different materials. Fortunately, a creature only corresponds to one essence. According to the recipe, just look for the ingredients.",
                "合成卡牌模板需要$(#ff0000)源质精华和特定材料$()。特殊精华前面已经介绍过了。原始材料比较复杂，因为不同的召唤卡需要不同的材料。幸运的是，一种生物只对应一种精华。根据配方去寻找材料即可。");

        this.craftingPage("plantcard3", "pvz:card/template/gray_card", "pvz:card/template/white_card",
                "Card Template", "卡牌模板", null, null);
        this.craftingPage("plantcard4", "pvz:card/template/green_card", "pvz:card/template/blue_card",
                "Card Template", "卡牌模板", null, null);
        this.craftingPage("plantcard5", "pvz:card/template/purple_card", "pvz:card/template/gold_card",
                "Card Template", "卡牌模板", null, null);
        this.craftingPage("plantcard6", "pvz:card/template/red_card", "pvz:card/template/black_card",
                "Card Template", "卡牌模板", null, null);

        this.craftingPage("plantcard7", "pvz:card/pea_shooter_card", null,
                "PeaShooter Card", "豌豆射手卡",
                "Take $(#ff0000)PeaShooter Card$() as an example. PeaShooter is a creature with $(l)gray quality and appease essence$(). So its recipe requires $(l)peas, gray card templates and $(#ff0000)Appease Essence$().",
                "以$(#ff0000)豌豆射手卡$()为例。豌豆射手是一种$(l)灰色品质、安抚精华$()的生物。因此它的配方需要$(l)豌豆、灰色卡牌模板和$(#ff0000)安抚精华$()。");

        this.addZhEntryName("合成植物卡");
    }

    @Override
    protected String entryName() {
        return "Crafting Plant Card";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA_SHOOTER_CARD.get());
    }

    @Override
    protected String entryId() {
        return "plant_card";
    }
}
