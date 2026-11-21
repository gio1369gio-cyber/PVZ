package com.lying.pvz.common.payload;



import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlaySoundPayload(String soundId) implements CustomPacketPayload {

	public static final Type<PlaySoundPayload> TYPE =
			new Type<>(StringUtil.id( "play_sound"));

	public static final StreamCodec<FriendlyByteBuf, PlaySoundPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, PlaySoundPayload::soundId,
			PlaySoundPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static class Handler {
		public static void handle(PlaySoundPayload message, IPayloadContext context) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player == null) return;

				Identifier soundKey = Identifier.tryParse(message.soundId());
				if (soundKey == null) return;

				BuiltInRegistries.SOUND_EVENT.getOptional(soundKey).ifPresent(sound ->
						player.playSound(sound, 1F, 1F));
			});
		}
	}
}