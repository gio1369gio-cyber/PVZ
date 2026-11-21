package com.lying.pvz.common.impl.challenge;

import com.lying.pvz.api.raid.*;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.challenge.placement.CenterPlacement;
import com.lying.pvz.utils.enums.Colors;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultChallengeDefinition implements IChallengeDefinition {

    public static final String TYPE = "default";

    /* ---------------- 内部 record ---------------- */

    public record Display(
            Component title,
            Component winTitle,
            Component lossTitle,
            BossEvent.BossBarColor barColor,
            List<Pair<MutableComponent, Integer>> messages
    ) {
        private static final Codec<Pair<MutableComponent, Integer>> MESSAGE_CODEC = RecordCodecBuilder.create(i -> i.group(
                ComponentSerialization.CODEC
                        .xmap(Component::copy, c -> c)
                        .fieldOf("text")
                        .forGetter(Pair::getFirst),
                Codec.INT
                        .optionalFieldOf("color", 0xFFFFFF)
                        .forGetter(Pair::getSecond)
        ).apply(i, Pair::of));

        static final MapCodec<Display> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                ComponentSerialization.CODEC
                        .optionalFieldOf("title", Component.translatable("challenge.pvz.title"))
                        .forGetter(Display::title),
                ComponentSerialization.CODEC
                        .optionalFieldOf("win_title", Component.translatable("challenge.pvz.win_title"))
                        .forGetter(Display::winTitle),
                ComponentSerialization.CODEC
                        .optionalFieldOf("loss_title", Component.translatable("challenge.pvz.loss_title"))
                        .forGetter(Display::lossTitle),
                BossEvent.BossBarColor.CODEC
                        .optionalFieldOf("bar_color", BossEvent.BossBarColor.RED)
                        .forGetter(Display::barColor),
                MESSAGE_CODEC.listOf()
                        .optionalFieldOf("messages", List.of())
                        .forGetter(Display::messages)
        ).apply(i, Display::new));
    }

    public record Trade(boolean canTrade, int price, int weight) {
        static final MapCodec<Trade> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.BOOL.optionalFieldOf("can_trade", true).forGetter(Trade::canTrade),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("trade_price", 100).forGetter(Trade::price),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("trade_weight", 100).forGetter(Trade::weight)
        ).apply(i, Trade::new));
    }

    public record Sounds(SoundEvent prepare, SoundEvent startWave, SoundEvent win, SoundEvent loss) {
        static final MapCodec<Sounds> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                SoundEvent.DIRECT_CODEC.optionalFieldOf("prepare").forGetter(s -> Optional.ofNullable(s.prepare())),
                SoundEvent.DIRECT_CODEC.optionalFieldOf("start_wave").forGetter(s -> Optional.ofNullable(s.startWave())),
                SoundEvent.DIRECT_CODEC.optionalFieldOf("win").forGetter(s -> Optional.ofNullable(s.win())),
                SoundEvent.DIRECT_CODEC.optionalFieldOf("loss").forGetter(s -> Optional.ofNullable(s.loss()))
        ).apply(i, (p, sw, w, l) -> new Sounds(p.orElse(null), sw.orElse(null), w.orElse(null), l.orElse(null))));
    }

    private record Config(
            List<String> authors,
            List<String> tags,
            List<Identifier> dimensions,
            int winTick,
            int lossTick,
            int recommendLevel,
            boolean showRound,
            boolean closeToCenter,
            Trade trade,
            IPlacementDefinition placement,
            List<IWaveDefinition> waves,
            List<IRewardDefinition> rewards,
            Display display,
            Sounds sounds
    ) {
        static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.STRING.listOf().optionalFieldOf("authors", List.of()).forGetter(Config::authors),
                Codec.STRING.listOf().optionalFieldOf("tags", List.of()).forGetter(Config::tags),
                Identifier.CODEC.listOf().optionalFieldOf("dimensions", List.of()).forGetter(Config::dimensions),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("win_tick", 400).forGetter(Config::winTick),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("loss_tick", 100).forGetter(Config::lossTick),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("recommend_level", 1).forGetter(Config::recommendLevel),
                Codec.BOOL.optionalFieldOf("show_round", true).forGetter(Config::showRound),
                Codec.BOOL.optionalFieldOf("close_to_center", true).forGetter(Config::closeToCenter),
                Trade.CODEC.forGetter(Config::trade),
                ChallengeManager.placementCodec()
                        .optionalFieldOf("placement", new CenterPlacement(1, true))
                        .forGetter(Config::placement),
                ChallengeManager.waveCodec()
                        .listOf()
                        .validate(Config::requireNonEmptyWaves)
                        .fieldOf("waves")
                        .forGetter(Config::waves),
                ChallengeManager.rewardCodec()
                        .listOf()
                        .optionalFieldOf("rewards", List.of())
                        .forGetter(Config::rewards),
                Display.CODEC.forGetter(Config::display),
                Sounds.CODEC.forGetter(Config::sounds)
        ).apply(i, Config::new));

        private static DataResult<List<IWaveDefinition>> requireNonEmptyWaves(List<IWaveDefinition> list) {
            return list.isEmpty()
                    ? DataResult.error(() -> "Challenge must have at least one wave")
                    : DataResult.success(list);
        }
    }

    public static final MapCodec<DefaultChallengeDefinition> CODEC = Config.CODEC.xmap(
            DefaultChallengeDefinition::new,
            DefaultChallengeDefinition::toConfig
    );

    /* ---------------- 字段 ---------------- */

    private Identifier definitionId;
    private final List<String> authors;
    private final List<String> tags;
    private final List<Identifier> dimensions;
    private final int winTick;
    private final int lossTick;
    private final int recommendLevel;
    private final boolean showRound;
    private final boolean closeToCenter;
    private final Trade trade;
    private final IPlacementDefinition placement;
    private final List<IWaveDefinition> waves;
    private final List<IRewardDefinition> rewards;
    private final Display display;
    private final Sounds sounds;

    private DefaultChallengeDefinition(Config c) {
        this.authors = c.authors();
        this.tags = c.tags();
        this.dimensions = c.dimensions();
        this.winTick = c.winTick();
        this.lossTick = c.lossTick();
        this.recommendLevel = c.recommendLevel();
        this.showRound = c.showRound();
        this.closeToCenter = c.closeToCenter();
        this.trade = c.trade();
        this.placement = c.placement();
        this.waves = c.waves();
        this.rewards = c.rewards();
        this.display = c.display();
        this.sounds = c.sounds();
    }

    private Config toConfig() {
        return new Config(
                this.authors, this.tags, this.dimensions,
                this.winTick, this.lossTick, this.recommendLevel,
                this.showRound, this.closeToCenter,
                this.trade, this.placement, this.waves, this.rewards,
                this.display, this.sounds
        );
    }

    /**
     * 由 loader 在注册前调用一次。第三方不应调用。
     */
    public void initId(Identifier id) {
        this.definitionId = id;
    }

    /* ---------------- IChallengeDefinition ---------------- */

    @Override public String type() { return TYPE; }
    @Override public Identifier getDefinitionId() { return this.definitionId; }

    @Override
    public int getPrepareCD(int wavePos) {
        return this.waves.get(this.wavePos(wavePos)).getPrepareCD();
    }

    @Override
    public int getLastDuration(int wavePos) {
        return this.waves.get(this.wavePos(wavePos)).getLastDuration();
    }

    @Override public int getTotalWaveCount() { return this.waves.size(); }
    @Override public int getWinTick() { return this.winTick; }
    @Override public int getLossTick() { return this.lossTick; }
    @Override public int getRecommendLevel() { return this.recommendLevel; }
    @Override public boolean canTrade() { return this.trade.canTrade(); }
    @Override public int getTradeWeight() { return this.trade.weight(); }
    @Override public int getTradePrice() { return this.trade.price(); }

    @Override
    public boolean isWaveFinish(int wavePos, int spawnPos) {
        return spawnPos >= this.waves.get(this.wavePos(wavePos)).getSpawns().size();
    }

    @Override
    public List<ISpawnDefinition> getSpawns(int wavePos) {
        return this.waves.get(this.wavePos(wavePos)).getSpawns();
    }

    @Override public List<IWaveDefinition> getWaves() { return this.waves; }

    @Override
    public IPlacementDefinition getPlacement(int wavePos) {
        final IPlacementDefinition p = this.waves.get(this.wavePos(wavePos)).getPlacement();
        return p != null ? p : this.placement;
    }

    @Override public boolean hasTag(String tag) { return this.tags.contains(tag); }

    @Override
    public boolean isSuitableDimension(ResourceKey<Level> type) {
        return this.dimensions.isEmpty() || this.dimensions.contains(type.identifier());
    }

    @Override public boolean showRoundTitle() { return this.showRound; }
    @Override public boolean shouldCloseToCenter() { return this.closeToCenter; }
    @Override public List<String> getAuthors() { return this.authors; }
    @Override public List<IRewardDefinition> getRewards() { return this.rewards; }
    @Override public Component getTitle() { return this.display.title(); }
    @Override public Component getWinTitle() { return this.display.winTitle(); }
    @Override public Component getLossTitle() { return this.display.lossTitle(); }
    @Override public BossEvent.BossBarColor getBarColor() { return this.display.barColor(); }
    @Override public SoundEvent getPrepareSound() { return this.sounds.prepare(); }
    @Override public SoundEvent getStartWaveSound() { return this.sounds.startWave(); }
    @Override public SoundEvent getWinSound() { return this.sounds.win(); }
    @Override public SoundEvent getLossSound() { return this.sounds.loss(); }
    @Override
    public List<Pair<MutableComponent, Integer>> getDescription() {
        if (!this.display.messages().isEmpty()) {
            return this.display.messages();
        }
        // JSON 未提供 messages 时回退到翻译键 challenge.<ns>.<path>.msg1..6。
        final String prefix = "challenge." + this.definitionId.getNamespace()
                + "." + this.definitionId.getPath() + ".msg";
        final List<Pair<MutableComponent, Integer>> messages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            messages.add(Pair.of(Component.translatable(prefix + (i + 1)), Colors.BLACK));
        }
        return messages;
    }

    private int wavePos(int pos) {
        return Mth.clamp(pos, 0, Math.max(0, this.waves.size() - 1));
    }
}