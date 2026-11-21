package com.lying.pvz.client.render.renderer.entity.zombie.other;


import com.lying.pvz.client.model.entity.zombie.other.MJZombieModel;
import com.lying.pvz.client.render.layer.DancerLightLayer;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.other.MJZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MJZombieRender extends PvZZombieRender<MJZombieEntity, MJZombieModel> {
    public MJZombieRender(EntityRendererProvider.Context context) {

        super(context, new MJZombieModel(context.bakeLayer(MJZombieModel.LAYER_LOCATION)), 0.5F);
    }


    @Override
    public void extractRenderState(MJZombieEntity entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.summonTime = entity.getSummonTime();
    }

    @Override
    protected void addZombieLayers() {
        super.addZombieLayers();
        this.addLayer(new DancerLightLayer<>(this));
    }
}