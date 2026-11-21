package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 卷心菜作物方块。
 *
 * <p>继承 {@link CropBlock}，把默认的 8 阶段（{@code AGE_7}）缩减为 4 阶段（{@code AGE_3}），
 * 形状高度按阶段线性增长。
 * <p>随机生长有 1/4 概率被跳过，整体生长速度约为普通作物的 3/4；骨粉增龄量除以 3，效果被削弱。
 * <p>种子物品为卷心菜种子。
 */
public class CabbageBlock extends CropBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	private static final VoxelShape[] SHAPE = {
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
	};

	public CabbageBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected IntegerProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return PvZItemRegister.CABBAGE_SEEDS;
	}


	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextInt(4) != 0) {
			super.randomTick(state, level, pos, random);
		}
	}


	@Override
	protected int getBonemealAgeIncrease(Level level) {
		return super.getBonemealAgeIncrease(level) / 3;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE[state.getValue(this.getAgeProperty())];
	}
}