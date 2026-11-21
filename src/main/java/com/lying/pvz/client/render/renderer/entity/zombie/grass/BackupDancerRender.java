package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.BackupDancerModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.grass.BackupDancerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BackupDancerRender extends PvZZombieRender<BackupDancerEntity, BackupDancerModel> {

	public BackupDancerRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BackupDancerModel(ctx.bakeLayer(BackupDancerModel.LAYER_LOCATION)), 0.5f);
	}
}