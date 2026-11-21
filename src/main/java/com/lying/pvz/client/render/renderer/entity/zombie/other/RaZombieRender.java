package com.lying.pvz.client.render.renderer.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.other.RaZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.other.RaZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RaZombieRender extends PvZZombieRender<RaZombieEntity, RaZombieModel> {

	public RaZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new RaZombieModel(ctx.bakeLayer(RaZombieModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(RaZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.sunLight = entity.checkCanWorkNow();
	}
}