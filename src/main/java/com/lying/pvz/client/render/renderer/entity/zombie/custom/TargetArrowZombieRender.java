package com.lying.pvz.client.render.renderer.entity.zombie.custom;

import com.lying.pvz.client.model.entity.zombie.custom.TargetArrowZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.custom.TargetArrowZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TargetArrowZombieRender extends PvZZombieRender<TargetArrowZombieEntity, TargetArrowZombieModel> {
    public TargetArrowZombieRender(EntityRendererProvider.Context context) {
        super(context, new TargetArrowZombieModel(context.bakeLayer(TargetArrowZombieModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void extractRenderState(TargetArrowZombieEntity entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.canPartsExist = entity.canPartsExist();
    }
}