package com.lying.pvz.common.advancement.trigger;

import com.lying.pvz.common.advancement.predicate.StringPredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class PlayerPlacePAZTrigger extends SimpleCriterionTrigger<PlayerPlacePAZTrigger.Instance> {
	@Override public Codec<Instance> codec() { return Instance.CODEC; }
	public void trigger(ServerPlayer p, String placeType, String pazType) { this.trigger(p, i -> i.test(placeType, pazType)); }

	public record Instance(Optional<Holder<LootItemCondition>> player,
						   StringPredicate placeType, StringPredicate pazType)
			implements SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
				StringPredicate.CODEC.optionalFieldOf("place_type", StringPredicate.ANY).forGetter(Instance::placeType),
				StringPredicate.CODEC.optionalFieldOf("paz_type", StringPredicate.ANY).forGetter(Instance::pazType)
		).apply(i, Instance::new));
		public boolean test(String pt, String pzt) { return placeType.test(pt) && pazType.test(pzt); }
	}

	public enum PlaceTypes { PLANT, UPGRADE, ZOMBIE }
}