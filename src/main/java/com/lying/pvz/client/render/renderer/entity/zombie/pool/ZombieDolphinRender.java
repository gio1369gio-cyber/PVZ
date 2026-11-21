package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.ZombieDolphinModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.ZombieDolphinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ZombieDolphinRender extends PvZZombieRender<ZombieDolphinEntity, ZombieDolphinModel> {

	public ZombieDolphinRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ZombieDolphinModel(ctx.bakeLayer(ZombieDolphinModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(ZombieDolphinEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.deltaMovement = entity.getDeltaMovement();
	}
}