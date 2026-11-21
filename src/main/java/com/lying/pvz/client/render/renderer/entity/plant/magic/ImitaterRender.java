package com.lying.pvz.client.render.renderer.entity.plant.magic;

import com.lying.pvz.client.model.entity.plant.magic.ImitaterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.magic.ImitaterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ImitaterRender extends PvZPlantRender<ImitaterEntity, ImitaterModel> {

	public ImitaterRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ImitaterModel(ctx.bakeLayer(ImitaterModel.LAYER_LOCATION)), 0);
	}
}