package com.lying.pvz.common.invasion.type;

import com.lying.pvz.api.invasion.IInvasionFog;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * 迷雾策略实现——数据包反序列化 + 网络同步载体。
 *
 * <p>JSON 中 {@code fog} 字段缺省即无雾（{@code Optional}），字段均可省略走默认值。
 */
public record InvasionFog(float strength, int nearDistance, int farDistance, int duration,
        int fadeTicks, boolean hideRaiders, boolean hidePlants
) implements IInvasionFog {

    public static final Codec<InvasionFog> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.optionalFieldOf("strength", 0.85F).forGetter(InvasionFog::strength),
            Codec.INT.optionalFieldOf("near_distance", 8).forGetter(InvasionFog::nearDistance),
            Codec.INT.optionalFieldOf("far_distance", 32).forGetter(InvasionFog::farDistance),
            Codec.INT.optionalFieldOf("duration", 0).forGetter(InvasionFog::duration),
            Codec.INT.optionalFieldOf("fade_ticks", 200).forGetter(InvasionFog::fadeTicks),
            Codec.BOOL.optionalFieldOf("hide_raiders", true).forGetter(InvasionFog::hideRaiders),
            Codec.BOOL.optionalFieldOf("hide_plants", false).forGetter(InvasionFog::hidePlants)
    ).apply(i, InvasionFog::new));

    /** 网络同步。视觉雾阶段仅用 {@link #strength()}；其余字段保留给功能雾。 */
    public static final StreamCodec<RegistryFriendlyByteBuf, InvasionFog> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public InvasionFog decode(RegistryFriendlyByteBuf buf) {
                    return new InvasionFog(
                            buf.readFloat(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readBoolean(),
                            buf.readBoolean());
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, InvasionFog f) {
                    buf.writeFloat(f.strength);
                    buf.writeVarInt(f.nearDistance);
                    buf.writeVarInt(f.farDistance);
                    buf.writeVarInt(f.duration);
                    buf.writeVarInt(f.fadeTicks);
                    buf.writeBoolean(f.hideRaiders);
                    buf.writeBoolean(f.hidePlants);
                }
            };
}
