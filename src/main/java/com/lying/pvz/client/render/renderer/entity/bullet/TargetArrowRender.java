package com.lying.pvz.client.render.renderer.entity.bullet;

import com.lying.pvz.client.model.entity.bullet.TargetArrowModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class TargetArrowRender extends PvZEntityRender<TargetArrowEntity, PvZEntityRenderState, TargetArrowModel> {

	private static final Identifier TARGET_ARROW =
			StringUtil.id("textures/entity/misc/target_arrow.png");

	public TargetArrowRender(EntityRendererProvider.Context context) {
		super(context, new TargetArrowModel(context.bakeLayer(TargetArrowModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(TargetArrowEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
		state.pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
		state.texture = TARGET_ARROW;
	}

	@Override
	public void submit(PvZEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.YP, state.yaw + 180.0F);
		poseStack.rotateDegrees(Axis.ZP, state.pitch);
		super.submit(state, poseStack, collector, camera);
		poseStack.popPose();
	}

	@Override
	protected float getScaleByEntity(TargetArrowEntity entity) {
		return 1.5F;
	}
}