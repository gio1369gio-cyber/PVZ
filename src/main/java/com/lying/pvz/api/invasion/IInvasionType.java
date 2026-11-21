package com.lying.pvz.api.invasion;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

/**
 * 入侵类型配置视图（数据包反序列化结果）。
 */
public interface IInvasionType {

    Identifier getID();

    MutableComponent getName();

    boolean isAssist();

    int getTriggerChance();

    int getRequireLevel();

    List<ISpawnType> getSpawns();

    int getTotalWaveCount();

    /**
     * 每波触发时刻（相对本次入侵开始 tick）。长度 = {@link #getTotalWaveCount()}。
     */
    int[] getWaveTimes();

    /**
     * 每波刷怪量（用于团队生成上限）。
     */
    int[] getWaveSpawnCounts();

    /**
     * 迷雾。无雾时返回 {@code null}。
     */
    IInvasionFog getFog();

    /**
     * 奖励资源 ID。无奖励时返回空。
     */
    Optional<Identifier> getBonus();

    /**
     * 显示颜色。不返回 {@code null}，无配置时默认白色。
     */
    TextColor getDisplayColor();
}