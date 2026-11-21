package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.PoleZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.PoleZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PoleZombieRender extends PvZZombieRender<PoleZombieEntity, PoleZombieModel> {

	public PoleZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PoleZombieModel(ctx.bakeLayer(PoleZombieModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(PoleZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.poleJumpCD = entity.getPoleJumpCD();
		state.hasPole   =entity .hasPole();
	}
}