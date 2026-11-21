package com.lying.pvz.common.level.block.special;


import com.lying.pvz.common.level.block.AbstractFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * 路灯方块（PvZ Lantern）。
 *
 * <p>用于植物大战僵尸关卡中的照明 / 场景装饰方块。该类本身只负责两件事：
 * <ul>
 *     <li>提供一个比标准方块更窄、更高（1.5 格）的碰撞箱，使其外观呈立柱状；</li>
 *     <li>将自身在生物寻路网格中标记为栅栏（{@link PathType#FENCE}），
 *         使僵尸、怪物等实体无法直接穿过，从而绕行。</li>
 * </ul>
 *
 * <p>朝向状态由父类 {@link AbstractFacingBlock} 提供；光照强度等属性
 * 应通过构造时传入的 {@link Properties}（如 {@code lightLevel}）配置，
 * 本类不再额外处理。
 *
 * @see AbstractFacingBlock
 */
public class PvZLanternBlock extends AbstractFacingBlock {

    private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 24.0D, 13.0D);

    public PvZLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return PathType.FENCE;
    }

}