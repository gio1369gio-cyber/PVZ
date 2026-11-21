package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.LadderZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.roof.LadderZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LadderZombieRender extends PvZZombieRender<LadderZombieEntity, LadderZombieModel> {

	public LadderZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new LadderZombieModel(ctx.bakeLayer(LadderZombieModel.LAYER_LOCATION)), 0.5F);
	}
}