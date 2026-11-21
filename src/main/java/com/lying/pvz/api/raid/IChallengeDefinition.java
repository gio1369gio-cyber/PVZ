package com.lying.pvz.api.raid;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * 挑战组件的配置数据（由 JSON 反序列化得到）。
 */
public interface IChallengeDefinition {

    String type();

    /**
     * 本定义在注册表中的 key。由 loader 在注册前注入。
     */
    Identifier getDefinitionId();

    /**
     * 挑战名称的默认翻译键：{@code challenge.<namespace>.<path>.name}。
     */
    default MutableComponent getName() {
        final Identifier id = this.getDefinitionId();
        return Component.translatable(
                "challenge." + id.getNamespace() + "." + id.getPath() + ".name");
    }

    int getPrepareCD(int wavePos);

    int getLastDuration(int wavePos);

    int getTotalWaveCount();

    int getWinTick();

    int getLossTick();

    int getRecommendLevel();

    boolean canTrade();

    int getTradeWeight();

    int getTradePrice();

    boolean isWaveFinish(int wavePos, int spawnPos);

    List<ISpawnDefinition> getSpawns(int wavePos);

    List<IWaveDefinition> getWaves();

    IPlacementDefinition getPlacement(int wavePos);

    boolean hasTag(String tag);

    boolean isSuitableDimension(ResourceKey<Level> type);

    boolean showRoundTitle();

    boolean shouldCloseToCenter();

    List<String> getAuthors();

    List<IRewardDefinition> getRewards();

    Component getTitle();

    Component getWinTitle();

    Component getLossTitle();

    BossEvent.BossBarColor getBarColor();

    SoundEvent getPrepareSound();

    SoundEvent getStartWaveSound();

    SoundEvent getWinSound();

    SoundEvent getLossSound();

    List<Pair<MutableComponent, Integer>> getDescription();

    default List<String> getTags() {
        return List.of();
    }

    default List<Identifier> getDimensions() {
        return List.of();
    }
}