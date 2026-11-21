package com.lying.pvz.common.level.item.tool.zombie;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.misc.BobsleCarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * 雪橇车（Bobsle Car）放置道具。
 *
 * <p><b>功能</b>：对空中方块顶面右键，在点击方块上方一格生成一辆雪橇车实体，
 * 朝向与玩家当前朝向一致，并消耗一个物品。
 *
 * <p><b>放置条件</b>（任一不满足则失败，不消耗物品）：
 * <ul>
 *   <li>副手不触发（仅主手有效）</li>
 *   <li>点击面必须是方块顶面（{@link Direction#UP}）</li>
 *   <li>点击方块上方一格必须为空（{@link Level#isEmptyBlock}）</li>
 *   <li>实体创建成功（{@code EntityType#create} 返回非 {@code null}）</li>
 * </ul>
 *
 * <p><b>服务端 / 客户端分工</b>：<br>
 * 生成实体、消耗物品、统计仅在服务端执行；
 * 客户端不执行副作用，但仍返回 {@link InteractionResult#SUCCESS} 以触发挥手动画。
 * 本道具在两端都返回 {@code SUCCESS}（即使条件失败），与原代码行为一致。
 *
 * <p><b>朝向</b>：雪橇车 yaw 直接继承玩家 yaw（{@link net.minecraft.world.entity.Entity#setYRot(float)}）。
 */
public class BobsleCarItem extends Item {

    private static final double SPAWN_Y_OFFSET = 1.0D;


    private static final double SPAWN_CENTER = 0.5D;

    public BobsleCarItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();
        if (player == null || hand == InteractionHand.OFF_HAND) {
            return InteractionResult.FAIL;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (!level.isClientSide()
                && context.getClickedFace() == Direction.UP
                && level.isEmptyBlock(pos.above())) {

            BobsleCarEntity car = PvZEntityRegister.BOBSLE_CAR.get()
                    .create(level, EntitySpawnReason.TRIGGERED);
            if (car == null) {
                return InteractionResult.FAIL;
            }

            stack.shrink(1);
            car.setYRot(player.getYRot());
            car.setPos(pos.getX() + SPAWN_CENTER, pos.getY() + SPAWN_Y_OFFSET, pos.getZ() + SPAWN_CENTER);
            level.addFreshEntity(car);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResult.SUCCESS;
    }
}