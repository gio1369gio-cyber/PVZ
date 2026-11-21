package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.SundayEditionZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.SundayEditionZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SundayEditionZombieRender extends PvZZombieRender<SundayEditionZombieEntity, SundayEditionZombieModel> {

	public SundayEditionZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SundayEditionZombieModel(ctx.bakeLayer(SundayEditionZombieModel.LAYER_LOCATION)), 0.51F);
	}

	@Override
	public void extractRenderState(SundayEditionZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAngry = entity.isAngry();
	}
}