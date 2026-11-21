package com.lying.pvz.common.payload;

import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SpawnParticlePayload(int particleType, double x, double y, double z) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SpawnParticlePayload> TYPE =
            new CustomPacketPayload.Type<>(StringUtil.id("spawn_particle"));

    public static final StreamCodec<FriendlyByteBuf, SpawnParticlePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SpawnParticlePayload::particleType,
            ByteBufCodecs.DOUBLE, SpawnParticlePayload::x,
            ByteBufCodecs.DOUBLE, SpawnParticlePayload::y,
            ByteBufCodecs.DOUBLE, SpawnParticlePayload::z,
            SpawnParticlePayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handle(SpawnParticlePayload message, IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                ParticleOptions particle = switch (message.particleType()) {
                    case 0 -> PvZParticleRegister.RED_BOMB.get();
                    case 1 -> PvZParticleRegister.MELON_SLICE.get();
                    case 2 -> PvZParticleRegister.FROZEN_MELON_SLICE.get();
                    case 3 -> PvZParticleRegister.DIRT_BURST_OUT.get();
                    case 4 -> PvZParticleRegister.YELLOW_BOMB.get();
                    case 5 -> ParticleTypes.EXPLOSION;
                    case 6 -> PvZParticleRegister.DIRT_BURST_OUT.get();
                    case 7 -> PvZParticleRegister.GREEN_SWEEP.get();
                    case 8 -> PvZParticleRegister.YELLOW_BOMB.get();
                    case 9 -> PvZParticleRegister.POP_CORN.get();
                    default -> null;
                };

                if (particle != null) {
                    player.level().addParticle(particle, message.x(), message.y(), message.z(), 0, 0, 0);
                }
            });
        }
    }
}
