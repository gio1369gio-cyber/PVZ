package com.lying.pvz.common.transaction.goodkind;

import com.lying.pvz.api.transaction.GoodKind;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SlotGood(int level) implements GoodKind {

    public static final MapCodec<SlotGood> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
           Codec.intRange(2, 10).fieldOf("level").forGetter(SlotGood::level)
    ).apply(i, SlotGood::new));

    @Override public String type() { return "slot"; }
    @Override public ItemStack createStack(HolderLookup.Provider r) { return ItemStack.EMPTY; }
    @Override public Component describe(ItemStack stack) {
        return Component.translatable("gui.pvz.shop.more_slot");
    }
    @Override public boolean isValidFor(Player p) {
        return PlayerUtil.getResource(p, Resources.SLOT_NUM) == this.level - 1;
    }
    @Override public int priority() { return 90; }
}