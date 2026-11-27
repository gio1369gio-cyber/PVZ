package com.lying.pvz.common.attachment.player;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 玩家群系解锁状态，存玩家 NBT（Data Attachment）。
 *
 * <p>语义：{@link #isUnlocked(ResourceKey)} 为 {@code true} 表示玩家可传送到该群系。
 * 新玩家默认只解锁起始群系远古埃及，其余群系需通过进度（BOSS / 击杀计数）解锁。
 *
 * <p>解锁判定走服务端权威：触发方在服务端调用 {@link #unlock(ResourceKey)}，
 * 随后由 facade 负责 {@code syncData}。
 */
public final class BiomeUnlockState implements ValueIOSerializable {

    /** 起始群系，新玩家默认解锁且始终可传送。 */
    private static final Identifier START_BIOME = PvZBiome.ANCIENT_EGYPT.identifier();

    private final Set<Identifier> unlocked = new HashSet<>();

    /**
     * 每个群系的记忆传送落点（首次到达该群系时定位一次并记录，之后固定复用）。
     * 目的：避免 TimeKey 误触时因重新随机定位而「乱传」。
     */
    private final Map<Identifier, BlockPos> anchors = new HashMap<>();

    /** 是否已发放过埃德加玩偶（只发一次，永久持有）。 */
    private boolean edgarDollGiven;

    public BiomeUnlockState() {
        unlocked.add(START_BIOME);
    }

    public boolean isEdgarDollGiven() {
        return edgarDollGiven;
    }

    public void setEdgarDollGiven(boolean given) {
        this.edgarDollGiven = given;
    }

    public boolean isUnlocked(ResourceKey<Biome> biome) {
        return isUnlocked(biome.identifier());
    }

    public boolean isUnlocked(Identifier id) {
        return unlocked.contains(id);
    }

    /** 解锁群系，返回本次是否发生实际变化（已解锁则 false）。 */
    public boolean unlock(ResourceKey<Biome> biome) {
        return unlocked.add(biome.identifier());
    }

    public Set<Identifier> all() {
        return Collections.unmodifiableSet(unlocked);
    }

    /* ---------------- 群系记忆坐标 ---------------- */

    /** 读取群系的记忆落点；从未到达过则返回 null。 */
    @Nullable
    public BlockPos getAnchor(ResourceKey<Biome> biome) {
        return anchors.get(biome.identifier());
    }

    /** 是否已记忆该群系落点。 */
    public boolean hasAnchor(ResourceKey<Biome> biome) {
        return anchors.containsKey(biome.identifier());
    }

    /** 记录群系落点（只写一次语义由调用方保证；重复调用会覆盖）。 */
    public void setAnchor(ResourceKey<Biome> biome, BlockPos pos) {
        anchors.put(biome.identifier(), pos.immutable());
    }

    /* ---------------- 序列化（玩家 NBT） ---------------- */

    /** 群系 ID → 坐标 的 codec。用 BlockPos.CODEC 存三个整数。 */
    private static final Codec<Map<Identifier, BlockPos>> ANCHORS_CODEC = Codec.unboundedMap(
            Codec.STRING.xmap(Identifier::parse, Identifier::toString), BlockPos.CODEC);

    @Override
    public void serialize(ValueOutput out) {
        final ValueOutput.TypedOutputList<Identifier> list = out.list("unlocked_biomes", Identifier.CODEC);
        for (Identifier id : unlocked) {
            list.add(id);
        }
        out.store("biome_anchors", ANCHORS_CODEC, anchors);
        out.putBoolean("edgar_doll_given", edgarDollGiven);
    }

    @Override
    public void deserialize(ValueInput in) {
        unlocked.clear();
        for (Identifier id : in.listOrEmpty("unlocked_biomes", Identifier.CODEC)) {
            unlocked.add(id);
        }
        // 兼容旧存档 / 防止误删起始群系
        unlocked.add(START_BIOME);
        anchors.clear();
        in.read("biome_anchors", ANCHORS_CODEC).ifPresent(anchors::putAll);
        this.edgarDollGiven = in.getBooleanOr("edgar_doll_given", false);
    }

    /* ---------------- 同步 ---------------- */

    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeUnlockState> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BiomeUnlockState decode(RegistryFriendlyByteBuf buf) {
                    final BiomeUnlockState s = new BiomeUnlockState();
                    s.unlocked.clear();
                    final int size = buf.readVarInt();
                    for (int i = 0; i < size; i++) {
                        s.unlocked.add(Identifier.parse(buf.readUtf()));
                    }
                    s.anchors.clear();
                    final int anchorCount = buf.readVarInt();
                    for (int i = 0; i < anchorCount; i++) {
                        s.anchors.put(Identifier.parse(buf.readUtf()), buf.readBlockPos());
                    }
                    s.edgarDollGiven = buf.readBoolean();
                    return s;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BiomeUnlockState s) {
                    buf.writeVarInt(s.unlocked.size());
                    for (Identifier id : s.unlocked) {
                        buf.writeUtf(id.toString());
                    }
                    buf.writeVarInt(s.anchors.size());
                    s.anchors.forEach((id, pos) -> {
                        buf.writeUtf(id.toString());
                        buf.writeBlockPos(pos);
                    });
                    buf.writeBoolean(s.edgarDollGiven);
                }
            };
}
