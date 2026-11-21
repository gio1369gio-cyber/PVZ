package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.SquashZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.SquashZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SquashZombieRender
		extends AbstractZombotanyRender<SquashZombieEntity, SquashZombieModel> {

	public SquashZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SquashZombieModel(ctx.bakeLayer(SquashZombieModel.LAYER_LOCATION)), 0.4F);
	}
}