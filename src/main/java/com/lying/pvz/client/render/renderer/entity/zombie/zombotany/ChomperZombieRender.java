package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.zombotany.ChomperZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.zombotany.ChomperZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChomperZombieRender extends AbstractZombotanyRender<ChomperZombieEntity,ChomperZombieModel> {
    public ChomperZombieRender(EntityRendererProvider.Context context) {
        super(context, new ChomperZombieModel(context.bakeLayer(ChomperZombieModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void extractRenderState(ChomperZombieEntity entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.random = entity.getRandom();
        state.isResting = entity.isResting();
    }
}