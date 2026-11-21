package com.lying.pvz.common.level.block.ore;

import com.lying.pvz.common.init.PvZBlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PVZOreBlock extends DropExperienceBlock {


    public PVZOreBlock(Properties properties) {
        super(ConstantInt.of(0), properties);
    }


    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos,
                          @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
        if (this instanceof EssenceOreBlock) {
            if (this == PvZBlockRegister.ORIGIN_ORE.get()) {
                return Mth.nextInt(level.getRandom(), 3, 7);
            } else {
                return Mth.nextInt(level.getRandom(), 1, 4);
            }
        }
        return super.getExpDrop(state, level, pos, blockEntity, breaker, tool);
    }
}