package com.lying.pvz.common.invasion.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

/**
 * 数据包反序列化产物（不含 ID，ID 由 loader 的 map key 提供）。
 *
 * <p>{@code requireLevel} 为 {@link Optional}：缺省时由 {@link InvasionType} 从
 * spawns 的 {@code invasion_level} 取最小值；显式写出则覆盖。
 */
public record InvasionTypeData(
        boolean isAssist,
        int triggerChance,
        Optional<Integer> requireLevel,
        Optional<Identifier> bonus,
        TextColor displayColor,
        List<SpawnType> spawns,
        List<Wave> waves
) {

    public static final Codec<InvasionTypeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.optionalFieldOf("is_assist", false).forGetter(InvasionTypeData::isAssist),
            Codec.INT.optionalFieldOf("trigger_chance", 10).forGetter(InvasionTypeData::triggerChance),
            Codec.INT.optionalFieldOf("require_level").forGetter(InvasionTypeData::requireLevel),
            Identifier.CODEC.optionalFieldOf("bonus").forGetter(InvasionTypeData::bonus),
            TextColor.CODEC.optionalFieldOf("display_color", TextColor.WHITE).forGetter(InvasionTypeData::displayColor),
            SpawnType.CODEC.listOf().optionalFieldOf("spawns", List.of()).forGetter(InvasionTypeData::spawns),
            Wave.CODEC.listOf().optionalFieldOf("waves", List.of()).forGetter(InvasionTypeData::waves)
    ).apply(i, InvasionTypeData::new));
}