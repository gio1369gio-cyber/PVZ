package com.lying.pvz.common.lottery;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

/**
 * 整个奖池 JSON 的结构。{@code core} 的字段通过 {@link LotteryCore#MAP_CODEC} 展开到同一层。
 */
public record LotteryJson(
        LotteryCore core,
        Optional<Integer> slotCount,
        List<SlotJson> items
) {

    public static final Codec<LotteryJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LotteryCore.MAP_CODEC.forGetter(LotteryJson::core),
            Codec.INT.optionalFieldOf("slot_count").forGetter(LotteryJson::slotCount),
            SlotJson.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(LotteryJson::items)
    ).apply(instance, LotteryJson::new));
}