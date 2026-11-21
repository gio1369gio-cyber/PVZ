package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.DancingZombieModel;
import com.lying.pvz.client.render.layer.DancerLightLayer;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.DancingZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * DancingZombie 渲染器。
 *
 * <p><b>DancerLightLayer 泛型</b>：1.21.1 {@code RenderLayer<S extends EntityRenderState,
 * M extends EntityModel<S>>} 约束下，旧 {@code new DancerLightLayer<>(this)} 的菱形推断
 * 可能失败，显式写为 {@code <PvZZombieRenderState, DancingZombieModel>}。
 */
public class DancingZombieRender extends PvZZombieRender<DancingZombieEntity, DancingZombieModel> {

	public DancingZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DancingZombieModel(ctx.bakeLayer(DancingZombieModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(DancingZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.summonTime = entity.getSummonTime();
	}

	@Override
	protected void addZombieLayers() {
		super.addZombieLayers();
		this.addLayer(new DancerLightLayer<>(this));
	}
}