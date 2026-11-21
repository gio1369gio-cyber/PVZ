package com.lying.pvz.common.level.item.blockitem;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/**
 * 老虎机物品。放置流程完全交给 {@link BlockItem#place}，本类只携带两个组件：
 * {@link PvZDataComponents#LOTTERY_TYPE} 与原版 {@code CUSTOM_NAME}。
 */
public class SlotMachineItem extends BlockItem {

    public SlotMachineItem(Properties properties) {
        super(PvZBlockRegister.SLOT_MACHINE.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        final Identifier res = stack.get(PvZDataComponents.LOTTERY_TYPE.get());
        if (res == null) {
            builder.accept(Component.translatable("tooltip.pvz.slot_machine.random")
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        } else {
            builder.accept(Component.literal(res.toString())
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}