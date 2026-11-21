package com.lying.pvz.event.common.handler;

import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 热键栏卡片槽位限制。
 *
 */
public final class HotbarCardSlotsHandler {

    private HotbarCardSlotsHandler() { }

    /* ---------------- 拾取拦截 ---------------- */


    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
        if (sp.isCreative() || sp.isSpectator()) return;
        final ItemStack stack = event.getItemEntity().getItem();
        if (!isCardItem(stack)) return;

        final int slots = getSlotNum(sp);
        final Inventory inv = sp.getInventory();

        // 1. 热键栏已有同类卡可堆叠 → 放行
        for (int i = 0; i < 9; i++) {
            final ItemStack cur = inv.getItem(i);
            if (isCardItem(cur) && ItemStack.isSameItemSameComponents(cur, stack)
                    && cur.getCount() < cur.getMaxStackSize()) {
                return;
            }
        }
        // 2. 热键栏还有空位且未达到卡片槽上限 → 放行（不限定具体槽位）
        if (countCardSlots(inv) < slots) {
            for (int i = 0; i < 9; i++) {
                if (inv.getItem(i).isEmpty()) {
                    return;
                }
            }
        }
        // 3. 背包有空间 → 放行（卡片进背包）
        for (int i = 9; i < 36; i++) {
            final ItemStack cur = inv.getItem(i);
            if (cur.isEmpty()) {
                return;
            }
            if (ItemStack.isSameItemSameComponents(cur, stack)
                    && cur.getCount() < cur.getMaxStackSize()) {
                return;
            }
        }
        // 4. 无处可放 → 拒绝
        event.setCanPickup(TriState.FALSE);
    }

    /* ---------------- tick 兜底扫描 ---------------- */


    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (sp.isCreative() || sp.isSpectator()) return;
        if (sp.tickCount % 20 != 0) return;

        final int slots = getSlotNum(sp);
        final Inventory inv = sp.getInventory();

        int cardSlots = countCardSlots(inv);
        if (cardSlots <= slots) {
            return;
        }

        for (int i = 0; i < 9 && cardSlots > slots; i++) {
            final ItemStack stack = inv.getItem(i);
            if (!isCardItem(stack)) continue;

            // 先尝试移入背包
            final ItemStack toMove = stack.copy();
            if (addToBackpack(inv, toMove)) {
                inv.setItem(i, toMove);
            } else {
                // 背包满 → 掉在原地
                final ItemEntity dropped = sp.drop(stack.copy(), false, Prediction.SERVER_ONLY);
                if (dropped != null) {
                    dropped.setPickUpDelay(40);
                }
                inv.setItem(i, ItemStack.EMPTY);
                PlayerUtil.sendMsgTo(sp,
                       Component.translatable("pvz.card.slot_locked")
                                .withStyle(net.minecraft.ChatFormatting.RED));
            }
            cardSlots--;
        }
    }

    /* ---------------- helpers ---------------- */

    private static boolean isCardItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof AbstractCardItem;
    }

    /** 热键栏（0~8）里当前占用了几格卡片槽。 */
    private static int countCardSlots(Inventory inv) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (isCardItem(inv.getItem(i))) {
                count++;
            }
        }
        return count;
    }

    private static int getSlotNum(ServerPlayer sp) {
        return sp.getData(PvZAttachments.PLAYER_STATS).get(Resources.SLOT_NUM);
    }


    /**
     * 把 {@code stack} 塞入背包（{@code 9~35}）。成功则 {@code stack} 变空并返回 {@code true}。
     */
    private static boolean addToBackpack(Inventory inv, ItemStack stack) {
        // 先堆叠
        for (int i = 9; i < 36; i++) {
            final ItemStack cur = inv.getItem(i);
            if (cur.isEmpty()) continue;
            if (!ItemStack.isSameItemSameComponents(cur, stack)) continue;
            final int space = cur.getMaxStackSize() - cur.getCount();
            if (space <= 0) continue;
            final int move = Math.min(space, stack.getCount());
            cur.grow(move);
            stack.shrink(move);
            if (stack.isEmpty()) return true;
        }
        // 再找空位
        for (int i = 9; i < 36; i++) {
            if (inv.getItem(i).isEmpty()) {
                inv.setItem(i, stack.copy());
                stack.setCount(0);
                return true;
            }
        }
        return false;
    }
}