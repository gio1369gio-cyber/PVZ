package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.TallNutModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.defence.TallNutEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * TallNut 渲染器。
 *
 * <p><b>注释代码说明</b>：下方 1.16.5 的 {@code getTextureLocation(TallNutEntity)}
 * 覆写为 <b>逻辑存档</b>，按迁移原则原样保留（签名/API 名均不修正）。
 * 注意新基类的钩子是 {@code getTextureLocation(PvZPlantRenderState)}，
 * 若日后恢复该逻辑，需改为读 {@code state} 上的字段（健康 / 防御 / 超级生命
 * 均需在 {@link PvZPlantRenderState}
 * 新增并在 {@code extractRenderState} 填充）。
 */
public class TallNutRender extends PvZPlantRender<TallNutEntity, TallNutModel> {

	public TallNutRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TallNutModel(ctx.bakeLayer(TallNutModel.LAYER_LOCATION)), 0.45f);
	}

//	@Override
//	public ResourceLocation getTextureLocation(TallNutEntity entity) {
//		float life=entity.getHealth();
//		float maxLife = entity.getMaxHealth();
//		float defLife = entity.getDefenceLife();
//		float maxDefLife = entity.getSuperLife();
//		if(defLife>0) {
//    		if(defLife>2*maxDefLife/3) return StringUtil.prefix("textures/entity/plant/defence/tall_nut4.png");
//    		else if(defLife>maxDefLife/3) return StringUtil.prefix("textures/entity/plant/defence/tall_nut5.png");
//    		return StringUtil.prefix("textures/entity/plant/defence/tall_nut6.png");
//    	}
//    	else {
//    		if(life>2*maxLife/3) return StringUtil.prefix("textures/entity/plant/defence/tall_nut1.png");
//    		else if(life>maxLife/3) return StringUtil.prefix("textures/entity/plant/defence/tall_nut2.png");
//    		return StringUtil.prefix("textures/entity/plant/defence/tall_nut3.png");
//    	}
//	}
}