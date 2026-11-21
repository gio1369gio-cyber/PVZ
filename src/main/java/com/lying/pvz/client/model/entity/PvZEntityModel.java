package com.lying.pvz.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

/**
 * PVZ 模型基类。
 *
 */
public abstract class PvZEntityModel<S extends EntityRenderState> extends EntityModel<S> {

    protected PvZEntityModel(ModelPart root) {
        super(root);
    }

    protected PvZEntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }



    public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
        modelPart.xRot = x;
        modelPart.yRot = y;
        modelPart.zRot = z;
    }
}