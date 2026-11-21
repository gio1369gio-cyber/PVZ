package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.AngelStarFruitModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.AngelStarFruitEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelStarFruitRender extends PvZPlantRender<AngelStarFruitEntity, AngelStarFruitModel> {

	public AngelStarFruitRender(EntityRendererProvider.Context ctx) {
		super(ctx, new AngelStarFruitModel(ctx.bakeLayer(AngelStarFruitModel.LAYER_LOCATION)), 0.44F);
	}

	@Override
	public void extractRenderState(AngelStarFruitEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
		state.lightTick = entity.lightTick;
	}
}