package com.lying.pvz.client.render.renderer.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.enforce.TangleKelpModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.enforce.TangleKelpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TangleKelpRender extends PvZPlantRender<TangleKelpEntity, TangleKelpModel> {

	public TangleKelpRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TangleKelpModel(ctx.bakeLayer(TangleKelpModel.LAYER_LOCATION)), 0.3f);
	}

}