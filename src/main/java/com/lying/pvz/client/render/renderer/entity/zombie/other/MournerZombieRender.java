package com.lying.pvz.client.render.renderer.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.other.MournerZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.other.MournerZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MournerZombieRender extends PvZZombieRender<MournerZombieEntity, MournerZombieModel> {

	public MournerZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new MournerZombieModel(ctx.bakeLayer(MournerZombieModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(MournerZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isRightShake = entity.isRightShake();
	}
}