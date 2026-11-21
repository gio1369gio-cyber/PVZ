package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityBlockRenderState;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity.ElementTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelSet;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ElementBallRender extends EntityRenderer<ElementBallEntity, PvZEntityBlockRenderState> {

	public ElementBallRender(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public PvZEntityBlockRenderState createRenderState() {
		return new PvZEntityBlockRenderState();
	}

	@Override
	public void extractRenderState(ElementBallEntity entity, PvZEntityBlockRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.scale = this.getScaleByEntity(entity);
		state.blockState = this.getBlockByEntity(entity);
	}

	@Override
	public void submit(PvZEntityBlockRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		poseStack.pushPose();

		float f = state.scale;
		poseStack.scale(f, f, f);
		poseStack.translate(-0.5F, 0.0F, -0.5F);

		BlockModelSet blockModelSet = Minecraft.getInstance().getModelManager().getBlockModelSet();
		BlockModel blockModel = blockModelSet.get(state.blockState);

		BlockModelRenderState modelState = new BlockModelRenderState();
		blockModel.update(modelState, state.blockState, BlockDisplayContext.create(), 0L);

		modelState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}

	public float getScaleByEntity(ElementBallEntity entity) {
		return 3.0F;
	}

	public BlockState getBlockByEntity(ElementBallEntity entity) {
		if (entity.getElementBallType() == ElementTypes.FLAME) {
			return Blocks.MAGMA_BLOCK.defaultBlockState();
		}
		return Blocks.BLUE_ICE.defaultBlockState();
	}
}