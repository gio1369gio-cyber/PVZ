package com.lying.pvz.client.render.layer.fullskin;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;


public abstract class PVZFullSkinLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    protected final RenderLayerParent<S, M> entityRender;
    protected final M entityModel;
    protected float scale = 1F;

    public PVZFullSkinLayer(RenderLayerParent<S, M> parent) {
        super(parent);
        this.entityRender = parent;
        this.entityModel = parent.getModel();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (!this.canRender(state)) {
            return;
        }

        poseStack.pushPose();
        poseStack.scale(this.scale, this.scale, this.scale);

        final float f = state.ageInTicks;
        final RenderType type = RenderTypes.energySwirl(
                this.getResourceLocation(state), this.getU(f), this.getV(f));
        final int color = ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.5F);

        collector.order(0).submitModel(
                this.entityModel, state, poseStack, type,
                lightCoords, OverlayTexture.NO_OVERLAY, color,
                null, state.outlineColor);

        poseStack.popPose();
    }

    protected float getU(float f) {
        return 0F;
    }

    protected float getV(float f) {
        return 0F;
    }

    protected abstract boolean canRender(S state);

    protected abstract Identifier getResourceLocation(S state);
}