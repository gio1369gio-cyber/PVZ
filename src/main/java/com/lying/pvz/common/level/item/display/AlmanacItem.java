package com.lying.pvz.common.level.item.display;

import com.lying.pvz.common.menu.AlmanacMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 图鉴（Almanac）道具。
 *
 * <p><b>功能</b>：右键打开 {@link AlmanacMenu}，查看植物图鉴。
 *
 * <p><b>服务端 / 客户端</b>：打开菜单只在服务端执行。
 *
 * <p><b>返回值语义</b>：返回 {@link InteractionResult#CONSUME}。
 * <ul>
 *   <li>不挥手（与旧版 {@code PASS} 视觉效果一致）</li>
 *   <li>阻止副手继续执行 {@code use} —— 原 {@code PASS} 在主副手都持图鉴时会重复 {@code openMenu}，
 *       表现为容器开→关→开，有闪烁</li>
 * </ul>
 */
public class AlmanacItem extends Item {

    public AlmanacItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player p) {
                    return new AlmanacMenu(id, p);
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("gui.pvz.almanac.show");
                }
            });
        }
        return InteractionResult.CONSUME;
    }
}