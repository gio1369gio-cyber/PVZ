package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.JackInBoxZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.JackInBoxZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * JackInBoxZombie 渲染器。
 *
 * <p><b>isShaking 迁移</b>：1.21.1 {@code LivingEntityRenderer.isShaking(S state)}
 * 参数是 <b>render state</b> 而非 entity，签名改为 {@code (PvZZombieRenderState state)}。
 * 覆写恒 {@code true}（旧 1.16.5 即如此），原样保留，不修正。
 * 该覆写覆盖 {@code LivingEntityRenderer} 的默认行为（通常读
 * {@code state.isShaking}，由实体受伤等触发）。
 */
public class JackInBoxZombieRender extends PvZZombieRender<JackInBoxZombieEntity, JackInBoxZombieModel> {

	public JackInBoxZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new JackInBoxZombieModel(ctx.bakeLayer(JackInBoxZombieModel.LAYER_LOCATION)), 0.45f);
	}

	@Override
	protected boolean isShaking(PvZZombieRenderState state) {
		return true;
	}
}