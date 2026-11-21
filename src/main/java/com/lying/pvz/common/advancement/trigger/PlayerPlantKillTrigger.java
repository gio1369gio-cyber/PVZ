package com.lying.pvz.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;

public class PlayerPlantKillTrigger extends SimpleCriterionTrigger<PlayerPlantKillTrigger.Instance> {
	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, Entity e, DamageSource src) { this.trigger(p, i -> i.test(p, e, src)); }

	public record Instance(Optional<Holder<LootItemCondition>> player,
						   Optional<Holder<LootItemCondition>> entity,
						   Optional<DamageSourcePredicate> killingBlow)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(Instance::entity),
				DamageSourcePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(Instance::killingBlow)
		).apply(i, Instance::new));

		public boolean test(ServerPlayer p, Entity e, DamageSource src) {
			var ctx = EntityPredicate.createContext(p, e);
			return entity.map(h -> h.value().test(ctx)).orElse(true)
					&& killingBlow.map(dp -> dp.matches(p, src)).orElse(true);
		}
	}
}