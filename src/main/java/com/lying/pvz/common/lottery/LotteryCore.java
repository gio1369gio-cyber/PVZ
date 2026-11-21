package com.lying.pvz.common.lottery;

import com.lying.pvz.api.lottery.LotteryKind;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * 奖池 JSON 的前 4 个字段。拆出来是为了绕开 {@code RecordCodecBuilder} 的 {@code ap4} 上限。
 */
public record LotteryCore(
        LotteryKind kind,
        int sunCost,
        int tradePrice,
        int tradeWeight
) {

    public static final MapCodec<LotteryCore> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.xmap(s -> LotteryKind.valueOf(s.toUpperCase()), k -> k.name().toLowerCase())
                    .optionalFieldOf("type", LotteryKind.NORMAL).forGetter(LotteryCore::kind),
            Codec.INT.optionalFieldOf("sun_cost", 25).forGetter(LotteryCore::sunCost),
            Codec.INT.optionalFieldOf("trade_price", 2000).forGetter(LotteryCore::tradePrice),
            Codec.INT.optionalFieldOf("trade_weight", 100).forGetter(LotteryCore::tradeWeight)
    ).apply(instance, LotteryCore::new));
}