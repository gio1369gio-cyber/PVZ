package com.lying.pvz.common.impl.challenge.reward;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.raid.IChallenge;
import com.lying.pvz.api.raid.IRewardDefinition;
import com.lying.pvz.utils.PlayerUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public record UnlockReward(List<IPAZType> unlocks) implements IRewardDefinition {

    public static final String TYPE = "unlocks";

    private static final Codec<IPAZType> PAZ_TYPE_CODEC = Identifier.CODEC.comapFlatMap(
            id -> PVZAPI.lookup().paz(id)
                    .map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Unknown PAZ type: " + id)),
            IPAZType::getID);

    public static final MapCodec<UnlockReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            PAZ_TYPE_CODEC.listOf().fieldOf(TYPE).forGetter(UnlockReward::unlocks)
    ).apply(i, UnlockReward::new));

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public void reward(ServerPlayer player) {
        for (IPAZType type : this.unlocks) {
            PlayerUtil.setPAZUnlocked(player, type, true);
            PlayerUtil.sendMsgTo(player, Component.translatable(
                    "challenge.pvz.unlock",
                    type.getText().getString()
            ).withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public void rewardGlobally(IChallenge challenge) {
    }
}