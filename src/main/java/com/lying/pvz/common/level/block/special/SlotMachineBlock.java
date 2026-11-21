package com.lying.pvz.common.level.block.special;

import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.level.block.AbstractFacingBlock;
import com.lying.pvz.common.level.blockentity.SlotMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SlotMachineBlock extends AbstractFacingBlock implements EntityBlock {

	public SlotMachineBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.getBlockEntity(pos) instanceof SlotMachineBlockEntity te && te.getLottery() != null) {
			if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				serverPlayer.openMenu(te, buf -> buf.writeBlockPos(pos));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
											   Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof SlotMachineBlockEntity te && te.getLottery() != null) {
			if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				serverPlayer.openMenu(te, buf -> buf.writeBlockPos(pos));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state,
							@Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (level.getBlockEntity(pos) instanceof SlotMachineBlockEntity te) {
			te.setCustomName(stack.getHoverName());
			te.init(stack.get(PvZDataComponents.LOTTERY_TYPE.get()));
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SlotMachineBlockEntity(pos, state);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		final ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
		if (includeData && level.getBlockEntity(pos) instanceof SlotMachineBlockEntity te) {
			final var lottery = te.getLottery();
			if (lottery != null) {
				stack.set(PvZDataComponents.LOTTERY_TYPE.get(), lottery.id());
			}
		}
		return stack;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null
				: (l, p, s, be) -> ((SlotMachineBlockEntity) be).tick();
	}
}