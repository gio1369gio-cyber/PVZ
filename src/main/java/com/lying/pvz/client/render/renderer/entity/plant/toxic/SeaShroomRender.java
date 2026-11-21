package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.SeaShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.toxic.SeaShroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * SeaShroom 渲染器。
 *
 * <p>若 {@link SeaShroomEntity} 是 shooter（{@code extends PlantShooterEntity}
 * 或其模型 {@code extends PlantShooterModel}），需覆写 extractRenderState 填
 * {@code state.shootTick/shootCD}——旧代码未体现（1.16.5 架构无需），
 * 待实体/模型源码确认。
 */
public class SeaShroomRender extends PvZPlantRender<SeaShroomEntity, SeaShroomModel> {

	public SeaShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SeaShroomModel(ctx.bakeLayer(SeaShroomModel.LAYER_LOCATION)), 0f);
	}


	 @Override
	 public void extractRenderState(SeaShroomEntity entity, PvZPlantRenderState state, float partialTick) {
	     super.extractRenderState(entity, state, partialTick);
	     state.shootTick = entity.getShootTick();
	     state.shootCD   = entity.getShootCD();
	 }
}