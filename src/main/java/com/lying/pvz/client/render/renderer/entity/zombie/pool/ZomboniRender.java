package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.ZomboniModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.ZomboniEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * Zomboni 渲染器。
 *
 * <p><b>isShaking 迁移</b>：1.21.1 {@code LivingEntityRenderer.isShaking(S state)}
 * 参数是 <b>render state</b> 而非 entity，签名改为
 * {@code (PvZZombieRenderState state)}。方法体 {@code zombie.isCarShaking()} 是模组
 * 自身方法，数据由 {@link PvZZombieRenderState#carShaking} 携带（在 extractRenderState 填充）。
 *
 * <p><b>与 JackInBoxZombieRender 的差异</b>：后者覆写恒 {@code true} 无需读 state；
 * 本类需读实体状态，故必须新增字段 + 覆写 extractRenderState。
 * 该覆写覆盖 {@code LivingEntityRenderer} 的默认行为（通常读 {@code state.isShaking}，
 * 由实体受伤等触发），让 Zomboni 在 {@code isCarShaking} 为真时红闪。
 */
public class ZomboniRender extends PvZZombieRender<ZomboniEntity, ZomboniModel> {

	public ZomboniRender(EntityRendererProvider.Context ctx) {
		super(ctx, new ZomboniModel(ctx.bakeLayer(ZomboniModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(ZomboniEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.carShaking = entity.isCarShaking();
	}

	@Override
	protected boolean isShaking(PvZZombieRenderState state) {
		return state.carShaking;
	}
}