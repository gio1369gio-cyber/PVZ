package com.lying.pvz.common.challenge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 挑战运行时状态的最小可持久化快照。
 *
 * <p>不含 {@code IChallengeDefinition}（datapack 数据，重载会变），
 * 不含 {@code currentMaxLevel}（每 10 tick 重算）。
 * 加载时通过 {@code definitionId} 从 {@code ChallengeManager} 反查定义，
 * 查不到则整条实例丢弃。
 */
public record ChallengeSnapshot(
        int instanceId,
        Identifier definitionId,
        BlockPos center,
        int stateOrdinal,
        int tick,
        int stopTick,
        int waitTick,
        int currentWave,
        int currentSpawn,
        boolean firstTick,
        boolean activated,
        @Nullable UUID ownerUUID,
        List<UUID> raiders,
        List<UUID> participants,
        List<UUID> deadParticipants
) {
    public static final Codec<ChallengeSnapshot> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("instance_id").forGetter(ChallengeSnapshot::instanceId),
            Identifier.CODEC.fieldOf("definition_id").forGetter(ChallengeSnapshot::definitionId),
            BlockPos.CODEC.fieldOf("center").forGetter(ChallengeSnapshot::center),
            Codec.INT.fieldOf("state").forGetter(ChallengeSnapshot::stateOrdinal),
            Codec.INT.fieldOf("tick").forGetter(ChallengeSnapshot::tick),
            Codec.INT.fieldOf("stop_tick").forGetter(ChallengeSnapshot::stopTick),
            Codec.INT.fieldOf("wait_tick").forGetter(ChallengeSnapshot::waitTick),
            Codec.INT.fieldOf("current_wave").forGetter(ChallengeSnapshot::currentWave),
            Codec.INT.fieldOf("current_spawn").forGetter(ChallengeSnapshot::currentSpawn),
            Codec.BOOL.fieldOf("first_tick").forGetter(ChallengeSnapshot::firstTick),
            Codec.BOOL.fieldOf("activated").forGetter(ChallengeSnapshot::activated),
            UUIDUtil.CODEC.optionalFieldOf("owner").forGetter(s -> Optional.ofNullable(s.ownerUUID())),
            UUIDUtil.CODEC.listOf().fieldOf("raiders").forGetter(ChallengeSnapshot::raiders),
            UUIDUtil.CODEC.listOf().fieldOf("participants").forGetter(ChallengeSnapshot::participants),
            UUIDUtil.CODEC.listOf().fieldOf("dead_participants").forGetter(ChallengeSnapshot::deadParticipants)
    ).apply(i, (instanceId, definitionId, center, stateOrdinal, tick, stopTick, waitTick,
                currentWave, currentSpawn, firstTick, activated, ownerOpt,
                raiders, participants, deadParticipants) ->
            new ChallengeSnapshot(instanceId, definitionId, center, stateOrdinal, tick, stopTick, waitTick,
                    currentWave, currentSpawn, firstTick, activated, ownerOpt.orElse(null),
                    raiders, participants, deadParticipants)));
}