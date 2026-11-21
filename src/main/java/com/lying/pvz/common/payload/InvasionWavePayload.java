package com.lying.pvz.common.payload;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZSoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 波次事件。三相位：预告（普通/大波）、实际触发。
 *
 * <p>音效职责重新划分：{@code HUGE_WAVE} 只在 {@link Phase#HUGE_WARNING} 播，
 * 触发相位静默，只推进度条/波次标记。普通波轻音效在 {@link Phase#WARNING}。
 */
public record InvasionWavePayload(int waveIndex, int totalWaves, Phase phase) implements CustomPacketPayload {

    public enum Phase { WARNING, HUGE_WARNING, TRIGGERED }

    public static final Type<InvasionWavePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(PvZMod.MOD_ID, "invasion_wave"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InvasionWavePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, InvasionWavePayload::waveIndex,
                    ByteBufCodecs.VAR_INT, InvasionWavePayload::totalWaves,
                    ByteBufCodecs.VAR_INT.map(i -> Phase.values()[i], Phase::ordinal),
                    InvasionWavePayload::phase,
                    InvasionWavePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handle(InvasionWavePayload message, IPayloadContext context) {
            context.enqueueWork(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;

                switch (message.phase()) {
                    case WARNING -> {
                        // 普通波：轻音效。
                        player.playSound(PvZSoundRegister.HUGE_WAVE.get(), 0.4F, 1.4F);
                    }
                    case HUGE_WARNING -> {
                        player.playSound(PvZSoundRegister.HUGE_WAVE.get(), 1F, 1F);

                        Minecraft mc = Minecraft.getInstance();
                        mc.gui.hud.setTimes(10, 130, 20);
                        mc.gui.hud.setTitle(Component.translatable("invasion.pvz.huge_wave").withStyle(ChatFormatting.DARK_RED)
                        );
                    }
                    case TRIGGERED -> {
                        // 刷怪相位：静默，进度条/波次标记自然推进
                    }
                }
            });
        }
    }
}