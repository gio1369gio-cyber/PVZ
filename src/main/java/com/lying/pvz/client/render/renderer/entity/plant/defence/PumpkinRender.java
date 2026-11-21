package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.PumpkinModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.defence.PumpkinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PumpkinRender extends PvZPlantRender<PumpkinEntity, PumpkinModel> {

	public PumpkinRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PumpkinModel(ctx.bakeLayer(PumpkinModel.LAYER_LOCATION)), 1f);
	}
}