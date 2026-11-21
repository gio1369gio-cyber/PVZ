package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

/**
 * 毒蘑菇方块。
 *
 * <p>生长在菌丝上，4 个生长阶段；天空光照必须低于 {@link #VALID_LIGHT_LEVEL} 才能存活与生长。
 * <p>完全成熟（阶段 3）后：
 * <ul>
 *     <li>非怪物生物在方块上移动时附加中毒；</li>
 *     <li>方块持续散发 {@link ParticleTypes#PORTAL} 粒子。</li>
 * </ul>
 * <p>非怪物生物在方块上都会被黏住减速，怪物免疫，避免破坏寻路。
 * <p>右键在阶段 2 / 3 可收获孢子并重置到阶段 1；骨粉可催熟。
 */
public class ToxicShroomBlock extends BushBlock implements BonemealableBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	private static final VoxelShape[] STAGES = {
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 11.0D, 15.0D)
	};

	private static final int VALID_LIGHT_LEVEL = 7;
	private static final int POISON_TICK = 100;
	private static final int POISON_LVL = 1;

	public ToxicShroomBlock(Properties properties) {
		super(properties.randomTicks());
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}


	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		return new ItemStack(PvZItemRegister.SPORE.get());
	}


	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(Blocks.MYCELIUM);
	}


	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		final BlockPos belowPos = pos.below();
		final BlockState belowState = level.getBlockState(belowPos);
		final TriState soil = belowState.canSustainPlant(level, belowPos, Direction.UP, state);
		final boolean lightOk = level.getBrightness(LightLayer.SKY, pos) < VALID_LIGHT_LEVEL;
		if (soil.isDefault()) {
			return this.mayPlaceOn(belowState, level, belowPos) && lightOk;
		}
		return soil.isTrue() && this.mayPlaceOn(belowState, level, belowPos) && lightOk;
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return STAGES[state.getValue(AGE)];
	}


	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		final int age = state.getValue(AGE);
		if (age < 3
				&& level.getRawBrightness(pos, 0) <= VALID_LIGHT_LEVEL
				&& CommonHooks.canCropGrow(level, pos, state, random.nextInt(10) == 0)) {
			level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
			CommonHooks.fireCropGrowPost(level, pos, state);
		}
	}


	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(AGE) != 3) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			level.addParticle(ParticleTypes.PORTAL,
					pos.getX(), pos.getY(), pos.getZ(),
					(random.nextFloat() - 0.5) / 5,
					(random.nextFloat() - 0.5) / 10,
					(random.nextFloat() - 0.5) / 5);
		}
	}


	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
								InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (!(entity instanceof LivingEntity living) || entity instanceof Monster) {
			return;
		}
		entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
		if (level.isClientSide() || state.getValue(AGE) != 3) {
			return;
		}
		final double dx = Math.abs(entity.getX() - entity.xOld);
		final double dz = Math.abs(entity.getZ() - entity.zOld);
		if (dx >= 0.003F || dz >= 0.003F) {
			living.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_TICK, POISON_LVL, false, false));
		}
	}


	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hitResult) {
		final int age = state.getValue(AGE);
		if (age < 3 && stack.is(Items.BONE_MEAL)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (age > 1) {
			if (!level.isClientSide()) {
				Block.popResource(level, pos, new ItemStack(PvZItemRegister.SPORE.get(), 1));
				level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
						SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
				level.setBlock(pos, state.setValue(AGE, 1), Block.UPDATE_CLIENTS);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, BonemealSource source) {
		return state.getValue(AGE) < 3;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
		final int next = Math.min(3, state.getValue(AGE) + 1);
		level.setBlock(pos, state.setValue(AGE, next), Block.UPDATE_CLIENTS);
	}
}