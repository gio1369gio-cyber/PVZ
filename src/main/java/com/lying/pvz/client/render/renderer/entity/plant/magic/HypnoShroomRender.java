package com.lying.pvz.client.render.renderer.entity.plant.magic;

import com.lying.pvz.client.model.entity.plant.magic.HypnoShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.magic.HypnoShroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HypnoShroomRender extends PvZPlantRender<HypnoShroomEntity, HypnoShroomModel> {

	public HypnoShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new HypnoShroomModel(ctx.bakeLayer(HypnoShroomModel.LAYER_LOCATION)), 0.5f);
	}
}