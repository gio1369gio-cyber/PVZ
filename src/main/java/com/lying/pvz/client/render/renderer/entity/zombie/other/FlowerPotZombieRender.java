package com.lying.pvz.client.render.renderer.entity.zombie.other;


import com.lying.pvz.client.model.entity.zombie.other.FlowerPotZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.other.FlowerPotZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlowerPotZombieRender extends PvZZombieRender<FlowerPotZombieEntity, FlowerPotZombieModel> {
	public FlowerPotZombieRender(EntityRendererProvider.Context context) {
		super(context, new FlowerPotZombieModel(context.bakeLayer(FlowerPotZombieModel.LAYER_LOCATION)), 0.5f);
	}
}