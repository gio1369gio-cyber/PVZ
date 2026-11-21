package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;
import net.minecraft.world.item.Items;

/**
 * "Difficulty" entry。
 */
public class DifficultyEntry extends BaseGuideEntry {

    public DifficultyEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("difficulty1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Difficulty");
        this.pageText(GuideText.convert("If you play in hard mode, there will have no Safe Day at the beginning. What's more, invasion and challenge will be much harder."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "难度");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("如果你在困难模式下游戏，开局将没有安全日。此外，入侵和挑战会难得多。"));

        this.addZhEntryName("难度");
    }

    @Override
    protected String entryName() {
        return "Difficulty";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.GOLDEN_APPLE);
    }

    @Override
    protected String entryId() {
        return "difficulty";
    }
}
