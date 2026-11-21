package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;
import net.minecraft.world.item.Items;

/**
 * "Player Resources" entry。
 */
public class PlayerResourceEntry extends BaseGuideEntry {

    public PlayerResourceEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("res1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Player Resources");
        this.pageText(GuideText.convert("Plants vs Zombies mod added some player resources, including TreeLevel, SunNum, EnergyNum and so on. If you didn't change the config file, you will see 2 strange gui on the corner of the screen in game, which are related to the player resources."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "玩家资源");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("植物大战僵尸模组添加了一些玩家资源，包括树等级、阳光数、能量数等。如果你没有改配置文件，游戏中会在屏幕角落看到 2 个与玩家资源相关的奇特 GUI。"));

        this.page("res2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Tree of Wisdom");
        this.pageText(GuideText.convert("Before explaining them, you should know The Tree of Wisdom first. The Tree of Wisdom is not a common tree, it's invisible and intangible and it rooted in player's soul and integrated with the player. In the world of PVZ, The Tree of Wisdom can help players collect and store resources such as sun and energy and fight side by side with players."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "智慧之树");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("在解释它们之前，你应该先了解智慧之树。智慧之树不是普通的树，它不可见、不可触碰，扎根于玩家的灵魂并与玩家融为一体。在 PVZ 世界中，智慧之树可以帮助玩家收集和储存阳光、能量等资源，并与玩家并肩作战。"));

        this.page("res3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Tree Level");
        this.pageText(GuideText.convert("Tree Level: It is independent of the default mc level, can also be called the PVZ Level. Usage: The higher the level is, the higher upper limit of SunNum and the more card you can use. Display: The upper left corner of the screen or command to view. Upgrade method: player or its plants kill zombies can gain some xp (Except for the zombies not belong to pvzmod)."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "树等级");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("树等级：它独立于默认 MC 等级，也可称为 PVZ 等级。用途：等级越高，阳光数上限越高，可使用的卡片越多。显示：屏幕左上角或指令查看。升级方式：玩家或其植物击杀僵尸可获得经验（不属于本模组的僵尸除外）。"));

        this.page("res4", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("SunNum");
        this.pageText(GuideText.convert("SunNum: The upper limit of SunNum will increase when TreeLevel increases. Usage: Using Summon Cards and some tools will consume SunNum. Display: The upper left corner of the screen or command to view. Obtaining method: collide with Sun entity or use Resource Collector or eat SunStorageSapling."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "阳光数");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("阳光数：阳光数上限会随树等级提升而增加。用途：使用召唤卡和某些工具会消耗阳光数。显示：屏幕左上角或指令查看。获取方式：与阳光实体碰撞、使用资源收集器或食用阳光储存树苗。"));

        this.page("res5", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("EnergyNum");
        this.pageText(GuideText.convert("EnergyNum: the upper limit of energy beans can be bought from Crazy Dave. Usage: player using sword enchanted with energy transfer right click plant will make plant in super mode and consume energy. Display: The lower left corner of the screen or command to view. Obtaining method: collide with Energy entity or use Resource Collector."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "能量数");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("能量数：能量豆上限可从疯狂戴夫处购买。用途：玩家用附有能量转移的剑右键植物，可让植物进入超级模式并消耗能量。显示：屏幕左下角或指令查看。获取方式：与能量实体碰撞或使用资源收集器。"));

        this.page("res6", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Money");
        this.pageText(GuideText.convert("Money: the general currency of shops, its upper limit is 1000000. Usage: purchase some Upgrade Plant Cards and tools. Display: The upper left corner of the screen or command. Obtaining method: collide with Coin entity or use Resource Collector."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "金钱");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("金钱：商店的通用货币，上限为 1000000。用途：购买一些植物升级卡和工具。显示：屏幕左上角或指令。获取方式：与金币实体碰撞或使用资源收集器。"));

        this.page("res7", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("GemNum");
        this.pageText(GuideText.convert("GemNum: the special currency of shops, its upper limit is 1000000. Usage: purchase some Plant Enjoy Cards and special tools. Display: The upper left corner of the screen or command. Obtaining method: collide with Jewel entity or use Resource Collector."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "宝石数");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("宝石数：商店的特殊货币，上限为 1000000。用途：购买一些植物体验卡和特殊工具。显示：屏幕左上角或指令。获取方式：与宝石实体碰撞或使用资源收集器。"));

        this.page("res8", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Tips");
        this.pageText(GuideText.convert("Tips: you can use command /playerstats to change or query your resource. If you can't see jewel num or money or tree level on the upper left corner, please press key (left <- or -> right) to toggle display."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "提示");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("提示：你可以使用 /playerstats 指令更改或查询你的资源。如果左上角看不到宝石数、金钱或树等级，请按方向键（左 ← 或右 →）切换显示。"));

        this.addZhEntryName("玩家资源");
    }

    @Override
    protected String entryName() {
        return "Player Resources";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.PLAYER_HEAD);
    }

    @Override
    protected String entryId() {
        return "player_resource";
    }
}
