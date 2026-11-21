package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.LilyPadModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.assist.LilyPadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LilyPadRender extends PvZPlantRender<LilyPadEntity, LilyPadModel> {

	public LilyPadRender(EntityRendererProvider.Context ctx) {
		super(ctx, new LilyPadModel(ctx.bakeLayer(LilyPadModel.LAYER_LOCATION)), 0.4F);
	}
}