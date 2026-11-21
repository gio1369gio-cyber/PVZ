package com.lying.pvz.client.render.renderer.entity.plant.spear;

import com.lying.pvz.client.model.entity.plant.spear.SpikeWeedModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.spear.SpikeWeedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpikeWeedRender extends PvZPlantRender<SpikeWeedEntity, SpikeWeedModel> {

	public SpikeWeedRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SpikeWeedModel(ctx.bakeLayer(SpikeWeedModel.LAYER_LOCATION)), 0.4f);
	}
}