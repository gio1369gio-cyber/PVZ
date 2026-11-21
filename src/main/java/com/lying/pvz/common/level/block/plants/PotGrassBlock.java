package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.level.block.AbstractFacingBlock;
import com.lying.pvz.common.list.PlantList;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class PotGrassBlock extends AbstractFacingBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(3.0, 1.0, 3.0, 13.0, 10.0, 13.0),
            Block.box(4.0, 10.0, 4.0, 12.0, 12.0, 12.0),
            Block.box(3.0, 12.0, 3.0, 13.0, 14.0, 13.0));

    public PotGrassBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            if (level.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK) {
                this.trySpawnPlant(level, pos);
            }
        }
    }

    private void trySpawnPlant(Level level, BlockPos pos) {
        RandomSource random = RandomSource.create();
        IPlantType plantType = PlantList.PLANT.getRandomItem(random).get();
        level.removeBlock(pos, false);
        Mob plant = plantType.getEntityType().get().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (plant != null) {
            EntityUtil.onEntitySpawn(level, plant, pos);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  SHAPE;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return PathType.FENCE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

}

