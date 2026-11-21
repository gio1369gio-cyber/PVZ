package com.lying.pvz.common.invasion.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** 波次配置：相对入侵开始 tick 的触发时刻 + 该波刷怪量。 */
public record Wave(int tick, int count) {

    public static final Codec<Wave> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.optionalFieldOf("tick", 0).forGetter(Wave::tick),
            Codec.INT.optionalFieldOf("count", 10).forGetter(Wave::count)
    ).apply(i, Wave::new));
}