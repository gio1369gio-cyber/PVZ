package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.DiggerZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.DiggerZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * DiggerZombie 渲染器。
 *
 * <p><b>getTranslateVec 迁移</b>：旧钩子 {@code getTranslateVec(DiggerZombieEntity)}
 * 读 {@code entity.getAttackTime()} 与 {@code DiggerZombieEntity.MAX_OUT_TIME}。
 * 新钩子读 {@link PvZZombieRenderState#attackTime}（在 {@code extractRenderState} 填充）。
 * <b>注意</b>：{@code entity.getAttackTime()} 与基类 {@code state.animTime}
 * （= {@code entity.getAttackAnimTime()}）语义不同，故独立新增字段，不复用 animTime。
 * 原逻辑（maxOffset = 4D，线性插值，空格写法）原样保留，不修正计算。
 */
public class DiggerZombieRender extends PvZZombieRender<DiggerZombieEntity, DiggerZombieModel> {

	public DiggerZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DiggerZombieModel(ctx.bakeLayer(DiggerZombieModel.LAYER_LOCATION)), 0.3f);
	}

	@Override
	public void extractRenderState(DiggerZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasPickaxe = entity.hasPickaxe();
	}

	@Override
	public Vec3 getTranslateVec(PvZZombieRenderState state) {
		final double maxOffset = 4D;
		final int now = state.attackTime;
		final int max = DiggerZombieEntity.MAX_OUT_TIME;
		return new Vec3(0, maxOffset * ( 1 - now * 1.0D / max), 0);
	}
}