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
 * 气球道具。
 *
 * <p><b>功能</b>：右键使用，消耗阳光为使用者挂上长时间 {@link MobEffects#SLOW_FALLING SLOW_FALLING}，
 * 用于减速下落、避免摔落伤害。
 *
 * <p><b>消耗</b>：
 * <ul>
 *   <li>{@value #SUN_COST} 点阳光（来自 {@link Resources#SUN_NUM}，通过 {@code PlayerDataManager} 读写）</li>
 *   <li>1 点耐久（仅生存模式，创造模式不扣）</li>
 *   <li>进入 {@value #EFFECT_CD} + {@value #BREAK_CD_EXTRA} tick 的冷却（仅生存模式）</li>
 * </ul>
 *
 * <p><b>服务端 / 客户端分工</b>：<br>
 * {@link #use} 在两端都会被调用。
 * 服务端负责实际扣资源、加效果、上冷却、扣耐久；
 * 客户端不执行副作用，但仍返回 {@link InteractionResult#SUCCESS} 以触发挥手动画。
 *
 * <p><b>创造模式</b>：阳光仍会被扣（写进 PDM），但不扣耐久、不上冷却。
 * 若希望创造模式也免阳光，需要额外判断。
 */
public class BalloonItem extends Item {

    private static final int SUN_COST = 25;

    private static final int EFFECT_CD = 200;


    private static final int EFFECT_AMPLIFIER = 10;

    private static final int BREAK_CD_EXTRA = 80;

    public BalloonItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("tooltip.pvz.balloon")
                .withStyle(ChatFormatting.AQUA));
        builder.accept(Component.translatable("tooltip.pvz.sun_cost", SUN_COST)
                .withStyle(ChatFormatting.YELLOW));
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (PlayerUtil.getManager(player).getResource(Resources.SUN_NUM) >= SUN_COST) {
            if (!level.isClientSide()) {
                PlayerUtil.getManager(player).addResource(Resources.SUN_NUM, -SUN_COST);

                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING,
                        EFFECT_CD,
                        EFFECT_AMPLIFIER));

                if (PlayerUtil.isPlayerSurvival(player)) {
                    player.getCooldowns().addCooldown(
                            player.getItemInHand(hand).getItem().getDefaultInstance(),
                            EFFECT_CD + BREAK_CD_EXTRA);
                    player.getItemInHand(hand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}