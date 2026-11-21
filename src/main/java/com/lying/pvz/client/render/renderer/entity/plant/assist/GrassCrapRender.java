package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.assist.GrassCarpModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.assist.GrassCarpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GrassCrapRender extends PvZPlantRender<GrassCarpEntity, GrassCarpModel> {

    public GrassCrapRender(EntityRendererProvider.Context context) {
        super(context, new GrassCarpModel(context.bakeLayer(GrassCarpModel.LAYER_LOCATION)), 0.6F);
    }
}