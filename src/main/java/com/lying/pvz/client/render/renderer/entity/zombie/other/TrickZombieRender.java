package com.lying.pvz.client.render.renderer.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.other.TrickZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.other.TrickZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TrickZombieRender extends PvZZombieRender<TrickZombieEntity, TrickZombieModel> {

	public TrickZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TrickZombieModel(ctx.bakeLayer(TrickZombieModel.LAYER_LOCATION)), 0.3f);
	}
}