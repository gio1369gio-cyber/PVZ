package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.YetiZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.pool.YetiZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class YetiZombieRender extends PvZZombieRender<YetiZombieEntity, YetiZombieModel> {

	public YetiZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new YetiZombieModel(ctx.bakeLayer(YetiZombieModel.LAYER_LOCATION)), 0.5f);
	}
}