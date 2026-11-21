package com.lying.pvz.client.render.renderer.entity.zombie.custom;

import com.lying.pvz.client.model.entity.zombie.custom.LavaZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.pool.AbstractSwimmerRender;
import com.lying.pvz.common.level.entity.zombie.custom.LavaZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LavaZombieRender
		extends AbstractSwimmerRender<LavaZombieEntity, LavaZombieModel> {

	public LavaZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new LavaZombieModel(ctx.bakeLayer(LavaZombieModel.LAYER_LOCATION)), 0.5f);
	}
}