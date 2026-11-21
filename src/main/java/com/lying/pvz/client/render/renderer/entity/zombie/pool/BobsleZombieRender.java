package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.BobsleZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.pool.BobsleZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BobsleZombieRender extends PvZZombieRender<BobsleZombieEntity, BobsleZombieModel> {

	public BobsleZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BobsleZombieModel(ctx.bakeLayer(BobsleZombieModel.LAYER_LOCATION)), 0.5f);
	}
}