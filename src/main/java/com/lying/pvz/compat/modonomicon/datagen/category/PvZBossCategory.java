package com.lying.pvz.compat.modonomicon.datagen.category;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.boss.EdgarEntry;

/**
 * "Boss" 分类：Boss 挑战。
 */
public class PvZBossCategory extends CategoryProvider {

    public PvZBossCategory(ModonomiconProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generateEntries() {
        // Modonomicon 只把分类名/描述写入默认语言，这里补上中文。
        this.add(this.lang("zh_cn"), this.context().categoryName(), "Boss");
        this.add(this.lang("zh_cn"), this.context().categoryDescription(), "Boss 挑战。");

        this.add(new EdgarEntry(this).generate('a'));
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "__a__"
        };
    }

    @Override
    protected String categoryName() {
        return "Boss";
    }

    @Override
    protected String categoryDescription() {
        return "Boss challenge.";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(PvZItemRegister.EDGAR_DOLL);
    }

    @Override
    public String categoryId() {
        return "boss";
    }
}
