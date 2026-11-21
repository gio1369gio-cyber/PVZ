package com.lying.pvz.client.render.renderer.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.enforce.UmbrellaLeafModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.enforce.UmbrellaLeafEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * UmbrellaLeaf 渲染器。
 *
 * <p><b>render 覆写迁移</b>：旧 1.16.5 覆写
 * {@code render(T, float, float, PoseStack, MultiBufferSource, int)}
 * 在外层做非均匀缩放 {@code (1+width, 1+height, 1+width)}。
 * 1.20.5 引入 submit 架构后 {@code MultiBufferSource} 不再走渲染主路径，
 * 改由 {@code PvZPlantRender.scale(PvZPlantRenderState, PoseStack)} 钩子叠加：
 * <ul>
 *   <li>旧顺序：外层 scale(1+w,1+h,1+w) → super.render → 内层 (renderScale×breath + translate)</li>
 *   <li>新顺序：super.scale（内层）→ 再叠加 scale(1+w,1+h,1+w)</li>
 * </ul>
 * 两个缩放乘性可交换；{@code UmbrellaLeafRender} 未覆写 {@code getTranslateVec}，
 * 基类返回 {@code Vec3.ZERO}，translate 无位移 → <b>与旧逻辑完全等价</b>。
 *
 * <p>原 {@code percent} 读 {@code entity.getAttackTime()}，迁移后改读
 * {@link PvZPlantRenderState#attackTime}（基类已填），{@code ANIM_TICK} 常量不变。
 */
public class UmbrellaLeafRender extends PvZPlantRender<UmbrellaLeafEntity, UmbrellaLeafModel> {

	public UmbrellaLeafRender(EntityRendererProvider.Context ctx) {
		super(ctx, new UmbrellaLeafModel(ctx.bakeLayer(UmbrellaLeafModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	protected void scale(PvZPlantRenderState state, PoseStack poseStack) {
		super.scale(state, poseStack);
		final float percent = state.attackTime * 1F / UmbrellaLeafEntity.ANIM_TICK;
		final float height = 2F * percent;
		final float width  = 2F * percent;
		poseStack.scale(1 + width, 1 + height, 1 + width);
	}
}