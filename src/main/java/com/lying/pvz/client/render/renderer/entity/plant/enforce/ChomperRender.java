package com.lying.pvz.client.render.renderer.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.enforce.ChomperModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.enforce.ChomperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChomperRender extends PvZPlantRender<ChomperEntity, ChomperModel> {

	public ChomperRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ChomperModel(ctx.bakeLayer(ChomperModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(ChomperEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.random = entity.getRandom();
		state.restTick = entity.getRestTick();
	}
}