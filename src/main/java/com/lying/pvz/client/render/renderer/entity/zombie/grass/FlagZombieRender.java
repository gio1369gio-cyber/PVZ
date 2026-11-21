package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.FlagZombieModel;
import com.lying.pvz.common.level.entity.zombie.grass.FlagZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlagZombieRender
		extends AbstractNormalRender<FlagZombieEntity, FlagZombieModel> {

	public FlagZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new FlagZombieModel(ctx.bakeLayer(FlagZombieModel.LAYER_LOCATION)), 0.5f);
	}
}