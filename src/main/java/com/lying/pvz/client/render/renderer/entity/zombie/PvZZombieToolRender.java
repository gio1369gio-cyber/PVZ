package com.lying.pvz.client.render.renderer.entity.zombie;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;

/**
 * 僵尸"工具/坐骑"类实体渲染基类（DuckyTube 等）。
 * 不做 rising / mini / 朝向特化，仅保留缩放钩子。
 */
public abstract class PvZZombieToolRender<T extends Mob,
        S extends LivingEntityRenderState,
        M extends EntityModel<? super S>>
        extends MobRenderer<T, S, M> {

    public PvZZombieToolRender(EntityRendererProvider.Context ctx, M model, float shadow) {
        super(ctx, model, shadow);
    }

    @Override
    protected void scale(S state, PoseStack poseStack) {
        final float sz = getScaleByEntity(state);
        poseStack.scale(sz, sz, sz);
    }

    protected abstract float getScaleByEntity(S state);
}
