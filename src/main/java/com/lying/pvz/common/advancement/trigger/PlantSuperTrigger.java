package com.lying.pvz.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class PlantSuperTrigger extends SimpleCriterionTrigger<PlantSuperTrigger.Instance> {

	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, Entity e) { this.trigger(p, i -> i.test(p, e)); }

	public record Instance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(Instance::entity)
		).apply(i, Instance::new));

		public boolean test(ServerPlayer p, Entity e) {
			var ctx = EntityPredicate.createContext(p, e);
			return this.entity.map(h -> h.value().test(ctx)).orElse(true);
		}
	}
}