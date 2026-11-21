package com.lying.pvz.compat.modonomicon.datagen.entry;

import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookCraftingRecipePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.modonomicon.client.gui.book.theme.GuiSprite;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import net.minecraft.world.level.ItemLike;

/**
 * PVZ 指南书 entry 的公共基类。
 *
 * <p>统一提供默认背景，封装中文语言注入，并提供图文页 / 展示页 / 合成页的
 * 双语快捷生成方法。子类只需实现 {@code entryId/entryName/entryIcon/generatePages}。
 */
public abstract class BaseGuideEntry extends EntryProvider {

    protected BaseGuideEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected GuiSprite entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected String entryDescription() {
        return "";
    }

    /**
     * 向 zh_cn 语言表写入一条翻译。
     */
    protected void zh(String key, String text) {
        this.add(this.lang("zh_cn"), key, text);
    }

    /**
     * 注入中文的 entry 名称与描述。子类在 {@code generatePages} 末尾调用。
     */
    protected void addZhEntryName(String name) {
        this.zh(this.context().entryName(), name);
    }

    protected void addZhEntryDescription(String desc) {
        if (desc != null && !desc.isEmpty()) {
            this.zh(this.context().entryDescription(), desc);
        }
    }

    /**
     * 生成一个双语纯文本页。
     */
    protected void textPage(String id, String enTitle, String zhTitle, String enText, String zhText) {
        this.page(id, () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle(GuideText.convert(enTitle));
        this.pageText(GuideText.convert(enText));
        this.zh(this.context().pageTitle(), GuideText.convert(zhTitle));
        this.zh(this.context().pageText(), GuideText.convert(zhText));
    }

    /**
     * 生成一个双语物品展示页（spotlight）。
     */
    protected void spotlightPage(String id, ItemLike item, String enTitle, String zhTitle, String enText, String zhText) {
        this.page(id, () -> BookSpotlightPageModel.create()
                .withItem(item)
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle(GuideText.convert(enTitle));
        this.pageText(GuideText.convert(enText));
        this.zh(this.context().pageTitle(), GuideText.convert(zhTitle));
        this.zh(this.context().pageText(), GuideText.convert(zhText));
    }

    /**
     * 生成一个双语合成页。{@code recipe2} 为 null/空时只显示一个配方。
     * 文本可为空（此时不设置文本键）。
     */
    protected void craftingPage(String id, String recipe1, String recipe2,
                                String enTitle, String zhTitle, String enText, String zhText) {
        final boolean hasText = enText != null && !enText.isEmpty();
        this.page(id, () -> {
            BookCraftingRecipePageModel model = BookCraftingRecipePageModel.create()
                    .withRecipeId1(recipe1)
                    .withTitle1(this.context().pageTitle());
            if (recipe2 != null && !recipe2.isEmpty()) {
                model = model.withRecipeId2(recipe2);
            }
            if (hasText) {
                model = model.withText(this.context().pageText());
            }
            return model;
        });
        this.pageTitle(GuideText.convert(enTitle));
        this.zh(this.context().pageTitle(), GuideText.convert(zhTitle));
        if (hasText) {
            this.pageText(GuideText.convert(enText));
            this.zh(this.context().pageText(), GuideText.convert(zhText));
        }
    }
}
