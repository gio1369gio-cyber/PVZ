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

public record EnergyGood(int level) implements GoodKind {

    public static final MapCodec<EnergyGood> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
           Codec.intRange(2, 10).fieldOf("level").forGetter(EnergyGood::level)
    ).apply(i, EnergyGood::new));

    @Override public String type() { return "energy"; }
    @Override public ItemStack createStack(HolderLookup.Provider r) { return ItemStack.EMPTY; }
    @Override public Component describe(ItemStack stack) {
        return Component.translatable("gui.pvz.shop.more_energy");
    }
    @Override public boolean isValidFor(Player p) {
        return PlayerUtil.getResource(p, Resources.MAX_ENERGY_NUM) == this.level - 1;
    }
    @Override public int priority() { return 100; }
}