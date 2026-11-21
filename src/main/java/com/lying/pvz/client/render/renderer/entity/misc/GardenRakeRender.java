package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.GardenRakeModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.GardenRakeEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class GardenRakeRender extends PvZEntityRender<GardenRakeEntity, PvZEntityRenderState, GardenRakeModel> {

	private static final Identifier GARDEN_RAKE_TEX =
			StringUtil.id("textures/entity/misc/garden_rake.png");

	public GardenRakeRender(EntityRendererProvider.Context context) {
		super(context, new GardenRakeModel(context.bakeLayer(GardenRakeModel.LAYER_LOCATION)));
	}

	@Override
	public  PvZEntityRenderState createRenderState() {
		return new  PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(GardenRakeEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
		state.texture = GARDEN_RAKE_TEX;
		state.attackTime = entity.getAttackTime();
		state.animTime = entity.getAnimTime();
	}

	@Override
	public void submit( PvZEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.YP, 180.0F - state.yaw);
		super.submit(state, poseStack, collector, camera);
		poseStack.popPose();
	}

	@Override
	protected float getScaleByEntity(GardenRakeEntity entity) {
		return 0.9F;
	}

}