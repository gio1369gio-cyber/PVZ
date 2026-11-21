package com.lying.pvz.common.attachment.player;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.Collections;
import java.util.HashSet;
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

    /* ---------------- 序列化（玩家 NBT） ---------------- */

    @Override
    public void serialize(ValueOutput out) {
        final ValueOutput.TypedOutputList<Identifier> list = out.list("unlocked_biomes", Identifier.CODEC);
        for (Identifier id : unlocked) {
            list.add(id);
        }
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
                    s.edgarDollGiven = buf.readBoolean();
                    return s;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BiomeUnlockState s) {
                    buf.writeVarInt(s.unlocked.size());
                    for (Identifier id : s.unlocked) {
                        buf.writeUtf(id.toString());
                    }
                    buf.writeBoolean(s.edgarDollGiven);
                }
            };
}
