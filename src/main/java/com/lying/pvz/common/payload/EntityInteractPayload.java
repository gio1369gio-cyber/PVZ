package com.lying.pvz.common.payload;

import com.lying.pvz.common.level.entity.plant.explosion.CobCannonEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EntityInteractPayload(int entityId, int op, int num) implements CustomPacketPayload {

	public static final Type<EntityInteractPayload> TYPE = new Type<>(StringUtil.id("entity_interact"));

	public static final StreamCodec<FriendlyByteBuf, EntityInteractPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, EntityInteractPayload::entityId,
			ByteBufCodecs.VAR_INT, EntityInteractPayload::op,
			ByteBufCodecs.VAR_INT, EntityInteractPayload::num,
			EntityInteractPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static class Handler {
		public static void handle(EntityInteractPayload message, IPayloadContext context) {
			if (!(context.player() instanceof ServerPlayer player)) return;

			context.enqueueWork(() -> {
				Entity entity = player.level().getEntity(message.entityId());
				if (entity instanceof CobCannonEntity cob) {
					cob.checkAndAttack();
				}
			});
		}
	}
}