package com.lying.pvz.client.render.renderer.entity;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityBlockRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PvZEntityBlockRender<T extends Entity, S extends PvZEntityBlockRenderState> extends EntityRenderer<T, S> {

	public PvZEntityBlockRender(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void extractRenderState(T entity, S state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.scale = this.getScaleByEntity(entity);
		state.xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
		state.blockState = this.getBlockByEntity(entity);
	}

	@Override
	public void submit(S state, PoseStack poseStack,
					   SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);

		float f = state.scale;
		poseStack.scale(f, f, f);
		poseStack.translate(0.0, -1.501, 0.0);
		poseStack.rotateDegrees(Axis.ZP, state.xRot);

		BlockModelSet blockModelSet = Minecraft.getInstance().getModelManager().getBlockModelSet();
		BlockModel blockModel = blockModelSet.get(state.blockState);

		BlockModelRenderState modelState = new BlockModelRenderState();
		blockModel.update(modelState, state.blockState,  BlockDisplayContext.create(), 0L);

		modelState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}

	public abstract float getScaleByEntity(T entity);

	public abstract BlockState getBlockByEntity(T entity);

}