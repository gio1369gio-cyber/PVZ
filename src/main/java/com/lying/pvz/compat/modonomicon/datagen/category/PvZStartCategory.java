package com.lying.pvz.compat.modonomicon.datagen.category;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.EnchantEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.EssenceEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.FestivalEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.PlantCardEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.PotionEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.SeedEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.ShopEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.SunStorageEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.ZenGardenEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.start.ZombieHouseEntry;

/**
 * "PVZ Start" 分类：开始冒险的各类系统说明。
 */
public class PvZStartCategory extends CategoryProvider {

    public PvZStartCategory(ModonomiconProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generateEntries() {
        // Modonomicon 只把分类名/描述写入默认语言，这里补上中文。
        this.add(this.lang("zh_cn"), this.context().categoryName(), "PVZ 开始");
        this.add(this.lang("zh_cn"), this.context().categoryDescription(),
                "相信通过阅读介绍，你大概已经了解了 PVZ 的世界是什么样子。接下来，让我们踏上旅程，开始冒险吧！");

        this.add(new EssenceEntry(this).generate('a'));
        this.add(new PlantCardEntry(this).generate('b'));
        this.add(new ZenGardenEntry(this).generate('c'));
        this.add(new ZombieHouseEntry(this).generate('d'));
        this.add(new ShopEntry(this).generate('e'));
        this.add(new SeedEntry(this).generate('f'));
        this.add(new EnchantEntry(this).generate('g'));
        this.add(new PotionEntry(this).generate('h'));
        this.add(new FestivalEntry(this).generate('i'));
        this.add(new SunStorageEntry(this).generate('j'));
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__a_____",
                "_b_c_d__",
                "e_f_g_h_",
                "__i_j___"
        };
    }

    @Override
    protected String categoryName() {
        return "PVZ Start";
    }

    @Override
    protected String categoryDescription() {
        return "I believe that through reading the introduction, you have probably understood what the world of PVZ is like. Next, let us embark on the journey and start the adventure!";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(PvZItemRegister.ORIGIN_SWORD);
    }

    @Override
    public String categoryId() {
        return "start";
    }
}
