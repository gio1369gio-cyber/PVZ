package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Potions" entry。
 */
public class PotionEntry extends BaseGuideEntry {

    public PotionEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.spotlightPage("potion1", PvZItemRegister.CHOCOLATE,
                "Excite Potion", "兴奋药水",
                "$(byellow)Excite potion$() can make the player in an excited state, thereby $(bred)reducing the cooldown of the plant card$(). $(l)Each level of excitement will reduce CD by 10%$(), and the tenth level of excitement can make plant cards have no CD. You can use $(borange)chocolate$() to make Excite potions at the $(l)brewing stand$().",
                "$(byellow)兴奋药水$()可以使玩家进入兴奋状态，从而$(bred)降低植物卡牌的冷却时间$()。$(l)每级兴奋会减少 10% 的冷却时间$()，第十级兴奋可以让植物卡牌没有冷却时间。你可以在$(l)酿造台$()用$(borange)巧克力$()酿造兴奋药水。");

        this.addZhEntryName("药水");
    }

    @Override
    protected String entryName() {
        return "Potions";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.POTION);
    }

    @Override
    protected String entryId() {
        return "potion";
    }
}
