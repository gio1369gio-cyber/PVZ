package com.lying.pvz.client.render.renderer.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.other.NobleZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.other.NobleZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NobleZombieRender extends PvZZombieRender<NobleZombieEntity, NobleZombieModel> {

	public NobleZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new NobleZombieModel(ctx.bakeLayer(NobleZombieModel.LAYER_LOCATION)), 0.5f);
	}
}