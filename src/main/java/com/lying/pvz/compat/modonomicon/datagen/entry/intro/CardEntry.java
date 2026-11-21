package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Summon Card" entry。
 */
public class CardEntry extends BaseGuideEntry {

    public CardEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("card1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Summon Card");
        this.pageText(GuideText.convert("Summon Card is the link between players and creatures. Players can summon the corresponding creature by right click Summon Card on the ground while consuming SunNum (some cards is right-clicking the water surface). Currently, the only kind of creature that can be summoned by Summon Card is plant type (zombies may be added in later updates)."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "召唤卡");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("召唤卡是玩家与生物之间的纽带。玩家可以在地面右键召唤卡来召唤对应生物，同时消耗阳光数（部分卡片是右键水面）。目前召唤卡只能召唤植物类生物（僵尸可能在后续更新中加入）。"));

        this.page("card2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Unlocking");
        this.pageText(GuideText.convert("There is an unlocking mechanism for summon card, and many cards are not unlocked when the player's tree level is insufficient. Unlock paths are upgrades, challenge rewards or command. Summon card is After player use card to summon a creature, the creature will treat the player as its owner."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "解锁");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("召唤卡有解锁机制，当玩家树等级不足时许多卡片未解锁。解锁途径为升级、挑战奖励或指令。玩家使用卡片召唤生物后，该生物会把玩家视作主人。"));

        this.page("card3", () -> BookSpotlightPageModel.create()
                .withItem(PvZItemRegister.JALAPENO_CARD.get())
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Common Card");
        this.pageText(GuideText.convert("Maximum stack size: 1\nCan enchant\nLong CoolDown\nUnlimited use\nThe color of card frame is related to its rank."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "普通卡");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("最大堆叠数：1\n可附魔\n冷却时间长\n无限使用\n卡片边框颜色与其品质相关。"));

        this.page("card4", () -> BookSpotlightPageModel.create()
                .withItem(PvZItemRegister.JALAPENO_ENJOY_CARD.get())
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Enjoy Card");
        this.pageText(GuideText.convert("Maximum stack size: 16\nCan't enchant\n1s CoolDown\nCan only be used once\nThe color of card frame is wood."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "体验卡");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("最大堆叠数：16\n不可附魔\n1 秒冷却\n只能使用一次\n卡片边框颜色为木质。"));

        this.page("card5", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Planting Limit");
        this.pageText(GuideText.convert("After 0.6 version, the card placement becomes more strict. Only 50 plants can be planted within 30 blocks, after that every 5 more sun cost will be doubled. Densely planting enchantment can slow down this situation."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "种植限制");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("0.6 版本后，卡片放置更加严格。30 格内只能种植 50 株植物，超出后每多种 5 株阳光消耗翻倍。密集种植附魔可以缓解这一情况。"));

        this.page("card6", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Acquisition");
        this.pageText(GuideText.convert("The previous two pages introduced the difference between Common Summon Cards and Enjoy Cards, and their acquisition is actually different. Common Cards are generally obtained by crafting or purchase, while Enjoy Cards are often obtained by opening treasure chests or lottery or finish invasion."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "获取方式");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("前两页介绍了普通召唤卡与体验卡的区别，它们的获取方式也不同。普通卡一般通过合成或购买获得，而体验卡常通过开宝箱、抽奖或完成入侵获得。"));

        this.addZhEntryName("召唤卡");
    }

    @Override
    protected String entryName() {
        return "Summon Card";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA_SHOOTER_CARD.get());
    }

    @Override
    protected String entryId() {
        return "card";
    }
}
