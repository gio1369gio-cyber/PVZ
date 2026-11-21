package com.lying.pvz.client.render.renderer.entity.plant.arma;


import com.lying.pvz.client.model.entity.plant.arma.ChorusFruitPultModel;
import com.lying.pvz.client.render.layer.ChorusFruitPultEyeLayer;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.ChorusFruitPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChorusFruitPultRender extends PvZPlantRender<ChorusFruitPultEntity, ChorusFruitPultModel> {
    public ChorusFruitPultRender(EntityRendererProvider.Context context) {
        super(context, new ChorusFruitPultModel(context.bakeLayer(ChorusFruitPultModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public void extractRenderState(ChorusFruitPultEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.pultAnimTime = entity.getPultAnimTime();
    }

    @Override
    protected void addPlantLayers() {
        super.addPlantLayers();
        this.addLayer(new ChorusFruitPultEyeLayer<>(this));
    }
}