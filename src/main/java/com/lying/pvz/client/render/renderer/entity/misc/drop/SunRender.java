package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class SunRender extends EntityRenderer<SunEntity, PvZEntityRenderState> {

	private static final Identifier SUN_LOCATION = StringUtil.id("textures/entity/drop/sun.png");

	protected final RenderType renderType;

	/** 普通太阳默认构造：使用自发光半透明 RenderType。 */
	public SunRender(EntityRendererProvider.Context context) {
		this(context, RenderTypes.entityTranslucentEmissive(SUN_LOCATION));
	}

	/** 供子类传入自定义 RenderType 的构造。 */
	protected SunRender(EntityRendererProvider.Context context, RenderType renderType) {
		super(context);
		this.renderType = renderType;
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}


	@Override
	protected int getBlockLightLevel(SunEntity entity, BlockPos pos) {
		return Mth.clamp(super.getBlockLightLevel(entity, pos) + 7, 0, 15);
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(SunEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.icon = entity.getIcon();
		state.colorBase = entity.ColorBase;
		state.colorChange = entity.ColorChange;

		// alpha 只依赖整数 tick，extract 阶段算好
		int remain = entity.getMaxLiveTick() - entity.tickCount;
		if (remain >= 90) {
			state.alpha = 255;
		} else if (remain > 10) {
			state.alpha = 159 - (int) (Mth.sin((float) ((remain + 5) / 3.183F)) * 96);
		} else {
			state.alpha = 127 - (int) (Mth.sin((float) ((remain + 5) / 3.183F)) * 127);
		}
	}

	@Override
	public void submit(PvZEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		poseStack.pushPose();
		poseStack.translate(0.0, 0.1F, 0.0);
		applyBillboard(poseStack, camera);

		float size = 0.4F;
		poseStack.scale(size, size, size);

		final float tick = state.ageInTicks / 20.0F + 0.79F;
		final float x = (float) (state.icon % 2 * 32 + 16) / 64.0F;
		final float y = (float) (state.icon / 2 * 32 + 16) / 64.0F;

		final int red   = (int) (state.colorBase.x + Mth.sin(tick) * state.colorChange.x);
		final int green = (int) (state.colorBase.y + Mth.sin(tick) * state.colorChange.y);
		final int blue  = (int) (state.colorBase.z + Mth.sin(tick) * state.colorChange.z);
		final int alpha = state.alpha;
		final int light = state.lightCoords;
		final float[] normal = faceNormal();
		final float nx = normal[0];
		final float ny = normal[1];
		final float nz = normal[2];

		collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
			Matrix4f matrix = pose.pose();

			vertex(consumer, pose, matrix, -1F, -1.0F, red, green, blue,
					x + 12.01F * Mth.cos(tick) / 64,
					y + 12.01F * Mth.sin(tick) / 64,
					light, alpha, nx, ny, nz);
			vertex(consumer, pose, matrix, 1F, -1.0F, red, green, blue,
					x + 12.01F * Mth.cos(tick + 1.57F) / 64,
					y + 12.01F * Mth.sin(tick + 1.57F) / 64,
					light, alpha, nx, ny, nz);
			vertex(consumer, pose, matrix, 1F, 1.0F, red, green, blue,
					x - 12.01F * Mth.cos(tick) / 64,
					y - 12.01F * Mth.sin(tick) / 64,
					light, alpha, nx, ny, nz);
			vertex(consumer, pose, matrix, -1F, 1.0F, red, green, blue,
					x - 12.01F * Mth.cos(tick + 1.57F) / 64,
					y - 12.01F * Mth.sin(tick + 1.57F) / 64,
					light, alpha, nx, ny, nz);
		});

		poseStack.popPose();
	}
	/* ============ 子类差异钩子（基类给默认实现） ============ */

	/** 朝向相机（billboard）。普通太阳仅旋转朝向；子类可追加翻转。 */
	protected void applyBillboard(PoseStack poseStack, CameraRenderState camera) {
		poseStack.rotate(camera.orientation);
	}

	/** 面法线，默认 (0,0,1)。 */
	protected float[] faceNormal() {
		return new float[]{0.0F, 0.0F, 1.0F};
	}


	/* ============ 几何 ============ */

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Matrix4f matrix, float x, float y,
							   int r, int g, int b, float u, float v,
							   int light, int alpha, float nx, float ny, float nz) {
		consumer.addVertex(matrix, x, y + 0.5F, 0.0F)
				.setColor(r, g, b, alpha)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(light)
				.setNormal(pose, nx, ny, nz);
	}
}