package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.GargantuarModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.GargantuarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * Gargantuar 渲染器。
 *
 * <p><b>GargantuarModel&lt;&gt; 泛型待核</b>：与 {@link GigaGargantuarRender}
 * 共用 {@link GargantuarModel}，两处<b>必须同改</b>。以实际模型签名为准。
 */
public class GargantuarRender extends PvZZombieRender<GargantuarEntity, GargantuarModel> {

	public GargantuarRender(EntityRendererProvider.Context ctx) {
		super(ctx, new GargantuarModel(ctx.bakeLayer(GargantuarModel.LAYER_LOCATION)), 1F);
	}

	@Override
	public void extractRenderState(GargantuarEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.crushCD = entity.getCrushCD();
		state.animThrowCD = entity.getThrowCD();
		state.isSad = entity.isSad;
		state.hasImp = entity.hasImp();
		state.toolType = entity.getToolType();
		state.percentHealth = entity.getHealth() / entity.getMaxHealth();

	}
}