package com.lying.pvz.client.render.renderer.entity.plant.explosion;

import com.lying.pvz.client.model.entity.plant.explosion.CobCannonModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.explosion.CobCannonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CobCannonRender extends PvZPlantRender<CobCannonEntity, CobCannonModel> {

	public CobCannonRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CobCannonModel(ctx.bakeLayer(CobCannonModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(CobCannonEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isPlayerRiding = entity.isPlayerRiding();
		state.animCD = entity.getAnimCD();
	}
}