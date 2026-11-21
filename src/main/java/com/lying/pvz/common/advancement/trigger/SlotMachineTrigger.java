package com.lying.pvz.common.advancement.trigger;

import com.lying.pvz.common.advancement.predicate.AmountPredicate;
import com.lying.pvz.common.advancement.predicate.StringPredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class SlotMachineTrigger extends SimpleCriterionTrigger<SlotMachineTrigger.Instance> {
	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, int amount, String slotType) { this.trigger(p, i -> i.test(amount, slotType)); }

	public record Instance(Optional<Holder<LootItemCondition>> player,
						   AmountPredicate amount, StringPredicate type)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				AmountPredicate.CODEC.optionalFieldOf("amount", AmountPredicate.ANY).forGetter(Instance::amount),
				StringPredicate.CODEC.optionalFieldOf("type", StringPredicate.ANY).forGetter(Instance::type)
		).apply(i, Instance::new));
		public boolean test(int amt, String t) { return amount.test(amt) && type.test(t); }
	}
}