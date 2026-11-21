package com.lying.pvz.client.render.renderer.entity.plant.appease;


import com.lying.pvz.client.model.entity.plant.appease.PrimalPeaShooterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.PrimalPeaShooterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PrimalPeaShooterRender extends PvZPlantRender<PrimalPeaShooterEntity, PrimalPeaShooterModel> {
    public PrimalPeaShooterRender(EntityRendererProvider.Context context) {
        super(context, new PrimalPeaShooterModel(context.bakeLayer(PrimalPeaShooterModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void extractRenderState(PrimalPeaShooterEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.shootTick = entity.getShootTick();
        state.shootCD = entity.getShootCD();
    }
}