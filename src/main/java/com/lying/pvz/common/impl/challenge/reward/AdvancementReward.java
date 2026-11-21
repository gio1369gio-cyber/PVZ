package com.lying.pvz.common.impl.challenge.reward;

import com.lying.pvz.api.raid.IChallenge;
import com.lying.pvz.api.raid.IRewardDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public record AdvancementReward(AdvancementRewards rewards) implements IRewardDefinition {

    public static final String TYPE = "advancements";

    public static final MapCodec<AdvancementReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.optionalFieldOf("experience", 0)
                    .forGetter(c -> c.rewards().experience()),
            LootTable.LIST_CODEC.optionalFieldOf("loot", HolderSet.empty())
                    .forGetter(c -> c.rewards().loot()),
            Recipe.KEY_CODEC.listOf().optionalFieldOf("recipes", List.of())
                    .forGetter(c -> c.rewards().recipes()),
            CacheableFunction.CODEC.optionalFieldOf("function")
                    .forGetter(c -> c.rewards().function())
    ).apply(i, (exp, loot, recipes, fn) ->
            new AdvancementReward(new AdvancementRewards(exp, loot, recipes, fn))));

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public void reward(ServerPlayer player) {
        this.rewards.grant(player);
    }

    @Override
    public void rewardGlobally(IChallenge challenge) {
    }
}