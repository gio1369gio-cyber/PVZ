package com.lying.pvz.client.render.renderer.blockentity;

import com.lying.pvz.client.model.entity.plant.light.SunFlowerModel;
import com.lying.pvz.common.level.block.AbstractFacingBlock;
import com.lying.pvz.common.level.block.special.SunFlowerTrophyBlock;
import com.lying.pvz.common.level.blockentity.SunFlowerTrophyBlockEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SunFlowerTrophyBlockEntityRenderer implements BlockEntityRenderer<SunFlowerTrophyBlockEntity,
		SunFlowerTrophyBlockEntityRenderer.SunFlowerTrophyRenderState> {

	private final SunFlowerModel model;

	public SunFlowerTrophyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new SunFlowerModel(context.bakeLayer(SunFlowerModel.LAYER_LOCATION));
	}

	@Override
	public SunFlowerTrophyRenderState createRenderState() {
		return new SunFlowerTrophyRenderState();
	}

	@Override
	public void extractRenderState(SunFlowerTrophyBlockEntity blockEntity, SunFlowerTrophyRenderState state, float partialTicks,
								   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);

		BlockState blockState = blockEntity.getBlockState();
		if (!(blockState.getBlock() instanceof SunFlowerTrophyBlock block)) {
			state.texture = null;
			return;
		}

		state.texture = getResourceByBlock(block);
		state.facing = blockState.getValue(AbstractFacingBlock.FACING);
	}

	@Override
	public void submit(SunFlowerTrophyRenderState state, PoseStack poseStack,
					   SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.texture == null) {
			return;
		}

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(-0.5D, -2.25D, 0.5D);

		Direction facing = state.facing;
		if (facing == Direction.SOUTH) {
			poseStack.rotateDegrees(Axis.YP, 180);
		} else if (facing == Direction.WEST) {
			poseStack.rotateDegrees(Axis.YP, -90);
		} else if (facing == Direction.EAST) {
			poseStack.rotateDegrees(Axis.YP, 90);
		}

		collector.submitModelPart(model.root(), poseStack, RenderTypes.entityTranslucent(state.texture),
				200, OverlayTexture.NO_OVERLAY, (UvMapping) null, -1);

		poseStack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(SunFlowerTrophyBlockEntity blockEntity) {
		return new AABB(blockEntity.getBlockPos()).inflate(1.0).expandTowards(0.0, -3.0, 0.0);
	}

	private Identifier getResourceByBlock(SunFlowerTrophyBlock block) {
		if (block.lvl == 1) {
			return StringUtil.id("textures/entity/plant/light/silver_sunflower.png");
		}
		if (block.lvl == 2) {
			return StringUtil.id("textures/entity/plant/light/gold_sunflower.png");
		}
		return StringUtil.id("textures/entity/plant/light/diamond_sunflower.png");
	}

	public static class SunFlowerTrophyRenderState extends BlockEntityRenderState {
		public @Nullable Identifier texture;
		public Direction facing = Direction.NORTH;
	}
}