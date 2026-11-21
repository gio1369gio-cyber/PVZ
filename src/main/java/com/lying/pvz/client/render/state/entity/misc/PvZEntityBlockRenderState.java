package com.lying.pvz.client.render.state.entity.misc;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PvZEntityBlockRenderState  extends EntityRenderState {
    public float scale = 1.0F;
    public float xRot;
    public BlockState blockState = Blocks.AIR.defaultBlockState();
}

