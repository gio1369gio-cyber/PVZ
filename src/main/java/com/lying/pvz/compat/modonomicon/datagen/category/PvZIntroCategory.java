package com.lying.pvz.compat.modonomicon.datagen.category;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.CardEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.DifficultyEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.GroupEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.InvadeEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.PlantsEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.PlayerResourceEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.intro.ZombiesEntry;

/**
 * "PVZ Intro" 分类：介绍模组的基础信息。
 */
public class PvZIntroCategory extends CategoryProvider {

    public PvZIntroCategory(ModonomiconProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generateEntries() {
        // Modonomicon 只把分类名/描述写入默认语言，这里补上中文。
        this.add(this.lang("zh_cn"), this.context().categoryName(), "PVZ 介绍");
        this.add(this.lang("zh_cn"), this.context().categoryDescription(),
                "这里将介绍《植物大战僵尸》模组的一些基本信息，帮助玩家更好地理解这个模组。");

        this.add(new PlayerResourceEntry(this).generate('a'));
        this.add(new ZombiesEntry(this).generate('b'));
        this.add(new PlantsEntry(this).generate('c'));
        this.add(new GroupEntry(this).generate('d'));
        this.add(new CardEntry(this).generate('e'));
        this.add(new InvadeEntry(this).generate('f'));
        this.add(new DifficultyEntry(this).generate('g'));
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "___a____",
                "__b_c___",
                "d_e_f_g_"
        };
    }

    @Override
    protected String categoryName() {
        return "PVZ Intro";
    }

    @Override
    protected String categoryDescription() {
        return "Here will introduce some basic information of the Plants vs Zombies mod, so that players can better understand this mod.";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(PvZBlockRegister.NUT_SAPLING);
    }

    @Override
    public String categoryId() {
        return "intro";
    }
}
