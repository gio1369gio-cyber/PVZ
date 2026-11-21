package com.lying.pvz.common.level.item.tool;

import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class ZombieDollItem extends Item {

	public ZombieDollItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		builder.accept(Component.translatable("tooltip.pvz.zombie_doll"));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) {
			return super.use(level, player, hand);
		}
		if (!level.isClientSide()) {
			EntityUtil.playSound(player, PvZSoundRegister.ZOMBIE_GROAN.get());
		}
		return InteractionResult.SUCCESS;
	}
}