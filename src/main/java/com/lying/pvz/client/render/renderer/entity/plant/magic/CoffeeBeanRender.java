package com.lying.pvz.client.render.renderer.entity.plant.magic;

import com.lying.pvz.client.model.entity.plant.magic.CoffeeBeanModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.magic.CoffeeBeanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CoffeeBeanRender extends PvZPlantRender<CoffeeBeanEntity, CoffeeBeanModel> {

	public CoffeeBeanRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CoffeeBeanModel(ctx.bakeLayer(CoffeeBeanModel.LAYER_LOCATION)), 0.3f);
	}
}