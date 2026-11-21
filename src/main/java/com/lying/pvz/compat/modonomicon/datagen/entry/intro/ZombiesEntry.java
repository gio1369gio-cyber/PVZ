package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Zombie" entry。
 */
public class ZombiesEntry extends BaseGuideEntry {

    public ZombiesEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("zombies1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Zombie");
        this.pageText(GuideText.convert("Zombies are crazy creatures that feed on brains. They are enemies with plants and also attack players and Crazy Dave. There are many types of zombies, some can walk on the road; some can drive a car, some can dive in the sea, there are even zombies that can ride a dolphin."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "僵尸");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("僵尸是以大脑为食的疯狂生物。它们与植物为敌，也会攻击玩家和疯狂戴夫。僵尸种类繁多，有的在路上行走，有的开车，有的潜入海中，甚至有能骑海豚的僵尸。"));

        this.page("zombies2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Rewards");
        this.pageText(GuideText.convert("There are various rewards for killing zombies, players can get TreeXp when they and their plants kill zombies. Zombies have chances to drop Coins(silver coins, gold coins), Gems, Chocolate, plant seeds, origin essence and so on. If you kill a zombie surrounded by green light, it will definitely drop Energy. If you kill a zombie surrounded by yellow light, it will definitely drop Sun."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "奖励");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("击杀僵尸有各种奖励，玩家及其植物击杀僵尸可获得树经验。僵尸有几率掉落金币（银币、金币）、宝石、巧克力、植物种子、原始精华等。击杀被绿光环绕的僵尸必掉能量，击杀被黄光环绕的僵尸必掉阳光。"));

        this.addZhEntryName("僵尸");
    }

    @Override
    protected String entryName() {
        return "Zombie";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.ZOMBIE_DOLL);
    }

    @Override
    protected String entryId() {
        return "zombies";
    }
}
