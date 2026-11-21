package com.lying.pvz.client.render.renderer.entity.plant.explosion;

import com.lying.pvz.client.model.entity.plant.explosion.DoomShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.explosion.DoomShroomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * DoomShroom 渲染器。
 *
 * <p>同 {@link CherryBombRender}：旧 {@code getScaleByEntity} 覆写在新基类
 * 下是死代码（{@code PvZPlantRender.scale} 不调用它），改为覆写
 * {@code scale(PvZPlantRenderState, PoseStack)}，逻辑 {@code 1f + plus² × 0.5f} 原样保留。
 *
 * <p>注：{@code DoomShroomModel} 同时被特效渲染器 {@code DoomFixerRender} 引用，
 * 两处的 bakeLayer 构造方式需一并核对。
 */
public class DoomShroomRender extends PvZPlantRender<DoomShroomEntity, DoomShroomModel> {

	public DoomShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DoomShroomModel(ctx.bakeLayer(DoomShroomModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(DoomShroomEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.readyTime = entity.getReadyTime();
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
		final float plus = state.attackTime * 1f / state.readyTime;
		return 1f + plus * plus * 0.5f;
	}
}