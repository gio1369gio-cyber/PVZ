package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.GargantuarModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.GigaGargantuarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * GigaGargantuar 渲染器。
 *
 * <p><b>刻意共用 GargantuarModel</b>：GigaGargantuar 与 Gargantuar 共用模型，
 * 不是独立模型——1.16.5 原逻辑，原样保留（逻辑一致优先）。
 * {@code GargantuarModel<>} 泛型与 {@link GargantuarRender} <b>必须同改</b>。
 */
public class GigaGargantuarRender extends PvZZombieRender<GigaGargantuarEntity, GargantuarModel> {

	public GigaGargantuarRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GargantuarModel(ctx.bakeLayer(GargantuarModel.LAYER_LOCATION)), 1F);
	}

	@Override
	public void extractRenderState(GigaGargantuarEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.crushCD = entity.getCrushCD();
		state.animThrowCD = entity.getThrowCD();
		state.isSad = entity.isSad;
		state.hasImp = entity.hasImp();
		state.toolType = entity.getToolType();
		state.percentHealth = entity.getHealth() / entity.getMaxHealth();
	}
}