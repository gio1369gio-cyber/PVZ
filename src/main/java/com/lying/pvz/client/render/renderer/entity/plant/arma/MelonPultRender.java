package com.lying.pvz.client.render.renderer.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.arma.MelonPultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.MelonPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MelonPultRender extends PvZPlantRender<MelonPultEntity, MelonPultModel> {

	public MelonPultRender(EntityRendererProvider.Context ctx) {
		super(ctx, new MelonPultModel(ctx.bakeLayer(MelonPultModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(MelonPultEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
	}
}