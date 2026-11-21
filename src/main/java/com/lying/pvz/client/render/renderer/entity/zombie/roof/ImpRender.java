package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.ImpModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.roof.ImpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ImpRender extends PvZZombieRender<ImpEntity, ImpModel> {

	public ImpRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ImpModel(ctx.bakeLayer(ImpModel.LAYER_LOCATION)), 0.3F);
	}
}