package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class MetalItemLayer<S extends PvZPlantRenderState,
		M extends EntityModel<? super S>>
		extends RenderLayer<S, M> {

	public MetalItemLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector,
					   int lightCoords, S state, float yRot, float xRot) {
		if (state.metalItem.isEmpty() || state.isInvisible || state.metalScale <= 0) {
			return;
		}
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0F, 0.1F, 0.0F);
		final float s = state.metalScale * 0.4F;
		poseStack.scale(s, s, s);
		state.metalItem.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
	}
}