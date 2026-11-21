package com.lying.pvz.client.render.renderer.entity.plant.light;

import com.lying.pvz.client.model.entity.plant.light.SunShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.light.SunShroomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * SunShroom 渲染器。
 *
 * <p><b>迁移点</b>：
 * <ul>
 *   <li>旧 {@code getScaleByEntity(entity)} 在新基类下是死代码
 *       （{@code PvZPlantRender.scale} 不调用它），改为覆写
 *       {@code scale(PvZPlantRenderState, PoseStack)}。</li>
 *   <li>{@code entity.isInGrowStage(1/2/3)} 通过 {@link PvZPlantRenderState#growStage}
 *       快照传递（在 {@code extractRenderState} 里算出一个 int）。</li>
 *   <li>分支顺序（stage 3 → stage 2 → stage 1）与<b>空分支
 *       {@code if(stage==3) { }} 原样保留</b>，不补全、不修正。</li>
 *   <li>{@code smallSize/bigSize/T/tick} 计算与线性插值<b>原样保留</b>。</li>
 * </ul>
 */
public class SunShroomRender extends PvZPlantRender<SunShroomEntity, SunShroomModel> {

	public SunShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SunShroomModel(ctx.bakeLayer(SunShroomModel.LAYER_LOCATION)), 0.3f);
	}

	@Override
	public void extractRenderState(SunShroomEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.growStage = entity.isInGrowStage(3) ? 3
				: entity.isInGrowStage(2) ? 2
				: entity.isInGrowStage(1) ? 1
				: 0;
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
		final float smallSize = 0.2F;
		final float bigSize = 0.35F;
//        final float hugeSize = 0.6F;
		final int T = SunShroomEntity.GROW_ANIM_CD;
		final int tick = state.existTick;
		if (tick <= 20) {
			return smallSize;
		}
		if (state.growStage == 3) {
			// 1.16.5 原逻辑：此分支为空，落到最后 return smallSize。
			// 迁移原样保留，不补全、不修正。
		}
		if (state.growStage == 2) {
			return bigSize;
		}
		if (state.growStage == 1) {
			final int now = tick + T - SunShroomEntity.GROW_CD;
			return now >= 0 ? smallSize + (bigSize - smallSize) * now / T : smallSize;
		}
		return smallSize;
	}
}