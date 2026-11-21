package com.lying.pvz.client.render.renderer.entity.plant.fire;

import com.lying.pvz.client.model.entity.plant.fire.JalapenoModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.fire.JalapenoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * Jalapeno 渲染器。
 *
 * <p>同 {@code CherryBombRender} / {@code DoomShroomRender}：旧 {@code getScaleByEntity}
 * 覆写在新基类下是死代码（{@code PvZPlantRender.scale} 不调用它），
 * 改为覆写 {@code scale(PvZPlantRenderState, PoseStack)}。
 * 逻辑 {@code 0.8F + plus/20F} 原样保留，<b>不修正计算</b>。
 *
 * <p>注：本覆写<b>替换</b>了 {@code super.getScaleByEntity} 语义——
 * 旧代码不调 super（直接返回 0.8F + plus/20F），故 {@code renderScale}
 * 不参与。迁移后保持同样语义：直接返回原表达式，不叠加 {@code state.renderScale}。
 */
public class JalapenoRender extends PvZPlantRender<JalapenoEntity, JalapenoModel> {

	public JalapenoRender(EntityRendererProvider.Context ctx) {
		super(ctx, new JalapenoModel(ctx.bakeLayer(JalapenoModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(JalapenoEntity entity, PvZPlantRenderState state, float partialTick) {
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
		return 0.8F + plus / 20F;
	}
}