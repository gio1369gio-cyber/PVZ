package com.lying.pvz.common.impl.challenge;

import com.lying.pvz.api.raid.IPlacementDefinition;
import com.lying.pvz.api.raid.ISpawnDefinition;
import com.lying.pvz.api.raid.IWaveDefinition;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Optional;

public record DefaultWaveDefinition(
        int duration,
        int preCD,
        IPlacementDefinition placement,
        List<ISpawnDefinition> spawns
) implements IWaveDefinition {

    public static final String TYPE = "default";

    public static final MapCodec<DefaultWaveDefinition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.POSITIVE_INT
                    .fieldOf("duration")
                    .forGetter(DefaultWaveDefinition::duration),
            Codec.INT
                    .optionalFieldOf("pre_tick", 100)
                    .forGetter(DefaultWaveDefinition::preCD),
            ChallengeManager.placementCodec()
                    .optionalFieldOf("placement")
                    .forGetter(w -> Optional.ofNullable(w.placement())),
            ChallengeManager.spawnCodec()
                    .listOf()
                    .validate(DefaultWaveDefinition::nonEmptySpawns)
                    .fieldOf("spawns")
                    .forGetter(DefaultWaveDefinition::spawns)
    ).apply(i, (duration, pre, placement, spawns) ->
            new DefaultWaveDefinition(duration, pre, placement.orElse(null), spawns)));

    private static DataResult<List<ISpawnDefinition>> nonEmptySpawns(List<ISpawnDefinition> list) {
        return list.isEmpty()
                ? DataResult.error(() -> "Wave spawn list cannot be empty")
                : DataResult.success(list);
    }

    @Override
    public String type() { return TYPE; }

    @Override public int getPrepareCD() { return this.preCD; }
    @Override public int getLastDuration() { return this.duration; }
    @Override public List<ISpawnDefinition> getSpawns() { return this.spawns; }
    @Override public IPlacementDefinition getPlacement() { return this.placement; }
}