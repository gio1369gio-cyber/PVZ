package com.lying.pvz.common.level.item.blockitem;

import com.lying.pvz.common.init.PvZBlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * 纱门（Screen Door）道具——可当作盾牌使用的防护道具。
 *
 *
 * <p><b>格挡机制</b>：由 {@code BLOCKS_ATTACKS} 数据组件驱动，无需覆写任何方法——
 * 使用时长覆盖仅为延长可连续格挡的窗口。
 */
public class ScreenDoorItem extends BlockItem {

    /**
     * 单次格挡最长持续时间（tick）。144000 tick = 2 小时，远超常规战斗时长，等效"无上限"。
     */
    private static final int USE_DURATION = 144_000;

    public ScreenDoorItem(Properties properties) {
        super(PvZBlockRegister.SCREEN_DOOR.get(), properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos clicked = context.getClickedPos();
		Direction face = context.getClickedFace();
		BlockPos placePos = clicked.relative(face);

		if (face != Direction.UP || !level.getBlockState(placePos).canBeReplaced()
				|| !level.getBlockState(placePos.above()).canBeReplaced()) {
			return InteractionResult.PASS;
		}

		// 构造标准放置上下文，让 DoorBlock 依据玩家朝向计算 facing / hinge。
		BlockPlaceContext placement = new BlockPlaceContext(context);
		DoorBlock door = PvZBlockRegister.SCREEN_DOOR.get();
		BlockState lower = door.getStateForPlacement(placement);
		if (lower == null) {
			return InteractionResult.PASS;
		}

		Player player = context.getPlayer();
		ItemStack held = context.getItemInHand();

		if (!level.setBlock(placePos, lower, Block.UPDATE_ALL_IMMEDIATE)) {
			return InteractionResult.PASS;
		}
		// 上半块：同朝向、同合页，HALF 为 UPPER。
		BlockState upper = lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
		level.setBlock(placePos.above(), upper, Block.UPDATE_ALL_IMMEDIATE);

		door.setPlacedBy(level, placePos, lower, player, held);
		level.levelEvent(player, 2005, placePos, 0);

		if (player != null && !player.getAbilities().instabuild) {
			held.shrink(1);
		}
		return InteractionResult.SUCCESS;
	}
}
