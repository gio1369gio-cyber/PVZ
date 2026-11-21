package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.GoldMagnetModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.assist.GoldMagnetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GoldMagnetRender extends PvZPlantRender<GoldMagnetEntity, GoldMagnetModel> {

	public GoldMagnetRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GoldMagnetModel(ctx.bakeLayer(GoldMagnetModel.LAYER_LOCATION)), 0.3F);
	}
}