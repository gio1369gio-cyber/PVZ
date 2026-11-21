package com.lying.pvz.client.render.renderer.entity.zombie.boss;

import com.lying.pvz.client.model.entity.zombie.boss.SphinxTerminatorModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.boss.SphinxTerminatorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SphinxTerminatorRender <T extends SphinxTerminatorEntity> extends PvZZombieRender<T, SphinxTerminatorModel> {


    public SphinxTerminatorRender(EntityRendererProvider.Context ctx) {
        super(ctx, new SphinxTerminatorModel(ctx.bakeLayer(SphinxTerminatorModel.LAYER_LOCATION)), 0);
    }

    @Override
    public void extractRenderState(T entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // 复用 summonTime 传递 BOSS 状态 ordinal 给模型动画
        state.summonTime = entity.getState().ordinal();
        state.dashYaw = entity.getDashYaw();
    }
}
