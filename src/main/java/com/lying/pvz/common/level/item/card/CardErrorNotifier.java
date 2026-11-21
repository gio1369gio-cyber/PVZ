package com.lying.pvz.common.level.item.card;

import com.lying.pvz.api.card.ICardError;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CardErrorNotifier {
    public static void send(Player player, ItemStack stack, ICardError error, Object... args) {
        if (player.level().isClientSide()) return;
        player.sendSystemMessage(error.format(args));
    }
}
