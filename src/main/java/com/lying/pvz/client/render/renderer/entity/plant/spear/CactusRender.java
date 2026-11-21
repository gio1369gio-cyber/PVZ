package com.lying.pvz.client.render.renderer.entity.plant.spear;

import com.lying.pvz.client.model.entity.plant.spear.CactusModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.spear.CactusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * Cactus 渲染器。
 *
 * <p><b>getTranslateVec 迁移</b>：旧钩子 {@code getTranslateVec(CactusEntity)} 读
 * {@code entity.getCactusHeight()}；新钩子 {@code getTranslateVec(PvZPlantRenderState)}，
 * 数据由 {@link PvZPlantRenderState#cactusHeight} 携带（在 extractRenderState 填充）。
 * 原逻辑（按 cactusHeight 下移）原样保留，不修正计算。不调用 super（旧代码即如此）。
 */
public class CactusRender extends PvZPlantRender<CactusEntity, CactusModel> {

	public CactusRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CactusModel(ctx.bakeLayer(CactusModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(CactusEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.cactusHeight = entity.getCactusHeight();
		state.isCactusPowered = entity.isCactusPowered();
	}

	@Override
	public Vec3 getTranslateVec(PvZPlantRenderState state) {
		final float h = state.cactusHeight;
		return new Vec3(0, -h, 0);
	}
}