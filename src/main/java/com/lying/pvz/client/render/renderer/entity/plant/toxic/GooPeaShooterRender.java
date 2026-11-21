package com.lying.pvz.client.render.renderer.entity.plant.toxic;


import com.lying.pvz.client.model.entity.plant.toxic.GooPeaShooterModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.toxic.GooPeaShooterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GooPeaShooterRender extends PvZPlantRender<GooPeaShooterEntity, GooPeaShooterModel> {
    public GooPeaShooterRender(EntityRendererProvider.Context context) {
        super(context, new GooPeaShooterModel(context.bakeLayer(GooPeaShooterModel.LAYER_LOCATION)), 0.4F);
    }
}