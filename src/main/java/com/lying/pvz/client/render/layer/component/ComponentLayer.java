package com.lying.pvz.client.render.layer.component;

import com.lying.pvz.client.model.entity.ComponentModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.resources.Identifier;

public abstract class ComponentLayer<S extends EntityRenderState, M extends EntityModel<? super S>>
        extends RenderLayer<S, M> {

    private final ComponentModel<? super S> model;

    public ComponentLayer(RenderLayerParent<S, M> parent, ComponentModel<? super S> model) {
        super(parent);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (!this.canRender(state)) {
            return;
        }
        poseStack.pushPose();
        collector.order(this.getOrder())
                .submitModel(this.model, state, poseStack,
                        RenderTypes.entityCutout(this.getRenderTexture(state)),
                        lightCoords, OverlayTexture.NO_OVERLAY, -1, (UvMapping) null, state.outlineColor);
        poseStack.popPose();
    }

    protected int getOrder() {
        return 0;
    }

    public boolean canRender(S state) {
        return true;
    }

    public abstract Identifier getRenderTexture(S state);
}