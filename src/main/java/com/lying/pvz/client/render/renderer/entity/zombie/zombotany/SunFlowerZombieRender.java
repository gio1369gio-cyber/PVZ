package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.zombotany.SunFlowerZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.SunFlowerZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SunFlowerZombieRender extends AbstractZombotanyRender<SunFlowerZombieEntity,SunFlowerZombieModel> {
    public SunFlowerZombieRender(EntityRendererProvider.Context context) {
        super(context, new SunFlowerZombieModel(context.bakeLayer(SunFlowerZombieModel.LAYER_LOCATION)), 0.4F);
    }
}