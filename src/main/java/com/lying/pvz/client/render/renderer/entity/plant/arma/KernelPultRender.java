package com.lying.pvz.client.render.renderer.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.arma.KernelPultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.KernelPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KernelPultRender extends PvZPlantRender<KernelPultEntity, KernelPultModel> {

	public KernelPultRender(EntityRendererProvider.Context ctx) {
		super(ctx, new KernelPultModel(ctx.bakeLayer(KernelPultModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(KernelPultEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
		state.currentBullet = entity.getCurrentBullet();
	}
}