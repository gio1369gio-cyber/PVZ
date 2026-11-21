package com.lying.pvz.compat.modonomicon.datagen.entry.weapon;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Pea Gun" entry。
 */
public class PeaGunEntry extends BaseGuideEntry {

    public PeaGunEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("peagun1", "Pea Gun", "豌豆枪",
                "$(bgreen)Pea Gun$() is a range-attack plant weapon that can fire peas. Right-click it in the $(l)off hand$() to open the backpack, in the $(l)main hand$() to fire peas, and $(l)it can also be worn on the head and fired automatically$(). It comes with a $(#ff0000)27-cell ammunition backpack for peas and a card slot$(), which is used to put plant cards to $(l)control the shooting mode$() of Pea Gun.",
                "$(bgreen)豌豆枪$()是一种可以发射豌豆的远程攻击植物武器。在$(l)副手$()右键点击可以打开背包，在$(l)主手$()右键点击可以发射豌豆，$(l)它也可以戴在头上自动发射$()。它自带一个可容纳$(#ff0000)27 格豌豆的弹药背包和一个卡槽$()，卡槽用于放置植物卡以$(l)控制豌豆枪的射击模式$()。");

        this.textPage("peagun2", "Shooting Type", "发射类型",
                "The fired pea type is decided by the $(l)shooting mode$() and the $(l)ammunition$() in the backpack: $(l)normal Pea$(), $(#ff0000)Ice Pea$() (from Snow Pea mode or ice pea ammo) and Flame Pea (from flame pea ammo)$(). The fire cooldown is $(l)30 ticks$() by default and is shortened by the $(#ff0000)Quick Charge$() enchantment (20/15/10 ticks for lvl 1/2/3). Power enchantment adds $(l)0.5$() base damage per level (base 1.5). Tree level only slightly improves the no-consumption chance of the Infinity enchantment.",
                "发射的豌豆类型由$(l)射击模式$()和弹药背包中的弹药决定：$(l)普通豌豆$()、$(#ff0000)寒冰豌豆$()（寒冰射手模式或寒冰豌豆弹药）和火焰豌豆（火焰豌豆弹药）。射击冷却默认为$(l)30 tick$()，可通过$(#ff0000)快速装填$()附魔缩短（1/2/3 级分别为 20/15/10 tick）。力量附魔每级增加$(l)0.5$() 基础伤害（基础 1.5）。智慧树等级仅略微提升无限附魔的不消耗豌豆几率。");

        this.textPage("peagun3", "Enchantments", "附魔",
                "$(l)Pea Gun$() can be enchanted with $(#ff0000)Power, Infinity, Quick Charge and Unbreaking$(). Power $(l)increases the initial damage$() of Pea. Infinity gives a $(l)chance not to consume peas$() (unlike a bow), and this chance is affected by the $(#ff0000)Tree Level$(). Quick Charge shortens the fire cooldown, while Unbreaking increases durability.",
                "$(l)豌豆枪$()可以附魔$(#ff0000)力量、无限、快速装填和耐久$()。力量附魔会$(l)提高豌豆的初始伤害$()。无限附魔不同于弓，而是有$(l)几率不消耗豌豆$()，这个几率受$(#ff0000)智慧树等级$()影响。快速装填缩短射击冷却，耐久附魔提升耐用度。");

        this.craftingPage("peagun4", "pvz:pea_gun", null,
                "Pea Gun Recipe", "豌豆枪配方",
                "The recipe of Pea Gun needs $(l)four peas, one dispenser, four Appease Essences and two Origin Ingots$().",
                "豌豆枪的配方需要$(l)四个豌豆、一个发射器、四个安抚精华和两个源质锭$()。");

        this.spotlightPage("peagun5", PvZItemRegister.PEA_SHOOTER_CARD.get(),
                "PeaShooter Mode", "豌豆射手模式",
                "normal shoot style, shoot forward one pea at a time",
                "普通射击方式，每次向前发射一颗豌豆");

        this.spotlightPage("peagun6", PvZItemRegister.SNOW_PEA_CARD.get(),
                "SnowPea Mode", "寒冰射手模式",
                "normal shoot style, $(l)no matter what kind of pea is in the ammunition backpack, the final shot out is $(#ff0000)snow peas$().",
                "普通射击方式，$(l)无论弹药背包中是哪种豌豆，最终射出的都是$(#ff0000)寒冰豌豆$()。");

        this.spotlightPage("peagun7", PvZItemRegister.REPEATER_CARD.get(),
                "Repeater Mode", "双发射手模式",
                "shoot forward two peas at a time",
                "每次向前发射两颗豌豆");

        this.spotlightPage("peagun8", PvZItemRegister.THREE_PEATER_CARD.get(),
                "ThreePeater Mode", "三线射手模式",
                "shoot three peas forward at a time in a fan (-15/0/+15 degrees)",
                "每次向前呈扇形发射三颗豌豆（-15/0/+15 度）");

        this.spotlightPage("peagun9", PvZItemRegister.SPLIT_PEA_CARD.get(),
                "SplitPea Mode", "裂荚射手模式",
                "shoot forward one peas and shoot backward two peas at a time",
                "每次向前发射一颗豌豆，向后发射两颗豌豆");

        this.spotlightPage("peagun10", PvZItemRegister.GATLING_PEA_CARD.get(),
                "GatlingPea Mode", "机枪射手模式",
                "shoot forward four peas at a time",
                "每次向前发射四颗豌豆");

        this.spotlightPage("peagun11", PvZItemRegister.STAR_FRUIT_CARD.get(),
                "StarFruit Mode", "杨桃模式",
                "shoot five peas at a time in a star pattern, $(l)72 degrees apart with a random starting angle$()",
                "每次呈星形发射五颗豌豆，$(l)相邻间隔 72 度，起始角度随机$()");

        this.spotlightPage("peagun12", PvZItemRegister.ANGEL_STAR_FRUIT_CARD.get(),
                "AngelStarFruit Mode", "天使杨桃模式",
                "shoot five peas at a time in a star pattern, $(l)72 degrees apart$()",
                "每次呈星形发射五颗豌豆，$(l)相邻间隔 72 度$()");

        this.addZhEntryName("豌豆枪");
    }

    @Override
    protected String entryName() {
        return "Pea Gun";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA_GUN);
    }

    @Override
    protected String entryId() {
        return "pea_gun";
    }
}
