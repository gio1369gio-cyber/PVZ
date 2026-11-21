package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.ScaredyShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.toxic.ScaredyShroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * ScaredyShroom 渲染器。
 *
 * <p><b>getTranslateVec 迁移</b>：旧钩子 {@code getTranslateVec(ScaredyShroomEntity)}
 * 读 {@code isScared() / getScareTime()}；新钩子读 {@link PvZPlantRenderState#scared}
 * 与 {@link PvZPlantRenderState#scareTime}（在 extractRenderState 填充）。
 * 原逻辑（{@code percent = scareTime/SCARE_ANIM_CD}，{@code change = 1.38}，上移
 * {@code change * percent}；否则回退 super）原样保留，不修正计算。
 *
 * <p>若 {@link ScaredyShroomEntity} 是 shooter（{@code extends PlantShooterEntity}
 * 或其模型 {@code extends PlantShooterModel}），需在 {@code extractRenderState}
 * 内追加 {@code state.shootTick/shootCD} 填充——待实体/模型源码确认。
 */
public class ScaredyShroomRender extends PvZPlantRender<ScaredyShroomEntity, ScaredyShroomModel> {

	public ScaredyShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ScaredyShroomModel(ctx.bakeLayer(ScaredyShroomModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(ScaredyShroomEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.scared    = entity.isScared();
		state.scareTime = entity.getScareTime();
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}

	@Override
	public Vec3 getTranslateVec(PvZPlantRenderState state) {
		if (state.scared) {
			final double percent = state.scareTime * 1.0 / ScaredyShroomEntity.SCARE_ANIM_CD;
			final double change = 1.38;
			return new Vec3(0, change * percent, 0);
		}
		return super.getTranslateVec(state);
	}
}