package com.lying.pvz.client.render.renderer.entity.effects;


import com.lying.pvz.client.model.entity.plant.explosion.DoomShroomModel;
import com.lying.pvz.common.level.entity.misc.DoomFixerEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;

public class DoomFixerRender extends EntityRenderer<DoomFixerEntity, DoomFixerRender.DoomFixerRenderState> {

	private static final Identifier TEX = StringUtil.id("textures/entity/layer/doom.png");

	private final DoomShroomModel model;

	public DoomFixerRender(EntityRendererProvider.Context context) {
		super(context);
		this.model = new DoomShroomModel(context.bakeLayer(DoomShroomModel.LAYER_LOCATION));
	}

	@Override
	public DoomFixerRenderState createRenderState() {
		return new DoomFixerRenderState();
	}

	@Override
	public void extractRenderState(DoomFixerEntity entity, DoomFixerRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.scale = Math.min(30.0F, entity.getExistTick() * 1.5F);
		state.animationTime = (float) entity.tickCount + partialTicks;
	}

	@Override
	public void submit(DoomFixerRenderState state, PoseStack poseStack,
					   SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.scale(state.scale, state.scale, state.scale);
		poseStack.translate(0.0, -1.501, 0.0);

		float f = state.animationTime;
		collector.submitModelPart(model.root(), poseStack, RenderTypes.energySwirl(TEX, f, f), state.lightCoords,
				OverlayTexture.NO_OVERLAY, (UvMapping) null, 0xFF7F7F7F);

		poseStack.popPose();
	}

	@Override
	protected AABB getBoundingBoxForCulling(DoomFixerEntity entity, float partialTicks) {
		return super.getBoundingBoxForCulling(entity, partialTicks).inflate(30.0);
	}

	public static class DoomFixerRenderState extends EntityRenderState {
		/** min(30, existTick * 1.5F) */
		public float scale;
		/** tickCount + partialTick，用于 energySwirl 偏移 */
		public float animationTime;
	}
}