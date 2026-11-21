package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.BloverModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.assist.BloverEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * Blover 渲染器。
 *
 */
public class BloverRender extends PvZPlantRender<BloverEntity, BloverModel> {

	public BloverRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BloverModel(ctx.bakeLayer(BloverModel.LAYER_LOCATION)), 0.4F);
	}

}