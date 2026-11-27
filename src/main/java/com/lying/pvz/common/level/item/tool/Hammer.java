package com.lying.pvz.common.level.item.tool;


import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class Hammer extends Item {
    public Hammer(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.getCooldowns().addCooldown(PvZItemRegister.HAMMER.toStack(), 10);
        player.level().playSound(null, player.blockPosition(), PvZSoundRegister.BUZZER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                  LivingEntity interactionTarget, InteractionHand usedHand) {
        //检查是否有CD
        if (player.getCooldowns().isOnCooldown(PvZItemRegister.HAMMER.toStack())){
            //如果有CD则结束方法
            return InteractionResult.FAIL;
        } else if (interactionTarget instanceof PVZZombieEntity) {
            //如果没有CD就执行一下语句
            //对PVZ僵尸照成20点伤害
            DamageSource source =  player.level().damageSources().playerAttack(player);
            interactionTarget.hurt(source, 20.0F);
            player.level().playSound(null, player.blockPosition(), PvZSoundRegister.HAMMER_BONK.get(),SoundSource.PLAYERS, 1.0F, 1.0F);
            //播放音效
            //减少锤子5点耐久
            stack.hurtAndBreak(5, interactionTarget, EquipmentSlot.MAINHAND);
            //如果是生存模式添加30tick的CD
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(PvZItemRegister.HAMMER.toStack(), 30);
            }
            player.level().playSound(null, player.blockPosition(), PvZSoundRegister.BUZZER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("tooltip.pvz.hammer.use").withStyle(ChatFormatting.GOLD));
    }

}
