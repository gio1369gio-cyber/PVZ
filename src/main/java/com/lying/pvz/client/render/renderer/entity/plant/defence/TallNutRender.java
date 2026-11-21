package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.TallNutModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.defence.TallNutEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * TallNut 渲染器。
 *
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