package com.lying.pvz.common.level.block.cubes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 黄油方块。
 * <p>属性复制自原版蜂蜜块，行为上表达"软而黏"：摔落不受伤，行走被拖慢。
 * <p>两个重写都只在实体未主动抑制黏性时生效，潜行或自带抑制标记的实体会退回默认行为。
 */
public class ButterBlock extends Block {

    public ButterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0.0F, entity.damageSources().fall());
        }
    }


    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1D && !entity.isSteppingCarefully()) {
            double d1 = 0.4D + d0 * 0.2D;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0D, d1));
        }
        super.stepOn(level, pos, state, entity);
    }
}