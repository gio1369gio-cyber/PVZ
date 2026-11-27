package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.BobsleTeamModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.pool.BobsleTeamEntityPVZ;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BobsleTeamRender extends PvZZombieRender<BobsleTeamEntityPVZ, BobsleTeamModel> {

	public BobsleTeamRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BobsleTeamModel(ctx.bakeLayer(BobsleTeamModel.LAYER_LOCATION)), 0.6f);
	}
}