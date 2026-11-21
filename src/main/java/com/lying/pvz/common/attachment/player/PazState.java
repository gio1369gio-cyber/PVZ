package com.lying.pvz.common.attachment.player;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * PAZ 解锁状态。
 *
 * <p>语义：{@link #isUnlocked(Identifier)} 返回 {@code true} 表示已解锁。
 * 新玩家默认全部锁定（{@code unlocked} 为空集）。
 *
 * <p>解锁流程：{@code unLockPAZs} 按 {@code IPAZType.getRequiredLevel()} 与
 * {@code TREE_LVL} 比较后批量 {@link #setUnlocked(Identifier, boolean)}。
 */
public final class PazState implements ValueIOSerializable {

    private final Set<Identifier> unlocked = new HashSet<>();

    public boolean isUnlocked(Identifier id) {
        return unlocked.contains(id);
    }

    public void setUnlocked(Identifier id, boolean u) {
        if (u) {
            unlocked.add(id);
        } else {
            unlocked.remove(id);
        }
    }

    public Set<Identifier> all() {
        return Collections.unmodifiableSet(unlocked);
    }

    /* ---------------- 序列化 ---------------- */

    @Override
    public void serialize(ValueOutput out) {
        final ValueOutput.TypedOutputList<Identifier> list = out.list("unlocked", Identifier.CODEC);
        for (Identifier id : unlocked) {
            list.add(id);
        }
    }

    @Override
    public void deserialize(ValueInput in) {
        unlocked.clear();
        for (Identifier id : in.listOrEmpty("unlocked", Identifier.CODEC)) {
            unlocked.add(id);
        }
    }

    /* ---------------- 同步 ---------------- */

    public static final StreamCodec<RegistryFriendlyByteBuf, PazState> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public PazState decode(RegistryFriendlyByteBuf buf) {
                    final PazState s = new PazState();
                    final int size = buf.readVarInt();
                    for (int i = 0; i < size; i++) {
                        s.unlocked.add(Identifier.parse(buf.readUtf()));
                    }
                    return s;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, PazState s) {
                    buf.writeVarInt(s.unlocked.size());
                    for (Identifier id : s.unlocked) {
                        buf.writeUtf(id.toString());
                    }
                }
            };
}