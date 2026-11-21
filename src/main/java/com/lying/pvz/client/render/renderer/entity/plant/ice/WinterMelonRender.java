package com.lying.pvz.client.render.renderer.entity.plant.ice;

import com.lying.pvz.client.model.entity.plant.ice.WinterMelonModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.ice.WinterMelonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * WinterMelon 渲染器。
 *
 * <p>语义上是 MelonPult 变体（投掷冰冻西瓜）。若模型读 {@code state.pultAnimTime}，
 * 而基类 {@code PvZPlantRender.extractRenderState} 中该行被注释（"子类实现（MelonPult 系列）"），
 * 则本类必须填充。与 arma 包 4 个 pult 处理一致。
 */
public class WinterMelonRender extends PvZPlantRender<WinterMelonEntity, WinterMelonModel> {

	public WinterMelonRender(EntityRendererProvider.Context ctx) {
		super(ctx, new WinterMelonModel(ctx.bakeLayer(WinterMelonModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void extractRenderState(WinterMelonEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
	}
}