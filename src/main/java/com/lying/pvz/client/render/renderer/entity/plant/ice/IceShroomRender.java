package com.lying.pvz.client.render.renderer.entity.plant.ice;

import com.lying.pvz.client.model.entity.plant.ice.IceShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.ice.IceShroomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * IceShroom 渲染器。
 *
 * <p>同 {@code CherryBombRender}：旧 {@code getScaleByEntity} 覆写在新基类下是死代码
 * （{@code PvZPlantRender.scale} 不调用它），改为覆写
 * {@code scale(PvZPlantRenderState, PoseStack)}。逻辑
 * {@code super.getScaleByEntity(entity) + plus*0.15F} 原样保留。
 */
public class IceShroomRender extends PvZPlantRender<IceShroomEntity, IceShroomModel> {

	public IceShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new IceShroomModel(ctx.bakeLayer(IceShroomModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(IceShroomEntity entity, PvZPlantRenderState state, float partialTick) {
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