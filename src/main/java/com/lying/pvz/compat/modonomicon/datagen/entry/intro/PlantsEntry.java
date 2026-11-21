package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Plants" entry。
 */
public class PlantsEntry extends BaseGuideEntry {

    public PlantsEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("plants1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Plants");
        this.pageText(GuideText.convert("Plants are creatures that are hostile to zombies and all kinds of monsters. Different plants have different usages. Some can produce sun, such as SunFlowers. Some can attack enemies, such as PeaShooters. Some can defend, such as NutWalls."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "植物");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("植物是对僵尸和各种怪物有敌意的生物。不同植物有不同用途。有的能产生阳光，如向日葵；有的能攻击敌人，如豌豆射手；有的能防御，如坚果墙。"));

        this.page("plants2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Habitat");
        this.pageText(GuideText.convert("Plants are relatively fragile. Terrestrial plants can only survive on grass blocks and Water lilies and golden tiles and flower pots, while aquatic plants can only survive in water."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "栖息环境");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("植物相对脆弱。陆生植物只能在草方块、睡莲、金地砖和花盆上生存，而水生植物只能在水中生存。"));

        this.page("plants3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Quality");
        this.pageText(GuideText.convert("Both plants and zombies have qualities. The qualities are gray, white, green, blue, purple, gold, red, black and mega, which become higher from left to right. The higher the quality, the stronger the corresponding plants or zombies. More experience gained from killing high-quality zombies. For plants, the higher the quality, the more difficult it is to get."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "品质");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("植物和僵尸都有品质。品质依次为灰、白、绿、蓝、紫、金、红、黑、超级，从左到右越来越高。品质越高，对应的植物或僵尸越强。击杀高品质僵尸获得的经验更多。对植物而言，品质越高越难获得。"));

        this.addZhEntryName("植物");
    }

    @Override
    protected String entryName() {
        return "Plants";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.PEA);
    }

    @Override
    protected String entryId() {
        return "plants";
    }
}
