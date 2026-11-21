package com.lying.pvz.client.render.renderer.entity.plant.arma;


import com.lying.pvz.client.model.entity.plant.arma.BurstKernelPultModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.arma.BurstKernelPultEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BurstKernelPultRender extends PvZPlantRender<BurstKernelPultEntity, BurstKernelPultModel> {
    public BurstKernelPultRender(EntityRendererProvider.Context context) {
        super(context, new BurstKernelPultModel(context.bakeLayer(BurstKernelPultModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public void extractRenderState(BurstKernelPultEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.currentBullet1 = entity.getCurrentBullet();
        state.pultAnimTime = entity.getPultAnimTime();

    }
}