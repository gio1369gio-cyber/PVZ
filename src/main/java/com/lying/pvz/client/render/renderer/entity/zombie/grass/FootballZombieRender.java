package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.FootballZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.grass.FootballZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FootballZombieRender extends PvZZombieRender<FootballZombieEntity, FootballZombieModel> {

	public FootballZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new FootballZombieModel(ctx.bakeLayer(FootballZombieModel.LAYER_LOCATION)), 0.5f);
	}
}