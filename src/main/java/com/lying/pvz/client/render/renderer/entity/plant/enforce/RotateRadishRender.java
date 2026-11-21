package com.lying.pvz.client.render.renderer.entity.plant.enforce;


import com.lying.pvz.client.model.entity.plant.enforce.RotateRadishModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.enforce.RotateRadishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RotateRadishRender extends PvZPlantRender<RotateRadishEntity,RotateRadishModel> {
    public RotateRadishRender(EntityRendererProvider.Context context) {
        super(context, new RotateRadishModel(context.bakeLayer(RotateRadishModel.LAYER_LOCATION)), 0.6f);
    }
}