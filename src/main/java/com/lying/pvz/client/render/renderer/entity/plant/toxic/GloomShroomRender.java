package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.GloomShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.toxic.GloomShroomEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * GloomShroom 渲染器。
 *
 * <p><b>刻意差异保留</b>：{@code T = entity.getShootCD()}（运行时值），
 * 与 FumeShroom/PuffShroom 用 {@code PlantShooterEntity.SHOOT_ANIM_CD}（静态常量）
 * 不同。迁移后 T 来自 {@code state.shootCD}，<b>不统一</b>为常量。
 *
 * <p>同其他 shooter：旧 {@code getScaleByEntity} 在新基类下是死代码，
 * 改为覆写 {@code scale(PvZPlantRenderState, PoseStack)}。
 */
public class GloomShroomRender extends PvZPlantRender<GloomShroomEntity, GloomShroomModel> {

	public GloomShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GloomShroomModel(ctx.bakeLayer(GloomShroomModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	public void extractRenderState(GloomShroomEntity entity, PvZPlantRenderState state, float partialTick) {
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
			final int T = state.shootCD;                       // ← 刻意用运行时值
			final int tick = state.superTime % T;
			return size + AnimationUtil.upDown(tick, T, changeSize);
		}
		final int T = state.shootCD;                           // ← 刻意用运行时值
		final int tick = state.shootTick + T - state.shootCD;
		return tick >= 0 ? size + AnimationUtil.upDown(tick, T, changeSize) : size;
	}
}