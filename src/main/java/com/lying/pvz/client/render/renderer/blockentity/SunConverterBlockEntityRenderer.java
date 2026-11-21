package com.lying.pvz.client.render.renderer.blockentity;


import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.blockentity.SunConverterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class SunConverterBlockEntityRenderer implements BlockEntityRenderer<SunConverterBlockEntity,
		SunConverterBlockEntityRenderer.SunConverterRenderState> {

	private static final Supplier<ItemStack> STACK = () -> new ItemStack(PvZItemRegister.RESOURCE_COLLECTOR.get());

	private final ItemModelResolver itemModelResolver;

	public SunConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SunConverterRenderState createRenderState() {
		return new SunConverterRenderState();
	}

	@Override
	public void extractRenderState(SunConverterBlockEntity blockEntity, SunConverterRenderState state, float partialTicks,
								   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);

		state.animationTime = blockEntity.tickExist + partialTicks;
		state.active = blockEntity.array.get(0) == 1;
		this.itemModelResolver.updateForTopItem(state.itemState, STACK.get(), ItemDisplayContext.GROUND,
				blockEntity.getLevel(), null, 0);
	}

	@Override
	public void submit(SunConverterRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();

		float scale = 1.2F;
		poseStack.scale(scale, scale, scale);
		poseStack.translate(0.5D / scale, 1D / scale, 0.5D / scale);

		if (state.active) {
			float time = state.animationTime;
			poseStack.translate(0, (0.1D * Mth.sin(time / 10F)) / scale, 0);
			poseStack.rotate(Axis.YP, time / 10F);
		}

		state.itemState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(SunConverterBlockEntity blockEntity) {
		return new AABB(blockEntity.getBlockPos()).inflate(0.5).expandTowards(0.0, 1.0, 0.0);
	}

	public static class SunConverterRenderState extends BlockEntityRenderState {
		/** tickExist + partialTick */
		public float animationTime;
		/** array.get(0) == 1 */
		public boolean active;
		/** 物品渲染状态，每帧在 extractRenderState 中重新提取 */
		public final ItemStackRenderState itemState = new ItemStackRenderState();
	}
}