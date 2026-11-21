package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.BobsleCarModel;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.BobsleCarEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class BobsleCarRender extends EntityRenderer<BobsleCarEntity, PvZEntityRenderState> {

	private static final Identifier TEX = StringUtil.id("textures/entity/misc/bobsle_car.png");

	private final EntityModel<? super PvZEntityRenderState> model;

	public BobsleCarRender(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BobsleCarModel(context.bakeLayer(BobsleCarModel.LAYER_LOCATION));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(BobsleCarEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
		state.texture = TEX;
	}

	@Override
	public void submit(PvZEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		if (state.texture == null) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.0, 0.75, 0.0);
		poseStack.rotateDegrees(Axis.YP, 180.0F - state.yaw);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.scale(0.5F, 0.5F, 0.5F);

		RenderType renderType = this.model.renderType(state.texture);

		collector.submitModel(this.model, state, poseStack, renderType, state.lightCoords,
				OverlayTexture.NO_OVERLAY, -1, null, 0);

		poseStack.popPose();
	}
}