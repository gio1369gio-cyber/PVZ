package com.lying.pvz.client.render.renderer.entity.effects;


import com.lying.pvz.common.level.entity.effect.OriginEffectEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class OriginEffectRender extends EntityRenderer<OriginEffectEntity, OriginEffectRender.OriginEffectRenderState> {

	public OriginEffectRender(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public OriginEffectRenderState createRenderState() {
		return new OriginEffectRenderState();
	}

	@Override
	public void extractRenderState(OriginEffectEntity entity, OriginEffectRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.color = entity.getColor();
		state.existTick = entity.getExistTick();
	}

	@Override
	public void submit(OriginEffectRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		final int color = state.color;
		final int T = 100;
		final float len = 1.2F;
		final float scale = 1.0F;
		final float maxScale = 0.1F * AnimationUtil.getUpDownUpDown(state.existTick % T, T, 90);

		poseStack.pushPose();
		poseStack.scale(scale + maxScale, scale + maxScale, scale + maxScale);
		poseStack.rotateDegrees(Axis.YN, 2 * state.existTick);

		// 方向 0：无额外旋转
		submitLight(collector, poseStack, len, color);

		// 方向 1：XN 90
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.XN, 90);
		submitLight(collector, poseStack, len, color);
		poseStack.popPose();

		// 方向 2：XN -90
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.XN, -90);
		submitLight(collector, poseStack, len, color);
		poseStack.popPose();

		// 方向 3：XN 180
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.XN, 180);
		submitLight(collector, poseStack, len, color);
		poseStack.popPose();

		// 方向 4：ZN 90
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.ZN, 90);
		submitLight(collector, poseStack, len, color);
		poseStack.popPose();

		// 方向 5：ZN -90
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.ZN, -90);
		submitLight(collector, poseStack, len, color);
		poseStack.popPose();

		poseStack.popPose();
	}

	private static void submitLight(SubmitNodeCollector collector, PoseStack poseStack, float len, int color) {
		collector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, consumer) -> {
			Matrix4f matrix = pose.pose();
			final int c1 = color / 256 / 256;
			final int c2 = color / 256 % 256;
			final int c3 = color % 256;

			for (int i = 0; i < 4; i++) {
				consumer.addVertex(matrix, 0.0F, 0.0F, 0.0F).setColor(c1, c2, c3, 255);
				consumer.addVertex(matrix, -0.866F, -len, -0.5F).setColor(c1, c2, c3, 20);
				consumer.addVertex(matrix, 0.866F, -len, -0.5F).setColor(c1, c2, c3, 20);
				consumer.addVertex(matrix, 0.0F, -len, 1.0F).setColor(c1, c2, c3, 20);
				consumer.addVertex(matrix, -0.866F, -len, -0.5F).setColor(c1, c2, c3, 20);
			}
		});
	}

	@Override
	protected AABB getBoundingBoxForCulling(OriginEffectEntity entity, float partialTicks) {
		return super.getBoundingBoxForCulling(entity, partialTicks).inflate(2.0);
	}

	public static class OriginEffectRenderState extends EntityRenderState {
		/** 实体颜色，0xRRGGBB */
		public int color;
		/** 实体存在 tick，用于动画 */
		public int existTick;
	}
}