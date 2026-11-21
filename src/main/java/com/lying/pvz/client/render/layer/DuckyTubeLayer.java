package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.model.entity.zombie.pool.DuckyTubeModel;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class DuckyTubeLayer<S extends LivingEntityRenderState,
		M extends EntityModel<? super S>>
		extends RenderLayer<S, M> {

	public static final Identifier TEXTURE =
			StringUtil.id("textures/entity/zombie/pool/ducky_tube.png");

	private final DuckyTubeModel model;

	public DuckyTubeLayer(RenderLayerParent<S, M> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new DuckyTubeModel(modelSet.bakeLayer(DuckyTubeModel.LAYER_LOCATION));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector,
					   int lightCoords, S state, float yRot, float xRot) {
		if (!state.isInWater || state.isInvisible) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0, -1.3f, 0);

		collector.submitModelPart(
				this.model.root(),
				poseStack,
				RenderTypes.entitySolid(TEXTURE),
				lightCoords,
				OverlayTexture.NO_OVERLAY,
				null);

		poseStack.popPose();
	}
}