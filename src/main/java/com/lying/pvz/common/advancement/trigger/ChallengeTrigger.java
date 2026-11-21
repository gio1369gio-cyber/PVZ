package com.lying.pvz.common.advancement.trigger;


import com.lying.pvz.common.advancement.predicate.StringPredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class ChallengeTrigger extends SimpleCriterionTrigger<ChallengeTrigger.Instance> {
    @Override public Codec<Instance> codec() { return Instance.CODEC; }
    public void trigger(ServerPlayer p, String s) { this.trigger(p, i -> i.test(s)); }

    public record Instance(Optional<Holder<LootItemCondition>> player, StringPredicate type)
            implements SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player),
                StringPredicate.CODEC.fieldOf("type").forGetter(Instance::type)
        ).apply(i, Instance::new));
        public boolean test(String s) { return type.test(s); }
    }
}