package com.lying.pvz.client.render.renderer.entity.plant.defence;


import com.lying.pvz.client.model.entity.plant.defence.SelfImitaterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.defence.SelfImitaterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SelfImitaterRender extends PvZPlantRender<SelfImitaterEntity, SelfImitaterModel> {
    public SelfImitaterRender(EntityRendererProvider.Context context) {
        super(context, new SelfImitaterModel(context.bakeLayer(SelfImitaterModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public void extractRenderState(SelfImitaterEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isAlive = entity.isAlive();
        state.imitaterChance = entity.getImitaterChance();
        state.isImitating = entity.isImitating();
    }
}