package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.BucketHeadZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.BucketHeadZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BucketHeadZombieRender
		extends AbstractNormalRender<BucketHeadZombieEntity, BucketHeadZombieModel> {

	public BucketHeadZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new BucketHeadZombieModel(ctx.bakeLayer(BucketHeadZombieModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(BucketHeadZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.innerDefenceStage = entity.hasBucketHead(3) ? 3
				: entity.hasBucketHead(2) ? 2
				: entity.hasBucketHead(1) ? 1 : 0;
	}
}