package com.lying.pvz.client.render.renderer.blockentity;

import com.lying.pvz.client.model.entity.blockentity.OriginModel;
import com.lying.pvz.common.level.blockentity.EssenceAltarBlockEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EssenceAltarBlockEntityRenderer
		implements BlockEntityRenderer<EssenceAltarBlockEntity,
		EssenceAltarBlockEntityRenderer.EssenceAltarRenderState> {

	private static final Identifier RES = StringUtil.id("textures/blockentity/origin.png");

	private final OriginModel origin;

	public EssenceAltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.origin = new OriginModel(context.bakeLayer(OriginModel.LAYER_LOCATION));
	}

	@Override
	public EssenceAltarRenderState createRenderState() {
		return new EssenceAltarRenderState();
	}

	@Override
	public void extractRenderState(EssenceAltarBlockEntity blockEntity, EssenceAltarRenderState state,
								   float partialTicks, Vec3 cameraPosition,
								   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {

		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);

		float time = blockEntity.tick + partialTicks;
		state.floatOffsetY = -2.3F - 0.15F * Mth.sin(time * 0.1F);
		state.modelState.ageInTicks = time;

	}

	@Override
	public void submit(EssenceAltarRenderState state,
					   PoseStack poseStack,
					   SubmitNodeCollector collector,
					   CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(-0.5F, state.floatOffsetY, 0.5F);

		RenderType renderType = RenderTypes.entityTranslucentCull(RES);

		collector.submitModel(origin,state.modelState, poseStack,
				renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(EssenceAltarBlockEntity blockEntity) {

		return new AABB(blockEntity.getBlockPos()).inflate(1.0).expandTowards(0.0, -3.0, 0.0);
	}

	public static class EssenceAltarRenderState extends BlockEntityRenderState {

		/** 模型 Y 轴浮动偏移 */
		public float floatOffsetY;

		public final EntityRenderState modelState = new EntityRenderState();
	}
}