package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.OldZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.OldZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class OldZombieRender extends PvZZombieRender<OldZombieEntity, OldZombieModel> {

	public OldZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new OldZombieModel(ctx.bakeLayer(OldZombieModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(OldZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAngry = entity.isAngry();
	}
}