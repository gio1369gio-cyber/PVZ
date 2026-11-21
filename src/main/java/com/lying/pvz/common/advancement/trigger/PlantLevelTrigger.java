package com.lying.pvz.common.advancement.trigger;

import com.lying.pvz.common.advancement.predicate.AmountPredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class PlantLevelTrigger extends SimpleCriterionTrigger<PlantLevelTrigger.Instance> {

	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, int amount) { this.trigger(p, i -> i.test(amount)); }

	public record Instance(Optional<Holder<LootItemCondition>> player, AmountPredicate amount)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				AmountPredicate.CODEC.fieldOf("amount").forGetter(Instance::amount)
		).apply(i, Instance::new));
		public boolean test(int v) { return amount.test(v); }
	}
}