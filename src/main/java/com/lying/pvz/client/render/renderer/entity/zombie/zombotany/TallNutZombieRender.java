package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.TallNutZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.TallNutZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TallNutZombieRender
		extends AbstractZombotanyRender<TallNutZombieEntity, TallNutZombieModel> {

	public TallNutZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TallNutZombieModel(ctx.bakeLayer(TallNutZombieModel.LAYER_LOCATION)), 0.4F);
	}
}