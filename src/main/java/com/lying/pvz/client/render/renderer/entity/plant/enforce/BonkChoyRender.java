package com.lying.pvz.client.render.renderer.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.enforce.BonkChoyModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.enforce.BonkChoyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BonkChoyRender extends PvZPlantRender<BonkChoyEntity, BonkChoyModel> {

	public BonkChoyRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BonkChoyModel(ctx.bakeLayer(BonkChoyModel.LAYER_LOCATION)), 0.4F);
	}
}