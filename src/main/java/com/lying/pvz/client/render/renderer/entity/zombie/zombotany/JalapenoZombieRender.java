package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.JalapenoZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.JalapenoZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class JalapenoZombieRender
		extends AbstractZombotanyRender<JalapenoZombieEntity, JalapenoZombieModel> {

	public JalapenoZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new JalapenoZombieModel(ctx.bakeLayer(JalapenoZombieModel.LAYER_LOCATION)), 0.4F);
	}
}