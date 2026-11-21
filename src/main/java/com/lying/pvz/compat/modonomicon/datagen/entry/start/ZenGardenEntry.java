package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Zen Garden" entry。
 */
public class ZenGardenEntry extends BaseGuideEntry {

    public ZenGardenEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("zengarden1", "Zen Garden", "禅意花园",
                "$(l)Zen Garden$() is a biome in $(l)OverWorld$(). It is a quiet and peaceful land where $(l)no creatures will naturally spawn$().$(br)There are 2 kinds of special plants: $(#ff0000)nut trees and chombers$()$(br)[Other content is coming soon]",
                "$(l)禅意花园$()是$(l)主世界$()中的一个生物群系。它是一片安静祥和的土地，$(l)没有生物会自然生成$()。$(br)有 2 种特殊植物：$(#ff0000)坚果树和大嘴花$()$(br)[其他内容即将推出]");

        this.spotlightPage("zengarden2", PvZBlockRegister.CHOMPER.get(),
                "Chomper Plant", "大嘴花",
                "$(l)Chompers$() are very $(l)rare$(), so it will take a lot of effort to find them.",
                "$(l)大嘴花$()非常$(l)稀有$()，因此需要花费很大力气才能找到它们。");

        this.addZhEntryName("禅意花园");
    }

    @Override
    protected String entryName() {
        return "Zen Garden";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZBlockRegister.NUT_SAPLING.get());
    }

    @Override
    protected String entryId() {
        return "zen_garden";
    }
}
