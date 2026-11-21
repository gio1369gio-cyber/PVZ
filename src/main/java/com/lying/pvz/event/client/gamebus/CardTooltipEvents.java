package com.lying.pvz.event.client.gamebus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.event.client.handler.ItemTooltipHandler;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * 卡片锁定行 tooltip。
 * <p>tooltip 只在客户端渲染，故 {@code value = Dist.CLIENT}。
 * 创造模式也显示锁定提示：不走 {@code isLocked}（其内部对创造玩家直接放行）。
 */
@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public final class CardTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemTooltipHandler.toolTip(event);
        ItemTooltipHandler.cardTip(event);
    }


}