package com.lying.pvz.client.render.renderer.entity.zombie;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class PvZZombieRender<T extends PVZZombieEntity,
        M extends EntityModel<? super PvZZombieRenderState>>
        extends PvZCreatureRender<T, PvZZombieRenderState, M> {

    public static final float MINI_SCALE = 0.32F;

    private static final Identifier FALLBACK_TEXTURE = StringUtil.id( "entity/zombie/missing");

    protected final EntityRendererProvider.Context context;

    public PvZZombieRender(EntityRendererProvider.Context ctx, M model, float shadow) {
        super(ctx, model, shadow);
        this.context = ctx;
        this.addZombieLayers();
    }

    @Override
    public PvZZombieRenderState createRenderState() {
        return new PvZZombieRenderState();
    }

    @Override
    public void extractRenderState(T entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.zombieTypeId   = entity.getZombieType().getID();
        state.animTime       = entity.getAnimTime();
        state.risingAnimTime = entity.getRisingAnimTime();
        state.aggressive     = entity.isAggressive();
        state.hasHand        = entity.hasHand();
        state.hasHead        = entity.hasHead();
        state.hasMetal = entity.hasMetal();
        state.innerDefenceLife = entity.getInnerDefenceLife();
        state.outerDefenceLife = entity.getOuterDefenceLife();
        state.charmed = entity.isCharmed();
        state.variantType = entity.getVariantType();
        state.cold = EntityUtil.isEntityCold(entity);
        state.frozen = EntityUtil.isEntityFrozen(entity);
        state.buttered = EntityUtil.isEntityButter(entity);
        state.marks = entity.views();
        state.attackTime = entity.getAttackTime();
        // state.hasHandDefence = entity.hasHandDefence();   // 子类实现
        state.mini           = entity.isMiniZombie();
        state.lightEyeEffect = ClientUtil.getClientPlayer().hasEffect(PvZEffectRegister.LIGHT_EYE_EFFECT);
        final Optional<IClientPAZInfo> info = PVZAPI.client().paz(state.zombieTypeId);
        state.texture     = info.map(IClientPAZInfo::getTexture).orElse(FALLBACK_TEXTURE);
        state.renderScale = info.map(IClientPAZInfo::getRenderScale).orElse(1.0F);
    }

    @Override
    protected float getScaleByEntity(PvZZombieRenderState state) {
        return state.mini ? state.renderScale * MINI_SCALE : state.renderScale;
    }

    /**
     * rising 下沉由 {@code PvZZombieModel.doRisingAnimation} 在模型内部对
     * {@code getZombieWholeBody().y} 处理，这里不再叠加，避免双重下沉。
     */
    @Override
    public Vec3 getTranslateVec(PvZZombieRenderState state) {
        return Vec3.ZERO;
    }

    @Override
    public Identifier getTextureLocation(PvZZombieRenderState state) {
        return state.texture;
    }

    protected void addZombieLayers() {
    }
}