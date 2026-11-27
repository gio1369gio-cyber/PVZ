package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.ScreenDoorZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.grass.ScreenDoorZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ScreenDoorZombieRender extends PvZZombieRender<ScreenDoorZombieEntity, ScreenDoorZombieModel> {

	public ScreenDoorZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ScreenDoorZombieModel(ctx.bakeLayer(ScreenDoorZombieModel.LAYER_LOCATION)), 0.5f);
	}

}