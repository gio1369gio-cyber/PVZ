package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Shop" entry。
 */
public class ShopEntry extends BaseGuideEntry {

    public ShopEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("shop1", "Shop", "商店",
                "$(bred)Dave Villa$() is a structure generated in $(l)Overworld plains$(), where a $(#ff0000)Crazy Dave and Penny$() will be spawned. At the same time, the lawn of the villa may spawn some plants. Players can trade with Dave with $(#ff0000)Money$(), and Dave currently sells $(l)Energy limits and almanac and some Upgrade Cards$().$(br)Penny currently does not have AI and will be updated soon.",
                "$(bred)戴夫别墅$()是一种生成在$(l)主世界平原$()的结构，那里会生成$(#ff0000)疯狂戴夫和潘妮$()。同时，别墅的草坪上可能会生成一些植物。玩家可以用$(#ff0000)金钱$()与戴夫交易，戴夫目前出售$(l)能量上限、图鉴和一些升级卡$()。$(br)潘妮目前没有 AI，将会尽快更新。");

        this.textPage("shop2", "Penny the Car", "潘妮小车",
                "$(bred)Dave Villa$() has a car named [Penny]. If you hold $(bred)Car Key$() and right click to trade with $(bblue)gem$(), you can get $(bred)Lawn Mower$() and many enjoy cards.",
                "$(bred)戴夫别墅$()有一辆名为[潘妮]的车，如果你手持$(bred)车钥匙$()并右键点击用$(bblue)宝石$()交易，就可以获得$(bred)割草机$()和许多享受卡。");

        this.textPage("shop3", "Sun Temple", "阳光神殿",
                "$(byellow)Sun Temple$() is a structure generated in $(l)Desert Sky$(), where a $(#ff0000)Sun Dave$() will spawn. Players can trade it with sun.",
                "$(byellow)阳光神殿$()是一种生成在$(l)沙漠天空$()的结构，那里会生成$(#ff0000)阳光戴夫$()。玩家可以用阳光与之交易。");

        this.textPage("shop4", "Trade Refresh", "交易刷新",
                "The trade list will update every 20 minutes.",
                "交易列表每 20 分钟更新一次。");

        this.addZhEntryName("商店");
    }

    @Override
    protected String entryName() {
        return "Shop";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Items.BRICKS);
    }

    @Override
    protected String entryId() {
        return "shop";
    }
}
