package com.lying.pvz.api.transaction;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 商店中一"类"商品的语义。每次 {@link #createStack} 必须返回新实例。
 */
public interface GoodKind {

    /** dispatch 键，全局唯一（"item" / "energy" / "slot" / "money"）。 */
    String type();

    /** 生成"全新"的展示/交付栈。不可返回共享 ItemStack。 */
    ItemStack createStack(HolderLookup.Provider registries);

    /** 用于 GUI 描述（不能依赖 stack 的 hoverName 时用此方法）。 */
    Component describe(ItemStack stack);

    /** 排序优先级（越高越靠前）。 */
    default int priority() { return 0; }

    /** 该商品对指定玩家是否可购买。 */
    default boolean isValidFor(Player player) { return true; }
}