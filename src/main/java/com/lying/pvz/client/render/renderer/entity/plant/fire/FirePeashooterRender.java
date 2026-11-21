package com.lying.pvz.client.render.renderer.entity.plant.fire;


import com.lying.pvz.client.model.entity.plant.fire.FirePeashooterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.fire.FirePeashooterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FirePeashooterRender extends PvZPlantRender<FirePeashooterEntity,FirePeashooterModel> {
    public FirePeashooterRender(EntityRendererProvider.Context context) {
        super(context, new FirePeashooterModel(context.bakeLayer(FirePeashooterModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public void extractRenderState(FirePeashooterEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.shootTick = entity.getShootTick();
        state.shootCD = entity.getShootCD();
    }
}