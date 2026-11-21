package com.lying.pvz.client.render.renderer.entity.plant.appease;


import com.lying.pvz.client.model.entity.plant.appease.PeaPodModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.PeaPodEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PeaPodRender extends PvZPlantRender<PeaPodEntity, PeaPodModel> {

    public PeaPodRender(EntityRendererProvider.Context context) {
        super(context, new PeaPodModel(context.bakeLayer(PeaPodModel.LAYER_LOCATION)), 0.6F);
    }

    @Override
    public void extractRenderState(PeaPodEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.count = entity.getCount();
        state.shootTick = entity.getShootTick();
        state.shootCD = entity.getShootCD();
    }
}