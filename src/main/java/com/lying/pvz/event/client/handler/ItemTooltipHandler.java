package com.lying.pvz.event.client.handler;

import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ItemTooltipHandler {

    public static void toolTip(ItemTooltipEvent event) {
        final ItemStack stack = event.getItemStack();

        if (stack.getItem() == PvZItemRegister.ORIGIN_SHOVEL.get()) {
            event.getToolTip().add(Component.translatable("tooltip.pvz.origin_shovel").withStyle(ChatFormatting.DARK_GREEN));
        }
        if (stack.getItem() == PvZItemRegister.ORIGIN_SWORD.get()) {
            event.getToolTip().add(Component.translatable("tooltip.pvz.origin_sword").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    public static void cardTip(ItemTooltipEvent event) {
        final ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof AbstractCardItem card)) {
            return;
        }
        final Player player = event.getEntity();
        if (player == null) {
            return;
        }
        if (!isLocked(card, player, stack)) {
            return;
        }
        if (ConfigUtil.needUnlockToPlant()) {
            event.getToolTip().add(
                    Component.translatable("tooltip.pvz.card_required_level", card.getRequiredLevel(stack))
                            .withStyle(ChatFormatting.RED));
        }
    }

    /**
     * 直接按解锁状态判定，忽略创造模式放行。
     */
    private static boolean isLocked(AbstractCardItem card, Player player, ItemStack stack) {
        for (IPAZType type : card.getLockCheckTypes(stack)) {
            if (PlayerUtil.isPAZLocked(player, type)) {
                return true;
            }
        }
        return false;
    }
}
