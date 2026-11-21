package com.lying.pvz.common.level.item.tool.zombie;

import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * 僵尸旗帜（Zombie Flag）道具。
 *
 * <p><b>功能</b>：右键使用，消耗阳光为使用者挂上 {@link MobEffects#SPEED MOVEMENT_SPEED}
 * 增益，用于短时间提速。
 */
public class ZombieFlagItem extends Item {


	private static final int SUN_COST = 25;


	private static final int EFFECT_CD = 400;


	private static final int EFFECT_AMPLIFIER = 1;

	private static final int BREAK_CD_EXTRA = 80;

	public ZombieFlagItem(Properties properties) {
		super(properties);
	}


	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		builder.accept(Component.translatable("tooltip.pvz.zombie_flag")
				.withStyle(ChatFormatting.AQUA));
		builder.accept(Component.translatable("tooltip.pvz.sun_cost", SUN_COST)
				.withStyle(ChatFormatting.YELLOW));
	}


	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (PlayerUtil.getManager(player).getResource(Resources.SUN_NUM) < SUN_COST) {
			return InteractionResult.FAIL;
		}
		if (!level.isClientSide()) {
			PlayerUtil.getManager(player).addResource(Resources.SUN_NUM, -SUN_COST);

			player.addEffect(new MobEffectInstance(
					MobEffects.SPEED,
					EFFECT_CD,
					EFFECT_AMPLIFIER));

			if (PlayerUtil.isPlayerSurvival(player)) {
				player.getCooldowns().addCooldown(
						player.getItemInHand(hand).getItem().getDefaultInstance(),
						EFFECT_CD + BREAK_CD_EXTRA);
				player.getItemInHand(hand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			}
		}
		return InteractionResult.SUCCESS;
	}
}