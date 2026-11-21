package com.lying.pvz.common.attachment.player;

import com.lying.pvz.utils.enums.Resources;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * 玩家资源状态。
 *
 * <p>纯数据，不持有 {@code Player} 引用。权威源。
 *
 * <p>序列化：{@code int[]} 按 {@link Resources#ordinal()} 索引。
 * 同步：全量 varint 编码（15 个 int）。
 */
public final class PlayerStats implements ValueIOSerializable {

    private final int[] values = new int[Resources.values().length];

    public PlayerStats() {
        for (Resources r : Resources.values()) {
            values[r.ordinal()] = Resources.getInitialValue(r);
        }
    }

    public int get(Resources r) {
        return values[r.ordinal()];
    }

    /** 按 {@code r.min/r.max} 兜底 clamp 后写入。 */
    public void set(Resources r, int v) {
        values[r.ordinal()] = Mth.clamp(v, r.min, r.max);
    }

    /**
     * 增量加法，按 {@code r.min/r.max} clamp。
     *
     * @return 实际变化量（可能小于 {@code delta}，也可能因 clamp 为 0）
     */
    public int add(Resources r, int delta) {
        final int before = get(r);
        set(r, before + delta);
        return get(r) - before;
    }

    /** 内部数组（只读语义，调用方不应修改）。供 facade 遍历。 */
    public int[] raw() {
        return values;
    }

    /* ---------------- 序列化 ---------------- */

    @Override
    public void serialize(ValueOutput out) {
        out.putIntArray("v", values.clone());
    }

    @Override
    public void deserialize(ValueInput in) {
        in.getIntArray("v").ifPresent(arr -> {
            final int n = Math.min(arr.length, values.length);
            System.arraycopy(arr, 0, values, 0, n);
        });
    }

    /* ---------------- 同步 ---------------- */

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStats> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public PlayerStats decode(RegistryFriendlyByteBuf buf) {
                    final PlayerStats stats = new PlayerStats();
                    for (int i = 0; i < stats.values.length; i++) {
                        stats.values[i] = buf.readVarInt();
                    }
                    return stats;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, PlayerStats stats) {
                    for (int v : stats.values) {
                        buf.writeVarInt(v);
                    }
                }
            };
}