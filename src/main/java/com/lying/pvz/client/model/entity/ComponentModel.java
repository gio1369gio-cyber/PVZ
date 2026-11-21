package com.lying.pvz.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

/**
 * 组件模型基类。
 *
 */
public abstract class ComponentModel<S extends EntityRenderState> extends PvZEntityModel<S> {

    protected ComponentModel(ModelPart root) {
        super(root);
    }

    protected ComponentModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }


    public ModelPart getTotalModel() { return this.root(); }
}