package com.lying.pvz.client.render.renderer.entity.zombie.custom;

import com.lying.pvz.client.model.entity.zombie.custom.RedEdgeZombieModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.custom.RedEdgeZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RedEdgeZombieRender extends PvZZombieRender<RedEdgeZombieEntity, RedEdgeZombieModel> {
    public RedEdgeZombieRender(EntityRendererProvider.Context context) {
        super(context, new RedEdgeZombieModel(context.bakeLayer(RedEdgeZombieModel.LAYER_LOCATION)), 1.0f);
    }
}