package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.GraveBusterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.assist.GraveBusterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * GraveBuster 渲染器。
 *
 * <p>吃墓碑时整体下沉 1.5 格的动画。原 1.16.5 通过 {@code getTranslateVec(entity)}
 * 从实体读 {@code isEatingTomb / getAttackTime / getEatTombCD}；迁移后钩子签名改为
 * {@code getTranslateVec(PvZPlantRenderState)}，数据由 {@link PvZPlantRenderState} 携带，
 * 故需在 state 中新增 {@code eatingTomb}、{@code eatTombCD} 两个字段
 * （见下方 {@code PvZPlantRenderState 补丁}）。
 *
 * <p><b>原逻辑原样保留</b>，不趁迁移修正任何计算。
 */
public class GraveBusterRender extends PvZPlantRender<GraveBusterEntity, GraveBusterModel> {

	public GraveBusterRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GraveBusterModel(ctx.bakeLayer(GraveBusterModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(GraveBusterEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.eatingTomb = entity.isEatingTomb();
		state.eatTombCD  = entity.getEatTombCD();
	}

	@Override
	public Vec3 getTranslateVec(PvZPlantRenderState state) {
		if (state.eatingTomb) {
			final float height = 1.5f;
			final float downOffset = (1 - state.attackTime * 1.0f / state.eatTombCD) * height;
			return new Vec3(0, -downOffset, 0);
		}
		return super.getTranslateVec(state);
	}
}