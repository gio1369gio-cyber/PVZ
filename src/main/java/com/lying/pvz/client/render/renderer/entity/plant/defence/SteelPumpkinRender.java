package com.lying.pvz.client.render.renderer.entity.plant.defence;


import com.lying.pvz.client.model.entity.plant.defence.SteelPumpkinModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.defence.SteelPumpkinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SteelPumpkinRender extends PvZPlantRender<SteelPumpkinEntity, SteelPumpkinModel> {
	public SteelPumpkinRender(EntityRendererProvider.Context context) {
		super(context, new SteelPumpkinModel(context.bakeLayer(SteelPumpkinModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(SteelPumpkinEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isSolid = entity.isSolid();
	}
}