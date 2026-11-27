package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.level.entity.plant.spear.CatTailEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.pool.ZombieDolphinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;


/**
 * PVZ荷叶方块。
 * <p>
 * 继承 {@link BushBlock}，带水平朝向状态 {@link #FACING}，使用 1.5/16 高的薄片碰撞/渲染形状。
 * 可放置在任意非空气方块上；服务端下会被可破坏植物的僵尸或未免疫虚弱的猫尾草破坏。
 */
public class PvZLilyPadBlock extends BushBlock {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape LILY_PAD_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.5D, 15.0D);

    public PvZLilyPadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        if (level instanceof ServerLevel) {
            if ((entity instanceof PVZZombieEntity && !(entity instanceof ZombieDolphinEntity))) {
                if (entity.isAlive() && ((PVZZombieEntity) entity).canBreakPlantBlock()) {
                    level.destroyBlock(pos, true, entity);
                }
            } else if (entity instanceof CatTailEntity && !((CatTailEntity) entity).isImmuneToWeak()) {
                level.destroyBlock(pos, true, entity);
            }
        }
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return LILY_PAD_AABB;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.isAir();
    }

    public BlockState getStateForPlacement(Player player) {
        if (player == null) return this.defaultBlockState();
        return this.defaultBlockState().setValue(FACING, player.getDirection().getOpposite());
    }

    public BlockState getStateForPlacement(Direction direction) {
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public @NonNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NonNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return rotate(state,mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return PathType.TRAPDOOR;
    }
}
