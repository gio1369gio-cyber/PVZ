package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.npc.PennyModel;
import com.lying.pvz.client.render.renderer.entity.npc.PennyRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.DestroyCarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class DestroyCarRender extends EntityRenderer<DestroyCarEntity, PvZEntityRenderState> {

	private final EntityModel<? super PvZEntityRenderState> model;

	public DestroyCarRender(EntityRendererProvider.Context context) {
		super(context);
		this.model = new PennyModel(context.bakeLayer(PennyModel.LAYER_LOCATION));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(DestroyCarEntity entity,
								   PvZEntityRenderState state,
								   float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.texture = PennyRender.PENNY_TEX;
	}

	@Override
	public void submit(PvZEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		if (state.texture == null) {
			return;
		}

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0, -1.501, 0.0);

		RenderType renderType = this.model.renderType(state.texture);

		collector.submitModel(this.model, state, poseStack, renderType, state.lightCoords,
				OverlayTexture.NO_OVERLAY, -1, null, 0);

		poseStack.popPose();
	}
}