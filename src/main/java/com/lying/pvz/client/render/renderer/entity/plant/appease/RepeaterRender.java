package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.RepeaterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.RepeaterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RepeaterRender extends PvZPlantRender<RepeaterEntity, RepeaterModel> {

	public RepeaterRender(EntityRendererProvider.Context ctx) {
		super(ctx, new RepeaterModel(ctx.bakeLayer(RepeaterModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(RepeaterEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}
}