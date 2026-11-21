package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.BalloonZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BalloonZombieRender extends PvZZombieRender<BalloonZombieEntity, BalloonZombieModel> {

	public BalloonZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BalloonZombieModel(ctx.bakeLayer(BalloonZombieModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(BalloonZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasBalloon = entity.hasBalloon();
	}
}