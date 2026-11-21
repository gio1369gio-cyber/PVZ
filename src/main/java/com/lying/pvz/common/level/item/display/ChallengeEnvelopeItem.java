package com.lying.pvz.common.level.item.display;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.client.gui.EnvelopeClientBridge;
import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class ChallengeEnvelopeItem extends Item {

    public ChallengeEnvelopeItem(Properties properties) {
        super(properties);
    }

    // ============================ 组件读写 ============================

    @Nullable
    public static Identifier getChallengeType(ItemStack stack) {
        return stack.get(PvZDataComponents.CHALLENGE_TYPE.get());
    }

    public static Optional<IChallengeDefinition> getRaidComponent(ItemStack stack) {
        Identifier id = getChallengeType(stack);
        if (id == null) return Optional.empty();
        return PVZAPI.raid().getDefinition(id);
    }

    public static ItemStack setChallengeType(ItemStack stack, Identifier id) {
        stack.set(PvZDataComponents.CHALLENGE_TYPE.get(), id);
        return stack;
    }

    public static ItemStack getChallengeEnvelope(Identifier id) {
        return setChallengeType(new ItemStack(PvZItemRegister.CHALLENGE_ENVELOPE.get()), id);
    }

    // ============================ Tooltip ============================

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.pvz.challenge_envelope1").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("tooltip.pvz.challenge_envelope2").withStyle(ChatFormatting.GREEN));

        final Identifier id = getChallengeType(stack);
        if (id == null) {
            builder.accept(Component.translatable("tooltip.pvz.challenge_envelope.empty")
                    .withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
            return;
        }

        builder.accept(Component.literal(id.toString())
                .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

        final Optional<IChallengeDefinition> opt = PVZAPI.raid().getDefinition(id);
        if (opt.isEmpty()) {
            builder.accept(Component.translatable("tooltip.pvz.challenge_envelope.unloaded")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        builder.accept(opt.get().getName().copy().withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
    }

    // ============================ 右键空气 ============================

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // 客户端开屏；服务端只消费交互，避免走默认右键行为
        if (level.isClientSide()) {
            EnvelopeClientBridge.open(player.getItemInHand(hand), hand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    // ============================ 对方块右键 ============================

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        final Player player = context.getPlayer();
        if (player == null) return InteractionResult.CONSUME;

        final ItemStack stack = context.getItemInHand();

        if (context.getClickedFace() != Direction.UP) {
            PlayerUtil.sendMsgTo(player,
                    Component.translatable("help.pvz.challenge_not_top").withStyle(ChatFormatting.RED));
            return InteractionResult.CONSUME;
        }

        final Identifier id = getChallengeType(stack);
        if (id == null) {
            PlayerUtil.sendMsgTo(player,
                    Component.translatable("help.pvz.no_challenge").withStyle(ChatFormatting.RED));
            return InteractionResult.CONSUME;
        }
        if (PVZAPI.raid().getDefinition(id).isEmpty()) {
            PlayerUtil.sendMsgTo(player,
                    Component.translatable("help.pvz.no_challenge").withStyle(ChatFormatting.RED));
            return InteractionResult.CONSUME;
        }

        final ServerLevel serverLevel = (ServerLevel) level;
        final BlockPos spawnPos = context.getClickedPos().above();

        if (PVZAPI.raid().getNearByRaid(serverLevel, spawnPos).isPresent()) {
            PlayerUtil.sendMsgTo(player,
                    Component.translatable("help.pvz.full_challenge").withStyle(ChatFormatting.RED));
            return InteractionResult.CONSUME;
        }

        if (player instanceof ServerPlayer sp && PVZAPI.raid().create(serverLevel, id, spawnPos, sp)) {
            if (PlayerUtil.isPlayerSurvival(player)) {
                stack.shrink(1);
            }
        } else {
            PlayerUtil.sendMsgTo(player,
                    Component.translatable("help.pvz.wrong_challenge").withStyle(ChatFormatting.RED));
        }
        return InteractionResult.CONSUME;
    }
}