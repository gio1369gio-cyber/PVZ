package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.ConeHeadZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.ConeHeadZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ConeHeadZombieRender
		extends AbstractNormalRender<ConeHeadZombieEntity, ConeHeadZombieModel> {

	public ConeHeadZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ConeHeadZombieModel(ctx.bakeLayer(ConeHeadZombieModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(ConeHeadZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.innerDefenceStage = entity.hasConeHead(3) ? 3
				: entity.hasConeHead(2) ? 2
				: entity.hasConeHead(1) ? 1 : 0;
	}
}