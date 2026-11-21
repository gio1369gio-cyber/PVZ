package com.lying.pvz.common.invasion;

import com.lying.pvz.common.invasion.type.InvasionFog;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 入侵状态快照——attachment 持久化 + 网络同步的载体。
 *
 * <p>不含运行时数据（{@code spawnedEntities} / {@code nextSpawnTick} / {@code victoryGraceTick}），
 * 这些只在服务端 {@link Invasion} 实例中存在，不序列化、不同步。
 *
 * <p>客户端 UI 通过 {@code player.getData(PvZAttachments.INVASION_SNAPSHOT)} 读取。
 */
public record InvasionSnapshot(
        boolean running,
        Optional<Identifier> primaryId,
        List<Identifier> assistIds,
        int waveIndex,
        List<Integer> waveTimes,
        List<Integer> waveSpawnCounts,
        Optional<Identifier> missionTypeId,
        int missionStage,
        int missionValue,
        int currentTick,
        Optional<InvasionFog> fog
) {

    /** 空闲态：入侵未运行。 */
    public static final InvasionSnapshot IDLE = new InvasionSnapshot(
            false, Optional.empty(), List.of(), 0,
            List.of(), List.of(), Optional.empty(), 0, 0, 0, Optional.empty());

    /* ---------------- Codec（持久化） ---------------- */

    public static final MapCodec<InvasionSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.optionalFieldOf("running", false).forGetter(InvasionSnapshot::running),
            Identifier.CODEC.optionalFieldOf("primary").forGetter(InvasionSnapshot::primaryId),
            Identifier.CODEC.listOf().optionalFieldOf("assists", List.of()).forGetter(InvasionSnapshot::assistIds),
            Codec.INT.optionalFieldOf("wave_index", 0).forGetter(InvasionSnapshot::waveIndex),
            Codec.INT.listOf().optionalFieldOf("wave_times", List.of()).forGetter(InvasionSnapshot::waveTimes),
            Codec.INT.listOf().optionalFieldOf("wave_counts", List.of()).forGetter(InvasionSnapshot::waveSpawnCounts),
            Identifier.CODEC.optionalFieldOf("mission_type").forGetter(InvasionSnapshot::missionTypeId),
            Codec.INT.optionalFieldOf("mission_stage", 0).forGetter(InvasionSnapshot::missionStage),
            Codec.INT.optionalFieldOf("mission_value", 0).forGetter(InvasionSnapshot::missionValue),
            Codec.INT.optionalFieldOf("current_tick", 0).forGetter(InvasionSnapshot::currentTick),
            InvasionFog.CODEC.optionalFieldOf("fog").forGetter(InvasionSnapshot::fog)
    ).apply(i, InvasionSnapshot::new));

    /* ---------------- StreamCodec（网络同步） ---------------- */
    // 手写，避免依赖 ByteBufCodecs.list() / Identifier.STREAM_CODEC 的不确定 API。

    public static final StreamCodec<RegistryFriendlyByteBuf, InvasionSnapshot> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public InvasionSnapshot decode(RegistryFriendlyByteBuf buf) {
                    boolean running = buf.readBoolean();
                    Optional<Identifier> primaryId = readOptionalId(buf);
                    int assistSize = buf.readVarInt();
                    List<Identifier> assistIds = new ArrayList<>(assistSize);
                    for (int i = 0; i < assistSize; i++) assistIds.add(Identifier.parse(buf.readUtf()));
                    int waveIndex = buf.readVarInt();
                    int timeSize = buf.readVarInt();
                    List<Integer> waveTimes = new ArrayList<>(timeSize);
                    for (int i = 0; i < timeSize; i++) waveTimes.add(buf.readVarInt());
                    int countSize = buf.readVarInt();
                    List<Integer> waveCounts = new ArrayList<>(countSize);
                    for (int i = 0; i < countSize; i++) waveCounts.add(buf.readVarInt());
                    Optional<Identifier> missionTypeId = readOptionalId(buf);
                    int missionStage = buf.readVarInt();
                    int missionValue = buf.readVarInt();
                    int currentTick = buf.readVarInt();
                    Optional<InvasionFog> fog = readOptionalFog(buf);
                    return new InvasionSnapshot(running, primaryId, assistIds, waveIndex,
                            waveTimes, waveCounts, missionTypeId, missionStage, missionValue, currentTick, fog);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, InvasionSnapshot s) {
                    buf.writeBoolean(s.running);
                    writeOptionalId(buf, s.primaryId);
                    buf.writeVarInt(s.assistIds.size());
                    for (Identifier id : s.assistIds) buf.writeUtf(id.toString());
                    buf.writeVarInt(s.waveIndex);
                    buf.writeVarInt(s.waveTimes.size());
                    for (int v : s.waveTimes) buf.writeVarInt(v);
                    buf.writeVarInt(s.waveSpawnCounts.size());
                    for (int v : s.waveSpawnCounts) buf.writeVarInt(v);
                    writeOptionalId(buf, s.missionTypeId);
                    buf.writeVarInt(s.missionStage);
                    buf.writeVarInt(s.missionValue);
                    buf.writeVarInt(s.currentTick);
                    writeOptionalFog(buf, s.fog);
                }

                private void writeOptionalId(RegistryFriendlyByteBuf buf, Optional<Identifier> id) {
                    if (id.isPresent()) {
                        buf.writeBoolean(true);
                        buf.writeUtf(id.get().toString());
                    } else {
                        buf.writeBoolean(false);
                    }
                }

                private Optional<Identifier> readOptionalId(RegistryFriendlyByteBuf buf) {
                    return buf.readBoolean() ? Optional.of(Identifier.parse(buf.readUtf())) : Optional.empty();
                }

                private void writeOptionalFog(RegistryFriendlyByteBuf buf, Optional<InvasionFog> fog) {
                    buf.writeBoolean(fog.isPresent());
                    fog.ifPresent(f -> InvasionFog.STREAM_CODEC.encode(buf, f));
                }

                private Optional<InvasionFog> readOptionalFog(RegistryFriendlyByteBuf buf) {
                    return buf.readBoolean() ? Optional.of(InvasionFog.STREAM_CODEC.decode(buf)) : Optional.empty();
                }
            };
}