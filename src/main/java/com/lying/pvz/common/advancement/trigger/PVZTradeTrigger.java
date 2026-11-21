package com.lying.pvz.common.advancement.trigger;

import com.lying.pvz.common.advancement.predicate.AmountPredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class PVZTradeTrigger extends SimpleCriterionTrigger<PVZTradeTrigger.Instance> {
	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, Entity e, int amount) { this.trigger(p, i -> i.test(p, e, amount)); }

	public record Instance(Optional<Holder<LootItemCondition>> player,
						   Optional<Holder<LootItemCondition>> entity, AmountPredicate amount)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(Instance::entity),
				AmountPredicate.CODEC.optionalFieldOf("amount", AmountPredicate.ANY).forGetter(Instance::amount)
		).apply(i, Instance::new));

		public boolean test(ServerPlayer p, Entity e, int amount) {
			var ctx = EntityPredicate.createContext(p, e);
			return this.entity.map(h -> h.value().test(ctx)).orElse(true)
					&& this.amount.test(amount);
		}
	}
}