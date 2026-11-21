package com.lying.pvz.client.render.renderer.entity.zombie.custom;


import com.lying.pvz.client.model.entity.zombie.custom.MCZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.custom.MCZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MCZombieRender extends PvZZombieRender<MCZombieEntity,MCZombieModel> {
	public MCZombieRender(EntityRendererProvider.Context context) {
		super(context, new MCZombieModel(context.bakeLayer(MCZombieModel.LAYER_LOCATION)), 0.5f);
	}
}