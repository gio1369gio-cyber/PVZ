package com.lying.pvz.client.render.renderer.entity.plant.explosion;

import com.lying.pvz.client.model.entity.plant.explosion.BambooLordModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.explosion.BambooLordEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BambooLordRender extends PvZPlantRender<BambooLordEntity, BambooLordModel> {

	public BambooLordRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BambooLordModel(ctx.bakeLayer(BambooLordModel.LAYER_LOCATION)), 0.4F);
	}
}