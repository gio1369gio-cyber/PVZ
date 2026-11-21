package com.lying.pvz.common.level.block.others;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;

public class ScreenDoorBlock extends DoorBlock {

    public ScreenDoorBlock(BlockSetType iron, Properties arg) {
        super(iron,arg);
    }

    private int getCloseSound() {
        return this.type() == BlockSetType.IRON || this.type() == BlockSetType.COPPER ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.type() == BlockSetType.IRON || this.type() == BlockSetType.COPPER ? 1005 : 1006;
    }



    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 10);
        level.levelEvent(player,state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        return InteractionResult.SUCCESS;
    }

}
