package com.lying.pvz.client.render.renderer.entity.plant.spear;

import com.lying.pvz.client.model.entity.plant.spear.SpikeRockModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.spear.SpikeRockEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpikeRockRender extends PvZPlantRender<SpikeRockEntity, SpikeRockModel> {

	public SpikeRockRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SpikeRockModel(ctx.bakeLayer(SpikeRockModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(SpikeRockEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.spikeNum = entity.getSpikeNum();
		state.spikeNumCount = entity.getSpikesCount();
	}
}