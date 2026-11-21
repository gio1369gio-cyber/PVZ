package com.lying.pvz.compat.modonomicon.datagen.entry.boss;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Edgar Robot" entry。
 */
public class EdgarEntry extends BaseGuideEntry {

    public EdgarEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("edgar1", "Edgar Robot", "埃德加机器人",
                "$(bred)Edgar-090505 and Edgar-09017$() are robots created by Dr.Edgar, they can be summoned by challenge envelope.",
                "$(bred)埃德加-090505 和埃德加-09017$()是埃德加博士创造的机器人，可以通过挑战信封召唤。");

        this.textPage("edgar2", "Edgar's Attack", "埃德加的攻击",
                "$(bred)Edgar Robots$()'s common attack is its $(byellow)ice and fire balls$(). $(baqua)Ice balls$() can only be destroyed by $(bred)jalapeno$(), and $(bred)fire balls$() can only be destroyed by cold $(baqua)ice shroom$(). In addition, it will summon almost $(l)all kinds of invading zombies$(), especially $(bred)bungee zombies$(), so please be careful.",
                "$(bred)埃德加机器人$()的常用攻击是它的$(byellow)冰球和火球$()。$(baqua)冰球$()只能被$(bred)火爆辣椒$()摧毁，而$(bred)火球$()只能被寒冷的$(baqua)寒冰菇$()摧毁。此外，它几乎会召唤$(l)所有种类的入侵僵尸$()，尤其是$(bred)蹦极僵尸$()，所以请务必小心。");

        this.addZhEntryName("埃德加机器人");
    }

    @Override
    protected String entryName() {
        return "Edgar Robot";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.EDGAR_DOLL);
    }

    @Override
    protected String entryId() {
        return "edgar";
    }
}
