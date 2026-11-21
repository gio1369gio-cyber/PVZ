package com.lying.pvz.client.render.renderer.entity.plant.light;

import com.lying.pvz.client.model.entity.plant.light.TwinSunFlowerModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.light.TwinSunFlowerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TwinSunFlowerRender extends PvZPlantRender<TwinSunFlowerEntity, TwinSunFlowerModel> {

	public TwinSunFlowerRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TwinSunFlowerModel(ctx.bakeLayer(TwinSunFlowerModel.LAYER_LOCATION)), 0.48f);
	}

	@Override
	public void extractRenderState(TwinSunFlowerEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.animCD = entity.getAnimGenCD();
		state.isPlantInSuperMode = entity.isPlantInSuperMode();
	}
}