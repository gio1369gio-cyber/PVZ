package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.CatapultZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.CatapultZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * CatapultZombie 渲染器。
 *
 * <p><b>isShaking 迁移</b>：与 {@code ZomboniRender} 完全同构，读
 * {@code isCarShaking()}。数据由 {@link PvZZombieRenderState#carShaking} 携带
 * （该字段已在 ZomboniRender 批次新增，本类<b>复用同一字段</b>，不重复声明）。
 * 方法体 {@code isCarShaking()} 是模组自身方法，逻辑原样保留。
 */
public class CatapultZombieRender extends PvZZombieRender<CatapultZombieEntity, CatapultZombieModel> {

	public CatapultZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CatapultZombieModel(ctx.bakeLayer(CatapultZombieModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(CatapultZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.carShaking = entity.isCarShaking();
		state.pultAnimTime = entity.getPultAnimTime();
		state.maxBallUse = entity.getMaxBallUse();
		state.ballCount = entity.getBallCount();
	}

	@Override
	protected boolean isShaking(PvZZombieRenderState state) {
		return state.carShaking;
	}
}