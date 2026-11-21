package com.lying.pvz.compat.modonomicon.datagen.category;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.weapon.PeaGunEntry;
import com.lying.pvz.compat.modonomicon.datagen.entry.weapon.ResourceCollectorEntry;

/**
 * "Plant Weapons and Tools" 分类。
 */
public class PvZWeaponCategory extends CategoryProvider {

    public PvZWeaponCategory(ModonomiconProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generateEntries() {
        // Modonomicon 只把分类名/描述写入默认语言，这里补上中文。
        this.add(this.lang("zh_cn"), this.context().categoryName(), "植物武器与工具");
        this.add(this.lang("zh_cn"), this.context().categoryDescription(),
                "植物与玩家共同成长。玩家训练植物使其升级。植物也会给予玩家使用植物武器与工具的能力。植物等级越高，植物武器与工具就越强大。");

        this.add(new PeaGunEntry(this).generate('a'));
        this.add(new ResourceCollectorEntry(this).generate('b'));
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "_a_b_"
        };
    }

    @Override
    protected String categoryName() {
        return "Plant Weapons and Tools";
    }

    @Override
    protected String categoryDescription() {
        return "Plants and Players progress together. Players train plants to upgrade. Plants will also give players the ability to use Plant weapons and tools. And the higher the plant level, the stronger the plant weapons and tools.";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA_GUN);
    }

    @Override
    public String categoryId() {
        return "weapon";
    }
}
