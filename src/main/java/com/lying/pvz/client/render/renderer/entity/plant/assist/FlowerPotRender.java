package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.FlowerPotModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.assist.FlowerPotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlowerPotRender extends PvZPlantRender<FlowerPotEntity, FlowerPotModel> {

	public FlowerPotRender(EntityRendererProvider.Context ctx) {
		super(ctx, new FlowerPotModel(ctx.bakeLayer(FlowerPotModel.LAYER_LOCATION)), 0.5F);
	}
}