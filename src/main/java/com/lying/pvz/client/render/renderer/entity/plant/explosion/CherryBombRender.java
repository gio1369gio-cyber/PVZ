package com.lying.pvz.client.render.renderer.entity.plant.explosion;

import com.lying.pvz.client.model.entity.plant.explosion.CherryBombModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.explosion.CherryBombEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * CherryBomb 渲染器。
 *
 * <p><b>scale 覆写迁移</b>：旧 1.16.5 覆写 {@code getScaleByEntity(entity)}
 * 在 {@code renderScale} 基础上加 {@code attackTime/readyTime × 0.15F} 的成长量。
 * 新基类 {@link PvZPlantRender#scale} 已改为硬编码 {@code renderScale × breath}，
 * <b>不再调用 getScaleByEntity</b>，故该覆写在新架构下是死代码。
 * 为保证动态缩放生效，本类改为覆写 {@code scale(PvZPlantRenderState, PoseStack)}，
 * 行为与旧 1.16.5 一致（无呼吸缩放，仅动态缩放）。
 */
public class CherryBombRender extends PvZPlantRender<CherryBombEntity, CherryBombModel> {

	public CherryBombRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CherryBombModel(ctx.bakeLayer(CherryBombModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(CherryBombEntity entity, PvZPlantRenderState state, float partialTick) {
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
		final float plus = state.attackTime * 1F / state.readyTime;
		return super.getScaleByEntity(state) + plus * 0.15F;
	}
}