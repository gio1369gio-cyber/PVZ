package com.lying.pvz.common.transaction.goodkind;

import com.lying.pvz.api.transaction.GoodKind;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** 直接以资源点（如金币/阳光）作为商品的抽象。 */
public record MoneyGood(int amount) implements GoodKind {

    public static final MapCodec<MoneyGood> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
           Codec.INT.fieldOf("amount").forGetter(MoneyGood::amount)
    ).apply(i, MoneyGood::new));

    @Override public String type() { return "money"; }
    @Override public ItemStack createStack(HolderLookup.Provider r) { return ItemStack.EMPTY; }
    @Override public Component describe(ItemStack stack) {
        return Component.translatable("gui.pvz.shop.money_" + (amount / 1000));
    }
    @Override public int priority() { return 95; }
}