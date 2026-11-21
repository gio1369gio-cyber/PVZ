package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.NormalZombieModel;
import com.lying.pvz.common.level.entity.zombie.grass.NormalZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NormalZombieRender
		extends AbstractNormalRender<NormalZombieEntity, NormalZombieModel> {

	public NormalZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new NormalZombieModel(ctx.bakeLayer(NormalZombieModel.LAYER_LOCATION)), 0.5f);
	}
}