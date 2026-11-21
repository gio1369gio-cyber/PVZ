package com.lying.pvz.client.render.renderer.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.enforce.SquashModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.enforce.SquashEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SquashRender extends PvZPlantRender<SquashEntity, SquashModel> {

	public SquashRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SquashModel(ctx.bakeLayer(SquashModel.LAYER_LOCATION)), 0.5f);
	}
}