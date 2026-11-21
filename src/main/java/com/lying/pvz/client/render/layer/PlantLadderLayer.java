package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.model.entity.misc.LadderModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class PlantLadderLayer<S extends PvZPlantRenderState,
		M extends EntityModel<? super S>>
		extends RenderLayer<S, M> {

	private static final Identifier LADDER_TEX =
			StringUtil.id("textures/entity/misc/ladder.png");

	private final LadderModel<?> model;

	public PlantLadderLayer(RenderLayerParent<S, M> parent, ModelPart modelPart) {
		super(parent);
		this.model = new LadderModel<>(modelPart);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector,
					   int lightCoords, S state, float yRot, float xRot) {
		if (!state.metal || state.isInvisible) {
			return;
		}

		poseStack.pushPose();

		final float scale = 0.6f;
		final float plantScale = state.renderScale;
		if (plantScale > 0) {
			final float k = scale / plantScale;
			poseStack.scale(k, k, k);
		}

		final double offsetH = 1.501D;
		poseStack.rotateDegrees(Axis.XP, -22.5F);
		poseStack.translate(0,
				plantScale / scale * offsetH - offsetH + 0.5,
				-0.8F);

		collector.order(0).submitModelPart(
				this.model.root(),
				poseStack,
				RenderTypes.entitySolid(LADDER_TEX),
				lightCoords,
				OverlayTexture.NO_OVERLAY,
				null);

		poseStack.popPose();
	}
}