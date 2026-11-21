package com.lying.pvz.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class InvasionTrigger extends SimpleCriterionTrigger<InvasionTrigger.Instance> {
    @Override public Codec<Instance> codec() { return Instance.CODEC; }
    public void trigger(ServerPlayer p) { this.trigger(p, i -> true); }

    public record Instance(Optional<Holder<LootItemCondition>> player)
            implements SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player)
        ).apply(i, Instance::new));
    }
}