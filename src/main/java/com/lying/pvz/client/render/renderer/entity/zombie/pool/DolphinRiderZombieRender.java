package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.DolphinRiderZombieModel;
import com.lying.pvz.common.level.entity.zombie.pool.DolphinRiderZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DolphinRiderZombieRender
		extends AbstractSwimmerRender<DolphinRiderZombieEntity, DolphinRiderZombieModel> {

	public DolphinRiderZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DolphinRiderZombieModel(ctx.bakeLayer(DolphinRiderZombieModel.LAYER_LOCATION)), 0.4f);
	}
}