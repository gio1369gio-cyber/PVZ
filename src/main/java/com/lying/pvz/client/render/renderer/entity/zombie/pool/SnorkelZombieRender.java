package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.SnorkelZombieModel;
import com.lying.pvz.common.level.entity.zombie.pool.SnorkelZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SnorkelZombieRender
		extends AbstractSwimmerRender<SnorkelZombieEntity, SnorkelZombieModel> {

	public SnorkelZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SnorkelZombieModel(ctx.bakeLayer(SnorkelZombieModel.LAYER_LOCATION)), 0.5f);
	}
}