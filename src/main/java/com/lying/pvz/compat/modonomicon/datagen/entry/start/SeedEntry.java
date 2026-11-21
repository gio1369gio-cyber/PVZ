package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Seed" entry。
 */
public class SeedEntry extends BaseGuideEntry {

    public SeedEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.spotlightPage("seed1", PvZItemRegister.PEA,
                "Pea", "豌豆",
                "Peas may drop when you $(l)break grass$(). Peas can be planted like crops, and right click to harvest them when matured.",
                "当你$(l)破坏草$()时可能会掉落豌豆。豌豆可以像农作物一样种植，成熟后右键点击即可收获。");

        this.craftingPage("seed2", "pvz:snow_pea", "pvz:flame_pea",
                "Snow Pea & Flame Pea", "寒冰豌豆与火焰豌豆", null, null);

        this.spotlightPage("seed3", PvZItemRegister.NUT,
                "Nut", "坚果",
                "Nut trees grow in the Zen garden, and the nuts fall through the nut leaves.",
                "坚果树生长在禅意花园中，坚果会从坚果树叶上掉落。");

        this.spotlightPage("seed4", PvZBlockRegister.CHOMPER.get(),
                "Chomper", "大嘴花",
                "Chompers grow in Zen Garden and are $(l)very rare$(). Fortunately, bone meal can make them grow.",
                "大嘴花生长在禅意花园中，$(l)非常稀有$()。幸运的是，骨粉可以促使它们生长。");

        this.spotlightPage("seed5", PvZItemRegister.PEPPER,
                "Pepper", "辣椒",
                "Killing $(#ff0000)Lava zombies$() has a chance to drop peppers, you can find them in $(#ff0000)Nether$().",
                "击杀$(#ff0000)熔岩僵尸$()有几率掉落辣椒，你可以在$(#ff0000)下界$()找到它们。");

        this.spotlightPage("seed6", PvZItemRegister.CABBAGE,
                "Cabbage", "卷心菜",
                "Cabbage Seeds may drop when you $(l)break grass$(). They can be planted like crops.",
                "当你$(l)破坏草$()时可能会掉落卷心菜种子。它们可以像农作物一样种植。");

        this.spotlightPage("seed7", PvZItemRegister.CORN,
                "Corn", "玉米",
                "Corn Seeds may drop when kill zombies. They can be planted like crops.",
                "击杀僵尸时可能会掉落玉米种子。它们可以像农作物一样种植。");

        this.spotlightPage("seed8", PvZItemRegister.SPORE,
                "Spore", "孢子",
                "Trade with wander villager, they can be planted in place where $(bred)sky light is 0$(). Right click can harvest.",
                "与流浪商人交易获得，它们可以种植在$(bred)天空光照为 0$()的地方。右键点击即可收获。");

        this.addZhEntryName("种子");
    }

    @Override
    protected String entryName() {
        return "Seed";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA);
    }

    @Override
    protected String entryId() {
        return "seed";
    }
}
