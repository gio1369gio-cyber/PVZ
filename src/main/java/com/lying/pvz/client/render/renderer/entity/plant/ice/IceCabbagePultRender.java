package com.lying.pvz.client.render.renderer.entity.plant.ice;


import com.lying.pvz.client.model.entity.plant.ice.IceCabbagePultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.ice.IceCabbagePultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class IceCabbagePultRender extends PvZPlantRender<IceCabbagePultEntity, IceCabbagePultModel> {
	public IceCabbagePultRender(EntityRendererProvider.Context context) {
		super(context, new IceCabbagePultModel(context.bakeLayer(IceCabbagePultModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public void extractRenderState(IceCabbagePultEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.pultAnimTime = entity.getPultAnimTime();
	}
}