package com.lying.pvz.client.render.renderer.entity.plant.fire;

import com.lying.pvz.client.model.entity.plant.fire.TorchWoodModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.fire.TorchWoodEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TorchWoodRender extends PvZPlantRender<TorchWoodEntity, TorchWoodModel> {

	public TorchWoodRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TorchWoodModel(ctx.bakeLayer(TorchWoodModel.LAYER_LOCATION)), 0.4f);
	}
}