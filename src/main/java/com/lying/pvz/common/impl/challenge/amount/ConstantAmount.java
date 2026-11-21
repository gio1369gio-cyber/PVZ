package com.lying.pvz.common.impl.challenge.amount;

import com.lying.pvz.api.raid.IAmountDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record ConstantAmount(int count) implements IAmountDefinition {

    public static final String TYPE = "count";
    public static final MapCodec<ConstantAmount> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.fieldOf(TYPE).forGetter(ConstantAmount::count)
    ).apply(i, ConstantAmount::new));

    @Override public String type() { return TYPE; }
    @Override public int getMinAmount() { return this.count; }
    @Override public int getMaxAmount() { return this.count; }
    @Override public int roll(RandomSource random) { return this.count; }
}