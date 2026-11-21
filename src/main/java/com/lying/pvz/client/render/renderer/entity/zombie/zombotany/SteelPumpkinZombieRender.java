package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.zombotany.SteelPumpkinZombieModel;
import com.lying.pvz.common.level.entity.zombie.zombotany.SteelPumpkinZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SteelPumpkinZombieRender extends AbstractZombotanyRender<SteelPumpkinZombieEntity, SteelPumpkinZombieModel> {
    public SteelPumpkinZombieRender(EntityRendererProvider.Context context) {
        super(context, new SteelPumpkinZombieModel(context.bakeLayer(SteelPumpkinZombieModel.LAYER_LOCATION)), 0.6f);
    }
}