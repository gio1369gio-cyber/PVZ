package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.layer.component.WallNutArmorLayer;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.defence.ExplodeONutEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ExplodeNutRender extends PvZPlantRender<ExplodeONutEntity, WallNutModel> {
    public ExplodeNutRender(EntityRendererProvider.Context ctx) {
        super(ctx,new WallNutModel(ctx.bakeLayer(WallNutModel.LAYER_LOCATION_2)), 0.55f);
    }

    @Override
    protected void addPlantLayers() {
        super.addPlantLayers();
        this.addLayer(new WallNutArmorLayer(this));
    }

}
