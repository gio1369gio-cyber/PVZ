package com.lying.pvz.client.render.renderer.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.arma.KernelPultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.ButterPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * ButterPult 复用 KernelPultModel —— 1.16.5 原逻辑，迁移原样保留。
 */
public class ButterPultRender extends PvZPlantRender<ButterPultEntity, KernelPultModel> {

	public ButterPultRender(EntityRendererProvider.Context ctx) {
		super(ctx, new KernelPultModel(ctx.bakeLayer(KernelPultModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(ButterPultEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
		state.currentBullet = entity.getCurrentBullet();
	}
}