package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;
import net.minecraft.world.item.Items;

/**
 * "Group" entry。
 */
public class GroupEntry extends BaseGuideEntry {

    public GroupEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("group1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Group");
        this.pageText(GuideText.convert("This world is divided into 5 groups, respectively, other hostile(-2), zombies(-1), neutral(0), plants(1), other guards(2). Players are initially set as plants, and this option can be changed in the config file. Also players can change groups through the /playerstats command."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "阵营");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("这个世界分为 5 个阵营，分别为：其他敌对(-2)、僵尸(-1)、中立(0)、植物(1)、其他守卫(2)。玩家初始设定为植物，可在配置文件中更改。玩家也可以通过 /playerstats 指令更改阵营。"));

        this.page("group2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Monsters and Guards");
        this.pageText(GuideText.convert("Monsters other than zombies are in other hostile groups, such as skeletons and creepers. Guards other than plants are in other guards groups, such as iron golems and villagers. Charmed plants will be in the zombie group, and Charmed zombies will be in the plant group."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "怪物与守卫");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("除僵尸外的怪物属于其他敌对阵营，如骷髅和苦力怕。除植物外的守卫属于其他守卫阵营，如铁傀儡和村民。被魅惑的植物会属于僵尸阵营，被魅惑的僵尸会属于植物阵营。"));

        this.page("group3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Relations");
        this.pageText(GuideText.convert("The zombie group will actively attack creatures in the plant and other guard groups, and will passively attack other hostile and neutral creatures. The plant group will actively attack creatures from the zombie and other hostile groups, while passively attacking other guards and neutral creatures."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "敌对关系");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("僵尸阵营会主动攻击植物和其他守卫阵营的生物，并会被动攻击其他敌对和中立生物。植物阵营会主动攻击僵尸和其他敌对阵营的生物，同时被动攻击其他守卫和中立生物。"));

        this.addZhEntryName("阵营");
    }

    @Override
    protected String entryName() {
        return "Group";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.IRON_SWORD);
    }

    @Override
    protected String entryId() {
        return "group";
    }
}
