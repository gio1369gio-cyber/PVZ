package com.lying.pvz.api.transaction;

import com.lying.pvz.common.transaction.TransactionCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GoodType(
        GoodKind kind,
        int price,
        int weight,
        int limit,
        boolean must
) {

    /** 通用字段。与 kind 的 dispatch 字段合并成 MapCodec。 */
    private record Extra(int price, int weight, int limit, boolean must) {}

    private static final MapCodec<Extra> EXTRA_MC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.optionalFieldOf("price", 1000).forGetter(Extra::price),
            Codec.INT.optionalFieldOf("weight", 100).forGetter(Extra::weight),
            Codec.INT.optionalFieldOf("limit", 10).forGetter(Extra::limit),
            Codec.BOOL.optionalFieldOf("must", false).forGetter(Extra::must)
    ).apply(i, Extra::new));

    /** "type" 走 GoodKind dispatch，其余字段由 EXTRA_MC 消费。 */
    public static final Codec<GoodType> CODEC = Codec.mapPair(
            TransactionCodecs.goodKindMapCodec(),
            EXTRA_MC
    ).xmap(
            p -> new GoodType(p.getFirst(),
                    p.getSecond().price(), p.getSecond().weight(),
                    p.getSecond().limit(), p.getSecond().must()),
            gt -> Pair.of(gt.kind(),
                    new Extra(gt.price(), gt.weight(), gt.limit(), gt.must()))
    ).codec();

    public GoodType withLimit(int newLimit) {
        return new GoodType(kind, price, weight, newLimit, must);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GoodType g
                && this.kind.equals(g.kind)
                && this.price == g.price;
    }

    @Override
    public int hashCode() {
        return this.kind.hashCode() * 31 + this.price;
    }
}