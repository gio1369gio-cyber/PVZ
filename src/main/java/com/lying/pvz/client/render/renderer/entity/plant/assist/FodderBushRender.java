package com.lying.pvz.client.render.renderer.entity.plant.assist;


import com.lying.pvz.client.model.entity.plant.assist.FodderBushModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.assist.FodderBushEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FodderBushRender extends PvZPlantRender<FodderBushEntity,FodderBushModel> {
	public FodderBushRender(EntityRendererProvider.Context context) {
		super(context, new FodderBushModel(context.bakeLayer(FodderBushModel.LAYER_LOCATION)), 0.5F);
	}
}