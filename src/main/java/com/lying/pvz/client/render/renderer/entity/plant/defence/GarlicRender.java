package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.GarlicModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.defence.GarlicEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GarlicRender extends PvZPlantRender<GarlicEntity, GarlicModel> {

	public GarlicRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GarlicModel(ctx.bakeLayer(GarlicModel.LAYER_LOCATION)), 0.5F);
	}
}