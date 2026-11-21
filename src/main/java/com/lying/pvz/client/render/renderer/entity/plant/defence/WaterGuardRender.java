package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.WaterGuardModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.defence.WaterGuardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WaterGuardRender extends PvZPlantRender<WaterGuardEntity, WaterGuardModel> {

	public WaterGuardRender(EntityRendererProvider.Context ctx) {
		super(ctx, new WaterGuardModel(ctx.bakeLayer(WaterGuardModel.LAYER_LOCATION)), 0f);
	}
}