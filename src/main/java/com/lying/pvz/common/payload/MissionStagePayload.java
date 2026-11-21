package com.lying.pvz.common.payload;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZSoundRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 任务阶段完成事件（一次性通知）。
 *
 * <p>消息（"mission.finish"）由服务端 {@code MissionTypes.defaultReward} 发送，
 * 本 payload 只负责播放阶段音效。
 */
public record MissionStagePayload(Identifier missionTypeId, int stage) implements CustomPacketPayload {

    public static final Type<MissionStagePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(PvZMod.MOD_ID, "mission_stage"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MissionStagePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.map(Identifier::parse, Identifier::toString),
                    MissionStagePayload::missionTypeId,
                    ByteBufCodecs.VAR_INT, MissionStagePayload::stage,
                    MissionStagePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handle(MissionStagePayload message, IPayloadContext context) {
            context.enqueueWork(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;

                switch (message.stage()) {
                    case 0, 2 -> player.playSound(PvZSoundRegister.SUN_PICK.get(), 1F, 1F);
                    case 1, 3 -> player.playSound(PvZSoundRegister.SLOT_MACHINE.get(), 1F, 1F);
                    default   -> player.playSound(PvZSoundRegister.JEWEL_PICK.get(), 1F, 1F);
                }
            });
        }
    }
}