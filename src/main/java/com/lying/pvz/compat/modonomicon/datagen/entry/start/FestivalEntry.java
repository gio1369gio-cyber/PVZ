package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Festivals" entry。
 */
public class FestivalEntry extends BaseGuideEntry {

    public FestivalEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("festival1", "Festivals", "节日",
                "$(l)Version 0.2.3 of the Plants vs Zombies mod$() updates $(borange)Halloween-related$() content, adding a Halloween zombie invasion type. On the day of the Halloween invasion, $(bred)trick zombies and pumpkin zombies$() will spawn. Killing them will has probability of dropping $(bpurple)candy$().",
                "$(l)植物大战僵尸模组 0.2.3 版本$()更新了$(borange)万圣节相关$()内容，新增了一种万圣节僵尸入侵类型。在万圣节入侵当天，会生成$(bred)捣蛋僵尸和南瓜僵尸$()。击杀它们有几率掉落$(bpurple)糖果$()。");

        this.craftingPage("festival2", "pvz:food/chocolate", null,
                "Chocolate Recipe", "巧克力配方",
                "In general, $(borange)Chocolate$() is difficult to obtain, but if you have $(bpurple)Candy$(), you can directly craft it.",
                "一般来说，$(borange)巧克力$()很难获得，但如果你有$(bpurple)糖果$()，就可以直接合成它。");

        this.addZhEntryName("节日");
    }

    @Override
    protected String entryName() {
        return "Festivals";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.PUMPKIN);
    }

    @Override
    protected String entryId() {
        return "festival";
    }
}
