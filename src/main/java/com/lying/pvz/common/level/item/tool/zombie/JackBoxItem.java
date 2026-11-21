package com.lying.pvz.common.level.item.tool.zombie;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.api.lottery.SlotEntry;
import com.lying.pvz.common.datapack.LotteryTypeLoader;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import java.util.function.Consumer;

/**
 * 惊吓盒（Jack-in-the-Box）道具。
 *
 * <p><b>功能</b>：右键使用后随机触发两种效果之一，无论结果如何都消耗一个：
 * <ul>
 *   <li><b>爆炸</b>（概率 {@code 1 / JackBoxSurpriseChance}）：
 *       播放惊吓音效，并在玩家位置产生半径 3 的爆炸。
 *       若 {@link EventHooks#canEntityGrief canEntityGrief} 为 {@code false}（例如刷怪规则关闭破坏），
 *       则爆炸不破坏方块（{@code ExplosionInteraction.NONE}）；
 *       否则破坏方块（{@code ExplosionInteraction.BLOCK}）。</li>
 *   <li><b>抽奖</b>（其余概率）：
 *       播放音乐盒音效，从 {@code pvz:all_plants} 抽奖池随机抽一个植物条目，
 *       复制为物品后加入玩家背包（满包时由 {@link Player#addItem} 自动掉落）。</li>
 * </ul>
 *
 * <p><b>消耗</b>：使用后立即 {@code stack.shrink(1)}，与结果无关。
 *
 * <p><b>服务端 / 客户端分工</b>：<br>
 * 副作用（音效、爆炸、抽奖、消耗物品）仅在服务端执行；
 * 客户端返回 {@link InteractionResult#SUCCESS} 以触发挥手。
 *
 * <p><b>抽奖池</b>：{@code LotteryTypeLoader.getLotteryType(ALL_PLANTS_ID)}。
 * 若对应 {@link Lottery} 未加载，抽奖分支静默跳过（仅播音乐，不产出物品）。
 */
public class JackBoxItem extends Item {

    /**
     * 抽奖使用的抽奖池 ID。需与 datapack 中 {@code all_plants} 抽奖类型注册的 ID 一致。
     */
    private static final Identifier ALL_PLANTS_ID = Identifier.fromNamespaceAndPath("pvz", "all_plants");

    /**
     * 爆炸半径。
     */
    private static final float EXPLOSION_RADIUS = 3.0F;

    public JackBoxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            int surpriseChance = PvZConfig.LOCAL_CONFIG.ItemSettings.JackBoxSurpriseChance.get();
            if (player.getRandom().nextInt(Math.max(1, surpriseChance)) == 0) {
                PlayerUtil.playClientSound(player, PvZSoundRegister.JACK_SURPRISE.get());

                if (level instanceof ServerLevel serverLevel) {
                    Level.ExplosionInteraction interaction = EventHooks.canEntityGrief(serverLevel, player)
                            ? Level.ExplosionInteraction.BLOCK
                            : Level.ExplosionInteraction.NONE;

                    level.explode(player, player.getX(), player.getY(), player.getZ(),
                            EXPLOSION_RADIUS, interaction);
                }
            } else {
                PlayerUtil.playClientSound(player, PvZSoundRegister.JACK_MUSIC.get());

                LotteryTypeLoader.getLotteryType(ALL_PLANTS_ID).ifPresent(lottery -> {
                    int slotCount = lottery.slotCount();
                    if (slotCount <= 0) {
                        return;
                    }
                    int idx = player.getRandom().nextInt(slotCount);
                    SlotEntry entry = lottery.getSlotType(idx);
                    if (entry == null) {
                        return;
                    }
                    ItemStack entryStack = entry.createStack();
                    if (entryStack != null && !entryStack.isEmpty()) {
                        player.addItem(entryStack);
                    }
                });
            }
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.pvz.jack_box").withStyle(ChatFormatting.RED));
    }
}