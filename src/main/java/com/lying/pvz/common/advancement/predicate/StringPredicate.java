package com.lying.pvz.common.advancement.predicate;

import com.mojang.serialization.Codec;

public record StringPredicate(String value) {
	public static final StringPredicate ANY = new StringPredicate("");

	public static final Codec<StringPredicate> CODEC =
			Codec.STRING.xmap(StringPredicate::new, StringPredicate::value);

	public boolean test(String other) {
		return this.value.isEmpty() || this.value.equals(other);
	}
}