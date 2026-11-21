package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.GatlingPeaModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.GatlingPeaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GatlingPeaRender extends PvZPlantRender<GatlingPeaEntity, GatlingPeaModel> {

	public GatlingPeaRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GatlingPeaModel(ctx.bakeLayer(GatlingPeaModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(GatlingPeaEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}
}