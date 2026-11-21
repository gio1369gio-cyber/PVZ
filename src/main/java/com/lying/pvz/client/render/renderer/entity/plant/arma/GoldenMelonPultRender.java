package com.lying.pvz.client.render.renderer.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.arma.GoldenMelonPultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.GoldenMelonPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GoldenMelonPultRender extends PvZPlantRender<GoldenMelonPultEntity, GoldenMelonPultModel> {
    public GoldenMelonPultRender(EntityRendererProvider.Context context) {
        super(context, new GoldenMelonPultModel(context.bakeLayer(GoldenMelonPultModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public void extractRenderState(GoldenMelonPultEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.pultAnimTime = entity.getPultAnimTime();
    }
}