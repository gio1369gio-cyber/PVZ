package com.lying.pvz.compat.modonomicon.datagen;

import com.klikli_dev.modonomicon.api.datagen.BookContextHelper;
import com.klikli_dev.modonomicon.api.datagen.BookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.ConditionHelper;
import com.klikli_dev.modonomicon.api.datagen.LanguageProviderCache;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.lying.pvz.PvZMod;
import com.lying.pvz.compat.modonomicon.datagen.category.PvZBossCategory;
import com.lying.pvz.compat.modonomicon.datagen.category.PvZIntroCategory;
import com.lying.pvz.compat.modonomicon.datagen.category.PvZStartCategory;
import com.lying.pvz.compat.modonomicon.datagen.category.PvZWeaponCategory;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * PVZ 指南书（Modonomicon）。
 *
 * <p>由 Patchouli 旧书 {@code pvz_guide} 迁移而来。直接实现 {@link BookSubProvider}，
 * 手动管理 en_us / zh_cn 双语，datagen 时生成到
 * {@code data/pvz/modonomicon/books/pvz_guide/}。
 */
public class PvZGuideBook extends ModonomiconProviderBase implements BookSubProvider {

    public static final String ID = "pvz_guide";

    /** 主语言（en_us）缓存，datagen 完成后由语言 provider 输出 en_us.json。 */
    private static final LanguageProviderCache EN_US = new LanguageProviderCache("en_us");
    /** 中文缓存，datagen 完成后由语言 provider 输出 zh_cn.json。 */
    private static final LanguageProviderCache ZH_CN = new LanguageProviderCache("zh_cn");

    public PvZGuideBook() {
        super(PvZMod.MOD_ID, EN_US, Map.of("zh_cn", ZH_CN),
                new BookContextHelper(PvZMod.MOD_ID), new ConditionHelper());
    }

    @Override
    public void generate(BiConsumer<Identifier, BookModel> consumer, HolderLookup.Provider registries) {
        this.registries(registries);
        // 必须先初始化 book 上下文，否则所有翻译键会变成 "book.pvz.null.*"。
        this.context().book(ID);

        Identifier bookId = Identifier.fromNamespaceAndPath(PvZMod.MOD_ID, ID);
        BookModel book = BookModel.create(bookId, this.context().bookName())
                .withTooltip(this.context().bookTooltip())
                .withGenerateBookItem(false)
                .withCreativeTab(StringUtil.id("pvz_useful"));

        this.add(this.context().bookName(), "PVZ Guide");
        this.add(this.context().bookTooltip(), "A guide to Plants vs Zombies");
        this.add(this.lang("zh_cn"), this.context().bookName(), "PVZ 指南");
        this.add(this.lang("zh_cn"), this.context().bookTooltip(), "植物大战僵尸指南");

        book = book.withCategories(
                new PvZIntroCategory(this).generate(),
                new PvZStartCategory(this).generate(),
                new PvZWeaponCategory(this).generate(),
                new PvZBossCategory(this).generate());

        consumer.accept(bookId, book);
    }

    /** en_us 语言缓存，供语言 provider 输出。 */
    public LanguageProviderCache enUs() {
        return EN_US;
    }

    /** zh_cn 语言缓存，供语言 provider 输出。 */
    public LanguageProviderCache zhCn() {
        return ZH_CN;
    }
}
