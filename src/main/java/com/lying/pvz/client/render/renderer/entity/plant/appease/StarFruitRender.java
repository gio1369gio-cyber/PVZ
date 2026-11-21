package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.StarFruitModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.StarFruitEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StarFruitRender extends PvZPlantRender<StarFruitEntity, StarFruitModel> {

	public StarFruitRender(EntityRendererProvider.Context ctx) {
		super(ctx, new StarFruitModel(ctx.bakeLayer(StarFruitModel.LAYER_LOCATION)), 0.45F);
	}

	@Override
	public void extractRenderState(StarFruitEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
		if (entity.lightTick > 0) {
			state.sunLight = true;
		}
	}
}