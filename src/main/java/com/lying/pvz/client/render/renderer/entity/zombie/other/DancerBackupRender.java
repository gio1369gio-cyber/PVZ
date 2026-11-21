package com.lying.pvz.client.render.renderer.entity.zombie.other;


import com.lying.pvz.client.model.entity.zombie.other.DancerBackupModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.common.level.entity.zombie.other.DancerBackupEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DancerBackupRender extends PvZZombieRender<DancerBackupEntity, DancerBackupModel> {
    public DancerBackupRender(EntityRendererProvider.Context context) {

        super(context, new DancerBackupModel(context.bakeLayer(DancerBackupModel.LAYER_LOCATION)), 0.5F);
    }
}