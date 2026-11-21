package com.lying.pvz.compat.modonomicon.datagen;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * 输出 PVZ 指南书在 datagen 期间收集到的翻译。
 *
 * <p>使用独立的资源命名空间 {@code pvz_guide}，避免与主模组已有的
 * {@code assets/pvz/lang/en_us.json}（由 {@code PvZLangProvider} 生成）以及手写的
 * {@code assets/pvz/lang/zh_cn.json} 发生冲突。Minecraft 会加载所有命名空间的
 * lang 文件并合并翻译键，因此书籍键（{@code book.pvz.*}/{@code entry.pvz.*}/...）
 * 位于哪个命名空间并不影响解析。
 */
public class PvZGuideBookLangProvider extends AbstractModonomiconLanguageProvider {

    /** 书籍语言文件专用的资源命名空间。 */
    private static final String NAMESPACE = "pvz_guide";

    public PvZGuideBookLangProvider(PackOutput output, String locale, ModonomiconLanguageProvider cache) {
        super(output, NAMESPACE, locale, cache);
    }

    @Override
    protected void addTranslations() {
        // 全部翻译由构造时传入的 cache 在 run() 中自动合并。
    }
}
