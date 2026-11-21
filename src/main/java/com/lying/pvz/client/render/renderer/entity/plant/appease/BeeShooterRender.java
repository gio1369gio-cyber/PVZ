package com.lying.pvz.client.render.renderer.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.appease.BeeShooterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.appease.BeeShooterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BeeShooterRender extends PvZPlantRender<BeeShooterEntity, BeeShooterModel> {
    public BeeShooterRender(EntityRendererProvider.Context context) {
        super(context, new BeeShooterModel(context.bakeLayer(BeeShooterModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void extractRenderState(BeeShooterEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.shootTick = entity.getShootTick();
        state.shootCD = entity.getShootCD();
    }
}