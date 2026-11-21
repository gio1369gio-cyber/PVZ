package com.lying.pvz.client.render.state.entity.bowling;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class BowlingRenderState extends PvZPlantRenderState {
    public float yRot;
    public float renderSize;
    public float renderOffsetY;
    public float renderPivotY;
    public @Nullable Identifier texture;
}
