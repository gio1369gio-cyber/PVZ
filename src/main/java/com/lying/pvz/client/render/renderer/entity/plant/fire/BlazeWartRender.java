package com.lying.pvz.client.render.renderer.entity.plant.fire;

import com.lying.pvz.client.model.entity.plant.fire.BlazeWartModel;
import com.lying.pvz.client.render.layer.NetherWartLayer;
import com.lying.pvz.client.render.layer.WartEyeLayer;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.fire.BlazeWartEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BlazeWartRender extends PvZPlantRender<BlazeWartEntity,BlazeWartModel> {
    public BlazeWartRender(EntityRendererProvider.Context context) {
        super(context, new BlazeWartModel(context.bakeLayer(BlazeWartModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public void extractRenderState(BlazeWartEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // 供 NetherWartLayer 判断 hasNetherWart()（血量 ≥ 25%）。
        state.percentHealth = entity.getHealth() / entity.getMaxHealth();
    }

    @Override
    protected void addPlantLayers() {
        super.addPlantLayers();
        this.addLayer(new NetherWartLayer<>(this));
        this.addLayer(new WartEyeLayer<>(this));
    }
}