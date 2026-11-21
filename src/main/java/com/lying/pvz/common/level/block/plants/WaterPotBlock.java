package com.lying.pvz.common.level.block.plants;

import com.lying.pvz.common.level.block.AbstractFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class WaterPotBlock extends AbstractFacingBlock {
    protected static final VoxelShape WATER_POT_AABB =
            Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0);

    public WaterPotBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return WATER_POT_AABB;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return PathType.BLOCKED;
    }

}