package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Zombie House" entry。
 */
public class ZombieHouseEntry extends BaseGuideEntry {

    public ZombieHouseEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("zombiehouse1", "Zombie House", "僵尸小屋",
                "$(l)Zombie House$() is a structure generated in the world. There are currently Bucket, Dolphin and Grave Houses. Zombie Houses usually have chests for $(#ff0000)Enjoy Cards$(), but the reward is accompanied by danger. There is also a $(l)spawner for zombies$(), which is also a form of zombie spawning.",
                "$(l)僵尸小屋$()是一种生成在世界中的结构。目前有水桶小屋、海豚小屋和坟墓小屋。僵尸小屋通常有装有$(#ff0000)享受卡$()的箱子，但奖励往往伴随着危险。还有$(l)僵尸刷怪笼$()，这也是僵尸生成的一种形式。");

        this.addZhEntryName("僵尸小屋");
    }

    @Override
    protected String entryName() {
        return "Zombie House";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.STONE);
    }

    @Override
    protected String entryId() {
        return "zombie_house";
    }
}
