package com.lying.pvz.client.render.renderer.entity.plant.light;

import com.lying.pvz.client.model.entity.plant.light.SunFlowerModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.light.SunFlowerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SunFlowerRender extends PvZPlantRender<SunFlowerEntity, SunFlowerModel> {

	public SunFlowerRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SunFlowerModel(ctx.bakeLayer(SunFlowerModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(SunFlowerEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.animCD = entity.getGenCD();
		state.isPlantInSuperMode = entity.isPlantInSuperMode();
	}
}