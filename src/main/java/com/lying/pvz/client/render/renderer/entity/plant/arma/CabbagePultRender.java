package com.lying.pvz.client.render.renderer.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.arma.CabbagePultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.CabbagePultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CabbagePultRender extends PvZPlantRender<CabbagePultEntity, CabbagePultModel> {

	public CabbagePultRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CabbagePultModel(ctx.bakeLayer(CabbagePultModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(CabbagePultEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
	}
}