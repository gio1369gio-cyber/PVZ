package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Sun Storage" entry。
 */
public class SunStorageEntry extends BaseGuideEntry {

    public SunStorageEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.spotlightPage("sunstorage1", PvZItemRegister.SUN_STORAGE_SAPLING,
                "Sun Storage Sapling", "阳光储存树苗",
                "$(byellow)Sun Storage Sapling$() is a kind of sapling that can $(bgreen)storage sun$() for players. Players can $(l)right click$() to eat it and $(bred)absorb its sun$(), and it will $(l)not consume$() after finish eating. Besides, it often used in machine at $()energy input$() slot.",
                "$(byellow)阳光储存树苗$()是一种可以为玩家$(bgreen)储存阳光$()的树苗。玩家可以$(l)右键点击$()食用它，$(bred)吸收其中的阳光$()，食用完毕后$(l)不会消耗$()。此外，它常用于机器的$()能量输入$()槽位。");

        this.craftingPage("sunstorage2", "pvz:food/small_sun_storage_sapling", null,
                "Low Storage Sapling", "低级储存树苗",
                "Basic storage sapling, its max storage amount is 1000.",
                "基础储存树苗，最大储存量为 1000。");

        this.craftingPage("sunstorage3", "pvz:food/sun_storage_sapling", null,
                "Storage Sapling", "储存树苗",
                "Normal storage sapling, its max storage amount is 3000.",
                "普通储存树苗，最大储存量为 3000。");

        this.craftingPage("sunstorage4", "pvz:food/large_sun_storage_sapling", null,
                "Large Storage Sapling", "大型储存树苗",
                "Large storage sapling, its max storage amount is 10000.",
                "大型储存树苗，最大储存量为 10000。");

        this.spotlightPage("sunstorage5", PvZBlockRegister.SUN_CONVERTER.get(),
                "Sun Converter", "阳光转换器",
                "$(byellow)Sun Converter$() can help Storage Saplings absorb sun, you just need to $(l)put saplings into the 9 slots$(), and the Converter will $(bred)automatically absorb nearby sun$() and transfer to saplings.",
                "$(byellow)阳光转换器$()可以帮助储存树苗吸收阳光，你只需$(l)将树苗放入 9 个槽位$()，转换器就会$(bred)自动吸收附近的阳光$()并传输给树苗。");

        this.addZhEntryName("阳光储存");
    }

    @Override
    protected String entryName() {
        return "Sun Storage";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZBlockRegister.SUN_CONVERTER.get());
    }

    @Override
    protected String entryId() {
        return "sun_storage";
    }
}
