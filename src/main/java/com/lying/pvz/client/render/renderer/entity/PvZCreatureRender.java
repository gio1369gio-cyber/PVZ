package com.lying.pvz.client.render.renderer.entity;

import com.lying.pvz.client.model.entity.plant.defence.PumpkinModel;
import com.lying.pvz.client.model.entity.plant.defence.SteelPumpkinModel;
import com.lying.pvz.client.render.layer.DynamicStatusLayer;
import com.lying.pvz.client.render.layer.MetalItemLayer;
import com.lying.pvz.client.render.layer.PlantLadderLayer;
import com.lying.pvz.client.render.layer.PumpkinArmorLayer;
import com.lying.pvz.client.render.layer.SteelPumpkinArmorLayer;
import com.lying.pvz.client.render.layer.fullskin.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;


public abstract class PvZCreatureRender<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
        extends MobRenderer<T, S, M> {

    protected final EntityRendererProvider.Context context;


    public PvZCreatureRender(EntityRendererProvider.Context ctx, M model, float shadow) {
        super(ctx, model, shadow);
        this.context = ctx;
        this.addCommonLayers();
    }

    @Override
    protected void scale(S state, PoseStack poseStack) {
        final float sz = getScaleByEntity(state);
        poseStack.scale(sz, sz, sz);
        final Vec3 vec = this.getTranslateVec(state);
        poseStack.translate(vec.x, vec.y, vec.z);
    }


    protected abstract float getScaleByEntity(S state);

    public Vec3 getTranslateVec(S state) {
        return Vec3.ZERO;
    }


    protected void addCommonLayers() {
        this.addLayer(new EnergyLayer<>(this));
        this.addLayer(new CharmLayer<>(this));
        this.addLayer(new DynamicStatusLayer<>(this));
        this.addLayer(new HealLightLayer<>(this));
        this.addLayer(new SunLayer<>(this));
        this.addLayer(new PumpkinArmorLayer<>(
                this, this.context.bakeLayer(PumpkinModel.ARMOR_LAYER_LOCATION)));
        this.addLayer(new SteelPumpkinArmorLayer<>(
                this, this.context.bakeLayer(SteelPumpkinModel.SteelPumpkinArmorModel.ARMOR_LAYER_LOCATION)));
    }

}
