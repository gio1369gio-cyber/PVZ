package com.lying.pvz.common.advancement.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds;

public record AmountPredicate(MinMaxBounds.Ints amount) {
	public static final AmountPredicate ANY = new AmountPredicate(MinMaxBounds.Ints.ANY);

	public static final Codec<AmountPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			MinMaxBounds.Ints.CODEC.fieldOf("amount").forGetter(AmountPredicate::amount)
	).apply(i, AmountPredicate::new));

	public boolean test(int value) {
		return this.amount.matches(value);
	}
}