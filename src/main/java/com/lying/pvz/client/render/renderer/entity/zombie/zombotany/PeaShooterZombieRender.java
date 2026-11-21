package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.PeaShooterZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.PeaShooterZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PeaShooterZombieRender
		extends AbstractZombotanyRender<PeaShooterZombieEntity, PeaShooterZombieModel> {

	public PeaShooterZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PeaShooterZombieModel(ctx.bakeLayer(PeaShooterZombieModel.LAYER_LOCATION)), 0.4F);
	}
}