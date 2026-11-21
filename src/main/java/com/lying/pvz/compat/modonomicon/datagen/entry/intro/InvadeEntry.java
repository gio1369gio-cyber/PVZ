package com.lying.pvz.compat.modonomicon.datagen.entry.intro;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.GuideText;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Zombie Invade" entry。
 */
public class InvadeEntry extends BaseGuideEntry {

    public InvadeEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("invade1", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Zombie Invade");
        this.pageText(GuideText.convert("Maybe you haven't encountered zombies yet, but it doesn't mean that there are no zombies. Strange help didn't lie to you. From the beginning of the creation of the world, there is a SafeDayLength. The default SafeDayLength is 4 game days, after which invasion will occur every 2 day in OverWorld. they can be modified in the config file."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "僵尸入侵");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("也许你还没遇到僵尸，但这不代表没有僵尸。奇怪的帮助没有骗你。从世界创建之初就存在安全日长度。默认安全日长度为 4 个游戏日，之后主世界每 2 天会发生一次入侵。可以在配置文件中修改。"));

        this.page("invade2", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Invasion Events");
        this.pageText(GuideText.convert("Invasions are based on days. If there is a sign <Zombies are coming> in the morning, it means that the invasion has begun and it will continue until the end of the day. Zombie Invasion contains various of events, Events can divided into two types (Spawn Events, Assist Events)."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "入侵事件");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("入侵以天为周期。如果早上出现「僵尸来袭」的标志，说明入侵开始，并持续到当天结束。僵尸入侵包含各种事件，事件可分为两类（生成事件、辅助事件）。"));

        this.page("invade3", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Spawn Events");
        this.pageText(GuideText.convert("Overworld will spawn various of zombies in that day. There is only one Spawn Events in each Invasion day, it determines what kind of zombies will spawn in Overworld. there may be also some Assist events, which can enhance zombies or change the environment."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "生成事件");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("当天主世界会生成各种僵尸。每个入侵日只有一个生成事件，它决定主世界生成何种僵尸。可能还有一些辅助事件，可以强化僵尸或改变环境。"));

        this.page("invade4", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Waves");
        this.pageText(GuideText.convert("when invasion start, you will see a invasion progress in the lower right corner, it will tip you when will wave start. The higher the TreeLevel is, the more waves player may encounter. You will see <A huge wave of zombies are coming> when wave is spawned. some zombie invasion teams will spawn surround players in 48 blocks, If you dislike the waves, you can turn off in config file."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "波次");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("入侵开始时，你会在右下角看到入侵进度，提示你波次何时开始。树等级越高，玩家可能遇到的波次越多。刷怪波出现时你会看到「一大波僵尸来袭」。部分僵尸入侵队伍会在玩家周围 48 格内生成。如果不喜欢波次，可在配置文件中关闭。"));

        this.page("invade5", () -> BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText()));
        this.pageTitle("Missions");
        this.pageText(GuideText.convert("Every Invasion day will randomly give mission to players, including kill invaders, collect sun, instant kill. All Mission have several stage, finish each of stage will get rewards like Money, Jewel, Lottery Chance."));
        this.add(this.lang("zh_cn"), this.context().pageTitle(), "任务");
        this.add(this.lang("zh_cn"), this.context().pageText(), GuideText.convert("每个入侵日都会随机给玩家任务，包括击杀入侵者、收集阳光、秒杀等。所有任务都有多个阶段，完成每个阶段会获得金钱、宝石、抽奖机会等奖励。"));

        this.addZhEntryName("僵尸入侵");
    }

    @Override
    protected String entryName() {
        return "Zombie Invade";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.ZOMBIE_FLAG);
    }

    @Override
    protected String entryId() {
        return "invade";
    }
}
