package com.lying.pvz.client.render.renderer.entity.plant.magic;

import com.lying.pvz.client.model.entity.plant.magic.StrangeCatModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.magic.StrangeCatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StrangeCatRender extends PvZPlantRender<StrangeCatEntity, StrangeCatModel> {

	public StrangeCatRender(EntityRendererProvider.Context ctx) {
		super(ctx, new StrangeCatModel(ctx.bakeLayer(StrangeCatModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	public void extractRenderState(StrangeCatEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isResting = entity.isResting();
	}
}