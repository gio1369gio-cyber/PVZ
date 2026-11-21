package com.lying.pvz.common.payload;

import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.common.level.blockentity.SlotMachineBlockEntity;
import com.lying.pvz.common.menu.CardFusionMenu;
import com.lying.pvz.common.menu.EssenceAltarMenu;
import com.lying.pvz.common.menu.FragmentSpliceMenu;
import com.lying.pvz.common.menu.SlotMachineMenu;
import com.lying.pvz.common.menu.shop.AbstractDaveShopMenu;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * GUI 按钮点击包。
 *
 * <p><b>字段语义</b>（按 {@code guiType} 区分）：
 * <ul>
 *   <li>{@code SHOP}：{@code num} = 商品稳定 key（{@link AbstractDaveShopMenu.GoodEntry#key()}）</li>
 *   <li>{@code SLOT_MACHINE}：{@code op} = 0 慢速 / 1 快速</li>
 *   <li>{@code FRAGMENT_SPLICE} / {@code CARD_FUSION}：{@code op} = 0（合成）</li>
 *   <li>{@code ESSENCE_ALTAR}：{@code op} = 技能索引</li>
 * </ul>
 *
 * <p>所有菜单操作统一走 {@link AbstractContainerMenu#clickMenuButton}，
 * 校验与状态变更都在菜单内部完成，本 Handler 只负责分发与必要的失败反馈。
 */
public record ClickButtonPayload(int guiType, int op, int num) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClickButtonPayload> TYPE =
            new CustomPacketPayload.Type<>(StringUtil.id("click_button"));

    public static final StreamCodec<FriendlyByteBuf, ClickButtonPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClickButtonPayload::guiType,
            ByteBufCodecs.VAR_INT, ClickButtonPayload::op,
            ByteBufCodecs.VAR_INT, ClickButtonPayload::num,
            ClickButtonPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {

        public static void handle(ClickButtonPayload message, IPayloadContext context) {
            if (!(context.player() instanceof ServerPlayer player)) return;

            context.enqueueWork(() -> {
                final AbstractContainerMenu menu = player.containerMenu;

                if (message.guiType() == GuiHandler.SHOP) {
                    if (menu instanceof AbstractDaveShopMenu shop) {
                        // num = 商品稳定 key，与 getDisplayGoods 的 GoodEntry.key 一致
                        if (!shop.clickMenuButton(player, message.num())) {
                            PlayerUtil.sendMsgTo(player,
                                    Component.translatable("help.pvz.shop.purchase_failed")
                                            .withStyle(ChatFormatting.RED));
                        }
                    }
                } else if (message.guiType() == GuiHandler.FRAGMENT_SPLICE) {
                    if (menu instanceof FragmentSpliceMenu splice && message.op() == 0) {
                        splice.clickMenuButton(player, 0);
                    }
                } else if (message.guiType() == GuiHandler.SLOT_MACHINE) {
                    if (menu instanceof SlotMachineMenu slot) {
                        final SlotMachineBlockEntity te = slot.getTe();
                        if (te == null) return;

                        // 前置原因反馈（te.canRun 与菜单内校验一致）
                        if (!te.canRun(player)) {
                            if (PlayerUtil.getResource(player, Resources.LOTTERY_CHANCE) <= 0) {
                                PlayerUtil.sendMsgTo(player,
                                        Component.translatable("help.pvz.out_of_lottery_chance")
                                                .withStyle(ChatFormatting.RED));
                            } else if (PlayerUtil.getResource(player, Resources.SUN_NUM) < te.getSunCost()) {
                                PlayerUtil.sendMsgTo(player,
                                        Component.translatable("help.pvz.not_enough_sun")
                                                .withStyle(ChatFormatting.RED));
                            } else {
                                PlayerUtil.sendMsgTo(player,
                                        Component.translatable("help.pvz.slot_machine.running")
                                                .withStyle(ChatFormatting.RED));
                            }
                            return;
                        }
                        slot.clickMenuButton(player, message.op());
                    }
                } else if (message.guiType() == GuiHandler.ESSENCE_ALTAR) {
                    if (menu instanceof EssenceAltarMenu altar) {
                        altar.learnSkillAt(message.op());
                    }
                } else if (message.guiType() == GuiHandler.CARD_FUSION) {
                    if (menu instanceof CardFusionMenu fusion && message.op() == 0) {
                        fusion.clickMenuButton(player, 0);
                    }
                }
            });
        }
    }
}