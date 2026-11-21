package com.lying.pvz.client.render.renderer.entity.zombie.roof;


import com.lying.pvz.client.model.entity.zombie.roof.Edgar090547Model;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.Edgar090547Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class Edgar090547Render extends PvZZombieRender<Edgar090547Entity,Edgar090547Model> {
	public Edgar090547Render(EntityRendererProvider.Context context) {
		super(context, new Edgar090547Model(context.bakeLayer(Edgar090547Model.LAYER_LOCATION)), 1.0f);
	}

	@Override
	public void extractRenderState(Edgar090547Entity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAngry = entity.angry();
		state.existTick = entity.getExistTick();
	}

}