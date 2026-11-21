package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.DolphinRiderModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.pool.DolphinRiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DolphinRiderRender extends PvZZombieRender<DolphinRiderEntity, DolphinRiderModel> {

	public DolphinRiderRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DolphinRiderModel(ctx.bakeLayer(DolphinRiderModel.LAYER_LOCATION)), 0);
	}
}