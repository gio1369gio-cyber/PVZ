package com.lying.pvz.client.render.renderer.entity.plant.light;

import com.lying.pvz.client.model.entity.plant.light.GoldLeafModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.light.GoldLeafEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GoldLeafRender extends PvZPlantRender<GoldLeafEntity, GoldLeafModel> {

	public GoldLeafRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GoldLeafModel(ctx.bakeLayer(GoldLeafModel.LAYER_LOCATION)), 0.35F);
	}


	@Override
	public void extractRenderState(GoldLeafEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		   if (entity.getAttackTime() + 30 > entity.getReadyTime()) {
			state.sunLight = true;
		}else if (entity.getGoldTime() + 20 > GoldLeafEntity.GOLD_GEN_CD) {
			   state.sunLight = true;
		   }


	}
}