package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.PeaShooterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.PeaShooterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PeaShooterRender extends PvZPlantRender<PeaShooterEntity, PeaShooterModel> {

	public PeaShooterRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PeaShooterModel(ctx.bakeLayer(PeaShooterModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(PeaShooterEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}
}