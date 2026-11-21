package com.lying.pvz.client.render.renderer.entity.plant.explosion;

import com.lying.pvz.client.model.entity.plant.explosion.PotatoMineModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.explosion.PotatoMineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * PotatoMine 渲染器。
 *
 * <p><b>迁移点</b>：
 * <ul>
 *   <li>{@code getScaleByEntity(entity)} → 覆写 {@code scale(PvZPlantRenderState, PoseStack)}
 *       （基类 {@code PvZPlantRender.scale} 不调用 getScaleByEntity，原覆写为死代码）。</li>
 *   <li>{@code getTranslateVec(entity)} → {@code getTranslateVec(PvZPlantRenderState)}，
 *       {@code Vector3d} → {@code Vec3}。</li>
 *   <li>原逻辑（isMineReady 分支、isRisingFromDirt 分支、offsetY / RISING_ANIM_CD 计算）
 *       全部原样保留，<b>不修正任何计算</b>。</li>
 * </ul>
 *
 * <p>{@link PotatoMineEntity#RISING_ANIM_CD} 为实体静态常量，直接从实体类引用。
 */
public class PotatoMineRender extends PvZPlantRender<PotatoMineEntity, PotatoMineModel> {

	public PotatoMineRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PotatoMineModel(ctx.bakeLayer(PotatoMineModel.LAYER_LOCATION)), 0.3F);
	}

	@Override
	public void extractRenderState(PotatoMineEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.attackCD         = entity.getAttackCD();
		state.mineReady        = entity.isMineReady();
		state.risingFromDirt   = entity.isRisingFromDirt();
		state.prepareCD        = entity.getPrepareCD();
		state.signChangeCD = entity.getSignChangeCD();
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
		final float sz = super.getScaleByEntity(state);
		if (state.mineReady) {
			final float scale = 0.15F;
			return sz + state.attackTime * scale / state.attackCD;
		}
		return sz;
	}

	@Override
	public Vec3 getTranslateVec(PvZPlantRenderState state) {
		final float offsetY = 0.6F;
		if (state.risingFromDirt) {
			final int time = state.prepareCD - state.existTick;
			return new Vec3(0, time * offsetY / PotatoMineEntity.RISING_ANIM_CD, 0);
		}
		return state.mineReady ? new Vec3(0, 0, 0) : new Vec3(0, offsetY, 0);
	}
}