package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.ThreePeaterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.ThreePeaterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ThreePeaterRender extends PvZPlantRender<ThreePeaterEntity, ThreePeaterModel> {

	public ThreePeaterRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ThreePeaterModel(ctx.bakeLayer(ThreePeaterModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(ThreePeaterEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
		state.isPlantInSuperMode  =  entity.isPlantInSuperMode();
	}
}