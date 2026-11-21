package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.NewspaperZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.NewspaperZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NewspaperZombieRender extends PvZZombieRender<NewspaperZombieEntity, NewspaperZombieModel> {

	public NewspaperZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new NewspaperZombieModel(ctx.bakeLayer(NewspaperZombieModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(NewspaperZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAngry = entity.isAngry();
	}
}