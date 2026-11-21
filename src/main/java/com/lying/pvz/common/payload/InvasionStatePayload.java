package com.lying.pvz.common.payload;


import com.lying.pvz.api.event.invasion.InvasionEndEvent;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

/**
 * 入侵开始/结束事件（一次性通知，不参与状态同步）。
 *
 * <p>客户端只负责音效；START/END 的文本消息由服务端 {@code sendSystemMessage} 发送，
 * 避免双发。
 */
public record InvasionStatePayload(
        boolean start,
        Optional<Identifier> primaryId,
        InvasionEndEvent.Reason reason
) implements CustomPacketPayload {

    public static final Type<InvasionStatePayload> TYPE =
            new Type<>(StringUtil.id("invasion_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InvasionStatePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, InvasionStatePayload::start,
                    ByteBufCodecs.optional(
                            ByteBufCodecs.STRING_UTF8.map(Identifier::parse, Identifier::toString)
                    ), InvasionStatePayload::primaryId,
                    ByteBufCodecs.VAR_INT.map(
                            i -> InvasionEndEvent.Reason.values()[i],
                            InvasionEndEvent.Reason::ordinal
                    ), InvasionStatePayload::reason,
                    InvasionStatePayload::new
            );

    /** START 便捷构造。 */
    public static InvasionStatePayload start(Identifier primaryId) {
        return new InvasionStatePayload(true, Optional.of(primaryId), InvasionEndEvent.Reason.NATURAL);
    }

    /** END 便捷构造。 */
    public static InvasionStatePayload end(InvasionEndEvent.Reason reason) {
        return new InvasionStatePayload(false, Optional.empty(), reason);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handle(InvasionStatePayload message, IPayloadContext context) {
            context.enqueueWork(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;

                if (message.start()) {
                    player.playSound(PvZSoundRegister.ZOMBIE_SIREN.get(), 1F, 1F);
                } else if (message.reason() == InvasionEndEvent.Reason.VICTORY) {
                    player.playSound(PvZSoundRegister.WIN_MUSIC.get(), 1F, 1F);
                }
            });
        }
    }
}
