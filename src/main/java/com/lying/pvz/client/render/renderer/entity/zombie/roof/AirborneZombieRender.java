package com.lying.pvz.client.render.renderer.entity.zombie.roof;


import com.lying.pvz.client.model.entity.zombie.roof.AirborneZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.AirborneZombieEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix4f;


public class AirborneZombieRender extends PvZZombieRender<AirborneZombieEntity,AirborneZombieModel> {

	public AirborneZombieRender(EntityRendererProvider.Context context) {
		super(context, new AirborneZombieModel(context.bakeLayer(AirborneZombieModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(AirborneZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasLine = entity.hasLine();

		if (!state.hasLine) return;

		// 绳子起点 = entity 自身位置（原代码用 entity.getX/Y/Z）
		state.ropeOriginPos = entity.blockPosition();
		state.ropeEyePos = entity.getEyePosition(partialTick);

		// 光照
		BlockPos eyeBlock = BlockPos.containing(state.ropeEyePos);
		state.ropeStartBlockLight = this.getBlockLightLevel(entity, eyeBlock);
		state.ropeStartSkyLight = this.getSkyLightLevel(entity, eyeBlock);
		state.ropeEndBlockLight = entity.level().getBrightness(LightLayer.BLOCK, state.ropeOriginPos);
		state.ropeEndSkyLight = entity.level().getBrightness(LightLayer.SKY, state.ropeOriginPos);

		// 身体朝向
		state.bodyRot = Mth.lerp(partialTick, entity.yBodyRot, entity.yBodyRotO);

	}
	@Override
	public void submit(PvZZombieRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		if (!state.hasLine || state.ropeOriginPos == null) return;

		poseStack.pushPose();

		// 原代码 d0/d1 恒为 0，d2/d3/d4/d5 固定
		double d0 = (double) (Mth.lerp(state.partialTick * 0.5F, 0.0F, 0.0F) * ((float) Math.PI / 180F));
		double d1 = (double) (Mth.lerp(state.partialTick * 0.5F, 0.0F, 0.0F) * ((float) Math.PI / 180F));
		double d2 = Math.cos(d0);
		double d3 = Math.sin(d0);
		double d4 = Math.sin(d1);
		double d5 = Math.cos(d1);

		// 终点（原 d6, d7, d8）：基于 entity.getX/Y/Z，不是 getOriginPos
		double endX = state.x - d2 * 0.7 - d3 * 0.5 * d5;
		double endY = state.y + 50 - d4 * 0.5 - 0.25;
		double endZ = state.z - d3 * 0.7 + d2 * 0.5 * d5;

		// 身体偏移（原 d9, d2, d3 重算）
		double d9 = (double) (state.bodyRot * ((float) Math.PI / 180F)) + (Math.PI / 2D);
		double offsetX = Math.cos(d9) * (double) state.boundingBoxWidth * 0.4;
		double offsetZ = Math.sin(d9) * (double) state.boundingBoxWidth * 0.4;

		// 起点（原 d10, d11, d12）
		double startX = state.x + offsetX;
		double startY = state.y;
		double startZ = state.z + offsetZ;

		poseStack.translate(offsetX, -(1.0 - (double) state.boundingBoxHeight), offsetZ);

		final float f = (float) (endX - startX);
		final float f1 = (float) (endY - startY);
		final float f2 = (float) (endZ - startZ);

		final float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
		final float f5 = f2 * f4;
		final float f6 = f * f4;

		final int i = state.ropeStartBlockLight;
		final int j = state.ropeEndBlockLight;
		final int k = state.ropeStartSkyLight;
		final int l = state.ropeEndSkyLight;

		collector.submitCustomGeometry(poseStack, RenderTypes.leash(), (pose, buffer) -> {
			Matrix4f m = pose.pose();
			BungeeZombieRender.renderSide(buffer, m, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6);
			BungeeZombieRender.renderSide(buffer, m, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6);
		});

		poseStack.popPose();
	}
}
