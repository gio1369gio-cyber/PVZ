package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.PogoZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.PogoZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * PogoZombie 渲染器。
 *
 * <p><b>getTranslateVec 迁移</b>：旧钩子读 {@code entity.hasMetal()}；
 * 新钩子读 {@link PvZZombieRenderState#hasMetal}（在 extractRenderState 填充）。
 * 分支逻辑（!hasMetal → (0, 0.4, 0)；否则回退 super）原样保留。
 * 原文件无 @OnlyIn，保持不加。
 */
public class PogoZombieRender extends PvZZombieRender<PogoZombieEntity, PogoZombieModel> {

	public PogoZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PogoZombieModel(ctx.bakeLayer(PogoZombieModel.LAYER_LOCATION)), 0.45f);
	}

	@Override
	public void extractRenderState(PogoZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasPogo = entity.hasPogo();
	}

	@Override
	public Vec3 getTranslateVec(PvZZombieRenderState state) {
		if (!state.hasMetal) return new Vec3(0, 0.4, 0);
		return super.getTranslateVec(state);
	}
}