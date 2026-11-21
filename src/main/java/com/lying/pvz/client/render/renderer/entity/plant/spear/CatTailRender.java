package com.lying.pvz.client.render.renderer.entity.plant.spear;

import com.lying.pvz.client.model.entity.plant.spear.CatTailModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.spear.CatTailEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * CatTail 渲染器。
 *
 * <p><b>getTranslateVec 迁移</b>：旧钩子 {@code getTranslateVec(CatTailEntity)} 读
 * {@code entity.isInWater()}；新钩子 {@code getTranslateVec(PvZPlantRenderState)}，
 * 不调用 super（旧代码即如此）。原文件无 @OnlyIn，保持不加。
 */
public class CatTailRender extends PvZPlantRender<CatTailEntity, CatTailModel> {

	public CatTailRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CatTailModel(ctx.bakeLayer(CatTailModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(CatTailEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isInWater = entity.isInWater();
		state.animCD = entity.getAnimCD();
	}

	@Override
	public Vec3 getTranslateVec(PvZPlantRenderState state) {
		if (state.isInWater) {
			return new Vec3(0, -0.5D, 0);
		}
		return new Vec3(0, 0, 0);
	}
}