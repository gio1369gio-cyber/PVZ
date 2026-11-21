package com.lying.pvz.compat.modonomicon.datagen.entry.start;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Essence" entry。原书含自定义 show_essence 页，按约定降级为图文页。
 */
public class EssenceEntry extends BaseGuideEntry {

    public EssenceEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.textPage("essence1", "Essence", "精华",
                "Everything has its $(l)origin$(), plants and zombies are no exception. Plants are derived from the special effects of $(l)plant seeds and special essences$(), and the special essences are derived from the $(#ff0000)Origin Essences$(). $(m)The source of zombies is currently unknown$(). The Origin Essence is $(l)the origin of most special essences$(). If you want to obtain it, you have to find the answer from the $(#ff0000)saplings$().",
                "万物皆有$(l)本源$()，植物和僵尸也不例外。植物源自$(l)植物种子和特殊精华$()的特殊效果，而特殊精华又源自$(#ff0000)源质精华$()。$(m)僵尸的来源目前尚不清楚$()。源质精华是$(l)大多数特殊精华的本源$()。如果你想获得它，就必须从$(#ff0000)树苗$()中寻找答案。");

        this.spotlightPage("essence2", PvZBlockRegister.ORIGIN_ORE.get(),
                "Origin Ore", "源质矿石",
                "During the growth of saplings, some saplings may absorb too much energy and overflow. Eventually, the saplings will fail to grow, and an $(#ff0000)Origin Ore$() full of energy will accumulate in the dirt below the saplings. Use a $(l)Diamond-level Pickaxe$() to dig the Origin Ore to obtain a small amount of $(#ff0000)Origin Essences$(). You can also find some ores on the ground.",
                "在树苗成长的过程中，有些树苗可能会吸收过多能量而溢出。最终，树苗会无法成长，并在树苗下方的泥土中积累出充满能量的$(#ff0000)源质矿石$()。使用$(l)钻石级镐$()挖掘源质矿石，可以获得少量$(#ff0000)源质精华$()。你也可以在地面上找到一些矿石。");

        this.spotlightPage("essence3", PvZItemRegister.ORIGIN_ESSENCE,
                "Origin Essence", "源质精华",
                "$(l)Origin Essence$() has a certain amount of $(l)radioactivity$(). It can radiate $(l)plant seeds$() and other items to make them alienate. However, a single original essence is not enough to obtain special essences, and they must be synthesized into $(#ff0000)Original Block$() with strong radiation.",
                "$(l)源质精华$()具有一定的$(l)放射性$()。它可以辐射$(l)植物种子$()和其他物品，使它们发生异变。然而，单个源质精华不足以获得特殊精华，必须将它们合成为具有强辐射的$(#ff0000)源质块$()。");

        this.craftingPage("essence4", "pvz:origin_block", null,
                "Origin Block", "源质块",
                "Its radiation has no effect on creatures, but it will $(l)radiate special blocks within 1 block around it as the corresponding Essence Ore$(). Due to the instability of its radiation, the time when the surrounding squares are changed is also different.",
                "它的辐射对生物没有影响，但会$(l)将周围 1 格内的特殊方块辐射为对应的精华矿石$()。由于其辐射不稳定，周围方块被改变的时间也各不相同。");

        // 原 show_essence 自定义页降级为图文页
        this.textPage("essence5", "Essence Ore Conversion", "精华矿石转化",
                "The result of all special blocks after radiation: Grass Block / Lunar Stone / Redstone Block / Granite / Snow Block / Andesite are radiated into Appease Ore / Light Ore / Explosion Ore / Defence Ore / Ice Ore / Enforce Ore.",
                "所有特殊方块被辐射后的结果：草方块 / 月光石 / 红石块 / 花岗岩 / 雪块 / 安山岩 会被辐射为 安抚矿石 / 光明矿石 / 爆炸矿石 / 防御矿石 / 寒冰矿石 / 强化矿石。");

        // 原 show_essence2 自定义页降级为图文页
        this.textPage("essence6", "Essence Ore Conversion II", "精华矿石转化 II",
                "Mycelium / Diorite / Soul Sand / Magma Block / Gravel / Sandstone are radiated into Toxic Ore / Assist Ore / Magic Ore / Flame Ore / Spear Ore / Arma Ore.",
                "菌丝 / 闪长岩 / 灵魂沙 / 岩浆块 / 砂砾 / 砂岩 会被辐射为 剧毒矿石 / 辅助矿石 / 魔法矿石 / 烈焰矿石 / 长矛矿石 / 武器矿石。");

        this.textPage("essence7", "Special Essences", "特殊精华",
                "There are $(l)14 special essences$() in total: $(l)appease, light, explosion, defense, ice, enforce, toxic, assist, magic, flame, spear, arma, electric and shadow$(). The first twelve are obtained by radiating the blocks listed above. $(#ff0000)Electric$() and $(#ff0000)Shadow$() have no radiating source block, so their essences can only be obtained from their own Essence Ores.",
                "目前共有$(l)14 种特殊精华$()：$(l)安抚、光明、爆炸、防御、寒冰、强化、剧毒、辅助、魔法、烈焰、长矛、武器、电击、暗影$()。前 12 种由上方列出的方块辐射获得；$(#ff0000)电击精华$()与$(#ff0000)暗影精华$()没有对应的辐射源方块，只能从各自的精华矿石中获得。");

        this.addZhEntryName("精华");
    }

    @Override
    protected String entryName() {
        return "Essence";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.ORIGIN_ESSENCE);
    }

    @Override
    protected String entryId() {
        return "essence";
    }
}
