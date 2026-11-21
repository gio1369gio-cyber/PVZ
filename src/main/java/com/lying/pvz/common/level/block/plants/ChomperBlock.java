package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * 食人花方块。
 *
 * <p>行为特性：
 * <ul>
 *     <li>无碰撞、无遮挡，形状为居中的 10×10×10 像素方块；</li>
 *     <li>动物和玩家进入方块时，每个游戏刻有 1/50 概率受到 8 点伤害；</li>
 *     <li>手持骨粉交互时，在周围 5×5 水平范围内以 20% 概率催生新的食人花，并消耗一份骨粉；</li>
 *     <li>放置时朝向玩家水平朝向的反方向；</li>
 *     <li>寻路代价为 {@link PathType#DAMAGING}，生物 AI 会尽量绕行。</li>
 * </ul>
 *
 * <p>僵尸不在伤害目标内，只有 {@link Animal} 和 {@link Player} 会受伤。
 */
public class ChomperBlock extends BushBlock {

	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);

	public ChomperBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}



	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}


	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
							 InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (level instanceof ServerLevel serverLevel) {
			if (entity instanceof Animal || entity instanceof Player) {
				if (!serverLevel.isClientSide()) {
					if (serverLevel.getRandom().nextInt(50) == 0) {
						entity.hurtServer(serverLevel, PvZEntityDamageSource.chomperPlant(), 8);
					}
				}
			}
		}
	}


	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!stack.is(Items.BONE_MEAL)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.isClientSide()) {
			for (int i = 0; i < 5; ++i) {
				WorldUtil.spawnRandomSpeedParticle(level, ParticleTypes.COMPOSTER, MathUtil.toVector(pos), 0.1F);
			}
		} else {
			for (int i = -2; i <= 2; ++i) {
				for (int k = -2; k <= 2; ++k) {
					final BlockPos tmp = WorldUtil.getSuitableHeightPos(level, pos.offset(i, 0, k)).below();
					if (Math.abs(pos.getY() - tmp.getY()) < 5 && mayPlaceOn(level.getBlockState(tmp), level, tmp)) {
						if (level.isEmptyBlock(tmp.above())) {
							if (level.getRandom().nextFloat() < 0.2F) {
								level.setBlockAndUpdate(tmp.above(),
										PvZBlockRegister.CHOMPER.get().defaultBlockState()
												.rotate(Rotation.getRandom(level.getRandom())));
							}
						}
					}
				}
			}
			stack.consume(1, player);
		}
		return InteractionResult.SUCCESS;
	}


	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}


	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return rotate(state,mirrorIn.getRotation(state.getValue(FACING)));
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
		return PathType.DAMAGING;
	}
}