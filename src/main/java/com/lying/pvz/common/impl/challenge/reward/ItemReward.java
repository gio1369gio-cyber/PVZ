package com.lying.pvz.common.impl.challenge.reward;

import com.lying.pvz.api.raid.IAmountDefinition;
import com.lying.pvz.api.raid.IChallenge;
import com.lying.pvz.api.raid.IRewardDefinition;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.challenge.amount.ConstantAmount;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.GiftBoxEntity;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ItemReward(List<Pair<Holder<Item>, IAmountDefinition>> items) implements IRewardDefinition {

    public static final String TYPE = "items";

    // holderByNameCodec(): Registry<T> 的 default 方法，返回 Codec<Holder<T>>，输入/输出都是 "pvz:xxx" 字符串
    private static final Codec<Pair<Holder<Item>, IAmountDefinition>> ENTRY_CODEC =
            RecordCodecBuilder.<Pair<Holder<Item>, IAmountDefinition>>create(i -> i.group(
                    BuiltInRegistries.ITEM.holderByNameCodec()
                            .fieldOf("item")
                            .forGetter(Pair::getFirst),
                    ChallengeManager.amountCodec()
                            .fieldOf("amount")
                            .orElse(new ConstantAmount(1))
                            .forGetter(Pair::getSecond)
            ).apply(i, Pair::of));

    public static final MapCodec<ItemReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ENTRY_CODEC.listOf().fieldOf(TYPE).forGetter(ItemReward::items)
    ).apply(i, ItemReward::new));

    @Override public String type() { return TYPE; }

    @Override public void reward(ServerPlayer player) { }

    @Override
    public void rewardGlobally(IChallenge challenge) {
        if (this.items.isEmpty()) return;
        final ServerLevel level = challenge.getWorld();
        final GiftBoxEntity box = PvZEntityRegister.GIFT_BOX.get()
                .create(level, EntitySpawnReason.TRIGGERED);
        if (box == null) return;
        final NonNullList<ItemStack> stacks = NonNullList.create();
        for (Pair<Holder<Item>, IAmountDefinition> pair : this.items) {
            final int count = pair.getSecond().roll(level.getRandom());
            if (count <= 0) continue;
            final int maxStack = new ItemStack(pair.getFirst()).getMaxStackSize();
            int remaining = count;
            while (remaining > 0) {
                final int size = Math.min(remaining, maxStack);
                stacks.add(new ItemStack(pair.getFirst(), size));
                remaining -= size;
            }
        }
        if (stacks.isEmpty()) return;
        box.setDrops(stacks);
        final BlockPos c = challenge.getCenter();
        box.setPos(c.getX() + 0.5D, c.getY(), c.getZ() + 0.5D);
        level.addFreshEntity(box);
    }
}