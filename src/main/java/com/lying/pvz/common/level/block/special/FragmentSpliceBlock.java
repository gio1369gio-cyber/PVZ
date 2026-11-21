package com.lying.pvz.common.level.block.special;

import com.lying.pvz.common.level.blockentity.FragmentSpliceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FragmentSpliceBlock extends Block implements EntityBlock {

	public FragmentSpliceBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.getBlockEntity(pos) instanceof FragmentSpliceBlockEntity te && player instanceof ServerPlayer sp) {
			sp.openMenu(te, buf -> buf.writeBlockPos(pos));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
											   Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof FragmentSpliceBlockEntity te && player instanceof ServerPlayer sp) {
			sp.openMenu(te, buf -> buf.writeBlockPos(pos));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FragmentSpliceBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null
				: (l, p, s, be) -> ((FragmentSpliceBlockEntity) be).tick();
	}

}