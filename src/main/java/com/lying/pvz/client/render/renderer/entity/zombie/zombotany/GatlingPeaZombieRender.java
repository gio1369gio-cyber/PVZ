package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.GatlingPeaZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.GatlingPeaZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GatlingPeaZombieRender
		extends AbstractZombotanyRender<GatlingPeaZombieEntity, GatlingPeaZombieModel> {

	public GatlingPeaZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GatlingPeaZombieModel(ctx.bakeLayer(GatlingPeaZombieModel.LAYER_LOCATION)), 0.4F);
	}
}