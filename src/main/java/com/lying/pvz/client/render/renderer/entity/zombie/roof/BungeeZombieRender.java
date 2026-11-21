package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.BungeeZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.BungeeZombieEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix4f;

/**
 * BungeeZombie 渲染器。
 *
 * <p><b>render → submit + submitCustomGeometry</b>：1.21.1 的 {@code EntityRenderer}
 * <b>没有</b> {@code render(T, float, float, PoseStack, MultiBufferSource, int)} 方法
 * （反编译 {@code EntityRenderer} 确认），主路径是
 * {@code submit(S, PoseStack, SubmitNodeCollector, CameraRenderState)}。
 * 自定义顶点几何通过 {@code OrderedSubmitNodeCollector.submitCustomGeometry(PoseStack, RenderType,
 * CustomGeometryRenderer)} 提交，{@code CustomGeometryRenderer.render(PoseStack.Pose, VertexConsumer)}。
 *
 * <p><b>entity → state</b>：{@code submit} 只有 state 没有 entity，所有 entity 数据
 * （originPos / eyePos / 四种光照）在 {@link #extractRenderState} 打包进 state。
 * {@code state.x/y/z} 已是插值后坐标（替代 {@code xo/yo/zo} + {@code getX/Y/Z}）。
 * {@code state.partialTick} 替代 {@code partialTicks}。
 * {@code state.boundingBoxWidth/Height} 替代 {@code getBbWidth/Height}。
 *
 * <p><b>LightTexture.pack → LightCoordsUtil.pack</b>：1.21.1 已迁移（见
 * {@code EntityRenderer.getPackedLightCoords}）。
 */
public class BungeeZombieRender extends PvZZombieRender<BungeeZombieEntity, BungeeZombieModel> {

	public BungeeZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BungeeZombieModel(ctx.bakeLayer(BungeeZombieModel.LAYER_LOCATION)), 0.0F);
	}

	@Override
	public void extractRenderState(BungeeZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.ropeOriginPos = entity.getOriginPos();
		state.ropeEyePos    = entity.getEyePosition(partialTick);
		final BlockPos eyeBlock = BlockPos.containing(state.ropeEyePos);
		state.ropeStartBlockLight = this.getBlockLightLevel(entity, eyeBlock);
        if (state.ropeOriginPos != null) {
            state.ropeEndBlockLight   = entity.level().getBrightness(LightLayer.BLOCK, state.ropeOriginPos);
            state.ropeEndSkyLight     = entity.level().getBrightness(LightLayer.SKY, state.ropeOriginPos);
        }
		state.ropeStartSkyLight   = this.getSkyLightLevel(entity, eyeBlock);
        state.bodyRot = Mth.lerp(partialTick, entity.yBodyRot, entity.yBodyRotO);
		state.stayTime = entity.getStayTime();
		state.passengers = entity.getPassengers();
	}

	@Override
	public void submit(PvZZombieRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);   // 先提交 model

		poseStack.pushPose();

		// 原代码 d0/d1 中 lerp(partial*0.5, 0, 0) 恒为 0，d2/d3/d4/d5 因此固定，原样保留
		double d0 = (double)(Mth.lerp(state.partialTick * 0.5F, 0.0F, 0.0F) * ((float)Math.PI / 180F));
		double d1 = (double)(Mth.lerp(state.partialTick * 0.5F, 0.0F, 0.0F) * ((float)Math.PI / 180F));
		double d2 = Math.cos(d0);
		double d3 = Math.sin(d0);
		double d4 = Math.sin(d1);
		double d5 = Math.cos(d1);
		double d6 = (double)state.ropeOriginPos.getX() - d2 * 0.7 - d3 * (double)0.5F * d5;
		double d7 = (double)state.ropeOriginPos.getY() - d4 * (double)0.5F - (double)0.25F;
		double d8 = (double)state.ropeOriginPos.getZ() - d3 * 0.7 + d2 * (double)0.5F * d5;
		double d9 = (double)(state.bodyRot * ((float)Math.PI / 180F)) + (Math.PI / 2D);
		d2 = Math.cos(d9) * (double)state.boundingBoxWidth * 0.4;
		d3 = Math.sin(d9) * (double)state.boundingBoxWidth * 0.4;
		double d10 = state.x + d2;
		double d11 = state.y;
		double d12 = state.z + d3;

		poseStack.translate(d2, -(1.6 - (double)state.boundingBoxHeight) * (double)0.5F, d3);

		final float f  = (float)(d6 - d10);
		final float f1 = (float)(d7 - d11);
		final float f2 = (float)(d8 - d12);

		final float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
		final float f5 = f2 * f4;
		final float f6 = f * f4;

		final int i = state.ropeStartBlockLight;
		final int j = state.ropeEndBlockLight;
		final int k = state.ropeStartSkyLight;
		final int l = state.ropeEndSkyLight;

		collector.submitCustomGeometry(poseStack, RenderTypes.leash(), (pose, buffer) -> {
			Matrix4f m = pose.pose();
			renderSide(buffer, m, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6);
			renderSide(buffer, m, f, f1, f2, i, j, k, l, 0.025F, 0.0F,  f5, f6);
		});

		poseStack.popPose();
	}
	public static void renderSide(
			VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float g, float h,
			int i, int j, int k, int l, float m, float n, float o, float p) {
		int q = 24;
		for (int r = 0; r < 24; r++) {
			float s = r / 23.0F;
			int t = (int)Mth.lerp(s, i, j);
			int u = (int)Mth.lerp(s, k, l);
			int v = LightCoordsUtil.pack(t, u);
			addVertexPair(vertexConsumer, matrix4f, v, f, g, h, m, n, 24, r,     false, o, p);
			addVertexPair(vertexConsumer, matrix4f, v, f, g, h, m, n, 24, r + 1, true,  o, p);
		}
	}

	public static void addVertexPair(
			VertexConsumer vertexConsumer, Matrix4f matrix4f, int i, float f, float g, float h,
			float j, float k, int l, int m, boolean bl, float n, float o) {
		float p = 0.5F;
		float q = 0.4F;
		float r = 0.3F;
		if (m % 2 == 0) {
			p *= 0.7F;
			q *= 0.7F;
			r *= 0.7F;
		}

		float s = (float)m / l;
		float t = f * s;
		float u = g > 0.0F ? g * s * s : g - g * (1.0F - s) * (1.0F - s);
		float v = h * s;
		if (!bl) {
			vertexConsumer.addVertex(matrix4f, t + n, u + j - k, v - o).setColor(p, q, r, 1.0F).setLight(i);
		}
		vertexConsumer.addVertex(matrix4f, t - n, u + k, v + o).setColor(p, q, r, 1.0F).setLight(i);
		if (bl) {
			vertexConsumer.addVertex(matrix4f, t + n, u + j - k, v - o).setColor(p, q, r, 1.0F).setLight(i);
		}
	}
}