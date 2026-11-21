package com.lying.pvz.client.render.renderer.entity.plant;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.model.entity.misc.LadderModel;
import com.lying.pvz.client.model.entity.plant.defence.PumpkinModel;
import com.lying.pvz.client.render.layer.PlantLadderLayer;
import com.lying.pvz.client.render.layer.PumpkinArmorLayer;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantProducerEntity;
import com.lying.pvz.utils.AlgorithmUtil;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class PvZPlantRender<T extends PlantEntity,
        M extends EntityModel<? super PvZPlantRenderState>>
        extends PvZCreatureRender<T, PvZPlantRenderState, M> {

    private static final int BREATH_ANIM_CD = 40;

    /** 未注册 client 元数据时的兜底贴图。 */
    private static final Identifier FALLBACK_TEXTURE = StringUtil.id("entity/plant/missing");

    protected final EntityRendererProvider.Context context;

    public PvZPlantRender(EntityRendererProvider.Context ctx, M model, float shadow) {
        super(ctx, model, shadow);
        this.context = ctx;
        this.addPlantLayers();
    }

    @Override
    public PvZPlantRenderState createRenderState() {
        return new PvZPlantRenderState();
    }

    @Override
    public void extractRenderState(T entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.plantTypeId = entity.getPlantType().getID();
        state.existTick   = entity.getExistTick();
        state.superMode   = entity.isPlantInSuperMode();
        state.superTime   = entity.getSuperTime();
        state.attackTime  = entity.getAttackTime();
        state.innerDefenceLife = entity.getInnerDefenceLife();
        state.outerDefenceLife = entity.getOuterDefenceLife();
        state.charmed = entity.isCharmed();
        state.boostTime = entity.getBoostTime();
        state.metal = entity.hasMetal();
        state.steelPumpkin = AlgorithmUtil.getSteelPumpkin(entity);
        state.cold = EntityUtil.isEntityCold(entity);
        state.frozen = EntityUtil.isEntityFrozen(entity);
        state.buttered = EntityUtil.isEntityButter(entity);
        state.marks = entity.views();
        if (entity instanceof PlantProducerEntity producer) {
            state.sunLight = producer.isPlantInGen();
        }
        // state.shootTick    = entity.getShootTick();       // 子类实现（Shooter 系列）
        // state.shootCD      = entity.getShootCD();         // 子类实现（Shooter 系列）
        // state.pultAnimTime = entity.getPultAnimTime();    // 子类实现（MelonPult 系列）

        final Optional<IClientPAZInfo> info = PVZAPI.client().paz(state.plantTypeId);
        state.texture     = info.map(IClientPAZInfo::getTexture).orElse(FALLBACK_TEXTURE);
        state.renderScale = info.map(IClientPAZInfo::getRenderScale).orElse(1.0F);
    }

    @Override
    protected void scale(PvZPlantRenderState state, PoseStack poseStack) {
        final int live = state.existTick % BREATH_ANIM_CD;
        final float breath = AnimationUtil.upDown(live, BREATH_ANIM_CD, 0.01F);
        final float sz = state.renderScale * (1 + breath);
        final Vec3 vec = this.getTranslateVec(state);
        poseStack.scale(sz, sz, sz);
        poseStack.translate(vec.x, vec.y, vec.z);
    }

    @Override
    protected float getScaleByEntity(PvZPlantRenderState state) {
        return state.renderScale;
    }

    @Override
    public Vec3 getTranslateVec(PvZPlantRenderState state) {
        return Vec3.ZERO;
    }

    @Override
    public Identifier getTextureLocation(PvZPlantRenderState state) {
        return state.texture;
    }

    protected void addPlantLayers() {
        this.addLayer(new PlantLadderLayer<>(this, this.context.bakeLayer(LadderModel.LAYER_LOCATION)));
    }
}