package com.lying.pvz.client.render.renderer.entity.plant.ice;

import com.lying.pvz.client.model.entity.plant.ice.SnowPeaModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.ice.SnowPeaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * SnowPea 渲染器。
 *
 * <p>语义上是 shooter（豌豆射手变体）。若 {@link SnowPeaModel} 继承
 * {@code PlantShooterModel}，其 {@code setupAnim} 读 {@code state.shootTick/shootCD}，
 * 而基类 {@code PvZPlantRender.extractRenderState} 中这两行被注释（"子类实现（Shooter 系列）"），
 * 故本类必须填充，否则动画卡末帧。与 appease 包 shooter 处理一致。
 */
public class SnowPeaRender extends PvZPlantRender<SnowPeaEntity, SnowPeaModel> {

	public SnowPeaRender(EntityRendererProvider.Context ctx) {
		super(ctx, new SnowPeaModel(ctx.bakeLayer(SnowPeaModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public void extractRenderState(SnowPeaEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.shootTick = entity.getShootTick();
		state.shootCD   = entity.getShootCD();
	}
}