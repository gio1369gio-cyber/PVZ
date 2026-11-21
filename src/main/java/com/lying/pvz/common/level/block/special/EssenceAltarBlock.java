package com.lying.pvz.common.level.block.special;

import com.lying.pvz.common.level.blockentity.EssenceAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EssenceAltarBlock extends Block implements EntityBlock {

	private static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);

	public EssenceAltarBlock(Properties  properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.getBlockEntity(pos) instanceof EssenceAltarBlockEntity te && player instanceof ServerPlayer sp) {
			sp.openMenu(te, buf -> buf.writeBlockPos(pos));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
											   Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof EssenceAltarBlockEntity te && player instanceof ServerPlayer sp) {
			sp.openMenu(te, buf -> buf.writeBlockPos(pos));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EssenceAltarBlockEntity(pos, state);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null
				: (l, p, s, be) -> ((EssenceAltarBlockEntity) be).tick();
	}
}