package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.FumeShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.level.entity.plant.toxic.FumeShroomEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * FumeShroom 渲染器。
 *
 * <p>旧 {@code getScaleByEntity} 覆写在新基类下是死代码
 * （{@code PvZPlantRender.scale} 不调用它），改为覆写
 * {@code scale(PvZPlantRenderState, PoseStack)}。逻辑（superMode/常规两种 tick 分支 +
 * {@code AnimationUtil.upDown}）原样保留。T 用 {@code PlantShooterEntity.SHOOT_ANIM_CD}。
 */
public class FumeShroomRender extends PvZPlantRender<FumeShroomEntity, FumeShroomModel> {

	public FumeShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new FumeShroomModel(ctx.bakeLayer(FumeShroomModel.LAYER_LOCATION)), 0.7f);
	}

	@Override
	public void extractRenderState(FumeShroomEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}

	@Override
	protected void scale(PvZPlantRenderState state, PoseStack poseStack) {
		final float sz = getScaleByEntity(state);
		final Vec3 vec = this.getTranslateVec(state);
		poseStack.scale(sz, sz, sz);
		poseStack.translate(vec.x, vec.y, vec.z);
	}

	@Override
	protected float getScaleByEntity(PvZPlantRenderState state) {
		final float changeSize = 0.1F;
		final float size = super.getScaleByEntity(state);
		if (state.superMode) {
			final int T = PlantShooterEntity.SHOOT_ANIM_CD;
			final int tick = state.superTime % T;
			return size + AnimationUtil.upDown(tick, T, changeSize);
		}
		final int T = PlantShooterEntity.SHOOT_ANIM_CD;
		final int tick = state.shootTick + T - state.shootCD;
		return tick >= 0 ? size + AnimationUtil.upDown(tick, T, changeSize) : size;
	}
}