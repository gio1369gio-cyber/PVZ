package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.PumpkinZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.PumpkinZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PumpkinZombieRender
		extends AbstractZombotanyRender<PumpkinZombieEntity, PumpkinZombieModel> {

	public PumpkinZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PumpkinZombieModel(ctx.bakeLayer(PumpkinZombieModel.LAYER_LOCATION)), 0.5f);
	}
}