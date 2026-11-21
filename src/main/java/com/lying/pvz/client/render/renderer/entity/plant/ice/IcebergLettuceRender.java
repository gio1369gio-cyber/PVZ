package com.lying.pvz.client.render.renderer.entity.plant.ice;

import com.lying.pvz.client.model.entity.plant.ice.IcebergLettuceModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.ice.IcebergLettuceEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class IcebergLettuceRender extends PvZPlantRender<IcebergLettuceEntity, IcebergLettuceModel> {

	public IcebergLettuceRender(EntityRendererProvider.Context ctx) {
		super(ctx, new IcebergLettuceModel(ctx.bakeLayer(IcebergLettuceModel.LAYER_LOCATION)), 0.3F);
	}
}