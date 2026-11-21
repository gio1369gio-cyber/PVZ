package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.GigaFootballZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.grass.GigaFootballZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GigaFootballZombieRender extends PvZZombieRender<GigaFootballZombieEntity, GigaFootballZombieModel> {

	public GigaFootballZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GigaFootballZombieModel(ctx.bakeLayer(GigaFootballZombieModel.LAYER_LOCATION)), 0.5f);
	}
}