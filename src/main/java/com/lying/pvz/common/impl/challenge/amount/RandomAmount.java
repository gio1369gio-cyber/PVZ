package com.lying.pvz.common.impl.challenge.amount;

import com.lying.pvz.api.raid.IAmountDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record RandomAmount(int min, int max) implements IAmountDefinition {

    public static final String TYPE = "random";
    public static final MapCodec<RandomAmount> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.fieldOf("min").forGetter(RandomAmount::min),
            Codec.INT.fieldOf("max").forGetter(RandomAmount::max)
    ).apply(i, RandomAmount::new));


    @Override public String type() { return TYPE; }

    @Override
    public int roll(RandomSource random) {
        final int lo = Math.min(this.min, this.max);
        final int hi = Math.max(this.min, this.max);
        return Math.max(0, random.nextInt(hi - lo + 1) + lo);
    }

    @Override public int getMinAmount() { return Math.min(this.min, this.max); }
    @Override public int getMaxAmount() { return Math.max(this.min, this.max); }
}