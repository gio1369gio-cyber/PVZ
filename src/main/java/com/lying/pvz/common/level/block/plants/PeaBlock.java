package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 豌豆作物方块。
 *
 * <p>继承 {@link CropBlock}，生长过程使用 8 个阶段的高度渐变形状；成熟后右键可收获，掉落 1 个豌豆并把方块重置回阶段 0。
 * <p>非成熟状态下的交互（种植种子、骨粉催熟）完全交给 {@link CropBlock} 的默认逻辑。
 */
public class PeaBlock extends CropBlock {


	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
	};

	public PeaBlock(Properties builder) {
		super(builder);
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}


	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (this.isMaxAge(state)) {
			if (!level.isClientSide()) {
				Block.popResource(level, pos, new ItemStack(PvZItemRegister.PEA.get(), 1));
				level.setBlockAndUpdate(pos, this.getStateForAge(0));
			}
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}


	@Override
	protected ItemLike getBaseSeedId() {
		return PvZItemRegister.PEA.get();
	}
}