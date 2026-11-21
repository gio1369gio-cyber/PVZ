package com.lying.pvz.compat.modonomicon.datagen;

import com.klikli_dev.modonomicon.api.datagen.NeoBookProvider;
import com.klikli_dev.modonomicon.api.datagen.research.ResearchCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Modonomicon datagen 的注册中心。
 *
 * <p>对 Modonomicon 的所有类引用都收拢在本类中，由 {@code DataGen} 在
 * {@code ModList.isLoaded("modonomicon")} 为 true 时才调用本类，从而保证软依赖安全。
 */
public final class ModonomiconDataGen {

    private ModonomiconDataGen() {
    }

    public static void register(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();

        PvZGuideBook guideBook = new PvZGuideBook();
        ResearchCache researchCache = new ResearchCache();

        // BookProvider 必须先加入：它在 run() 时把 en_us 缓存注入子 provider 并生成书籍，
        // 随后语言 provider 才能从缓存中取出完整翻译。
        gen.addProvider(true, NeoBookProvider.of(event, guideBook.enUs(), researchCache, guideBook));
        gen.addProvider(true, new PvZGuideBookLangProvider(output, "en_us", guideBook.enUs()));
        gen.addProvider(true, new PvZGuideBookLangProvider(output, "zh_cn", guideBook.zhCn()));
    }
}
