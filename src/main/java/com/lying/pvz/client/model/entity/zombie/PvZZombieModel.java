package com.lying.pvz.client.model.entity.zombie;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.PvZEntityModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;

import java.util.Optional;

/**
 * 僵尸模型基类。
 *
 * <p>固定 {@link PvZZombieRenderState} 作为 render state 类型。
 */
public abstract class PvZZombieModel extends PvZEntityModel<PvZZombieRenderState> implements IZombieModel {

    protected static final float HAND_MAX_ANGLE = AnimationUtil.byDegree(-120F);
    protected static final int MAX_ANIM_CD = PVZZombieEntity.PERFORM_ATTACK_CD;

    /** 升起动画总时长，与 {@link PVZZombieEntity#RISING_CD} 保持一致。 */
    protected static final int MAX_RISING_CD = PVZZombieEntity.RISING_CD;

    /**
     * 完全埋入地下时的额外下沉深度（模型单位，1 方块 = 16 单位）。
     * <p>默认 32 单位 = 2 方块，足够让 1.98 高的僵尸头也沉到脚下。
     * <p>子类可覆写以调整视觉深度。
     */
    protected static final float RISING_DEPTH_UNITS = 32.0F;

    protected boolean isLeftHandFree = true;
    protected boolean isRightHandFree = true;
    protected boolean isHeadFree = true;

    /**
     * 掉落肢体渲染时右手的初始角度（度）。供子类覆写以调整 body drop 的姿势；
     * 默认 0 表示自然下垂。
     */
    protected float rightHandOriginAngel = 0;

    protected PvZZombieModel(ModelPart root) {
        super(root);
    }

    /* ------------------------------------------------------------------ */
    /* 掉落肢体渲染（IBodyEntity 路径，与本体分离）                         */
    /* ------------------------------------------------------------------ */

    @Override
    public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {
        switch (entity.getBodyType()) {
            case BODY -> {
                int cd = 20;
                if (entity.getAnimTime() < cd) {
                    this.getZombieWholeBody().xRot = AnimationUtil.getUp(entity.getAnimTime(), cd, 90);
                    this.getZombieRightHand().xRot = AnimationUtil.byDegree(rightHandOriginAngel)
                            - AnimationUtil.getUp(entity.getAnimTime(), cd, 180 + rightHandOriginAngel);
                    if (entity.hasHandDefence()) {
                        this.getZombieLeftHand().xRot = AnimationUtil.byDegree(rightHandOriginAngel)
                                - AnimationUtil.getUp(entity.getAnimTime(), cd, 180 + rightHandOriginAngel);
                    }
                } else {
                    this.getZombieWholeBody().xRot = AnimationUtil.byDegree(90);
                    this.getZombieRightHand().xRot = AnimationUtil.byDegree(-180);
                    if (entity.hasHandDefence()) {
                        this.getZombieLeftHand().xRot = AnimationUtil.byDegree(-180);
                    }
                }
            }
            default -> { }
        }
    }

    /**
     * 渲染掉落肢体。
     *
     * <p><b>模型实例共享约束</b>：本方法可能作用于与本体共享的 {@link ModelPart}。
     * 所有被修改的 {@code visible / pos / rot} 都必须在 {@link #renderBody} 内
     * 保存-恢复，避免污染本体渲染（或其它 drop body 渲染）。
     */
    @Override
    public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector,
                           RenderType renderType, int packedLight, int packedOverlay) {
        switch (entity.getBodyType()) {
            case HAND -> {
                final ModelPart part = this.getZombieLeftHand();
                part.visible = true;
                part.setPos(0, 24, 0);
                part.xRot = 0;
                collector.submitModelPart(part, stack, renderType, packedLight, packedOverlay, null);
            }
            case HEAD -> {
                final ModelPart part = this.getZombieHead();
                final ModelPart helmet = this.getHelmet().orElse(null);
                part.visible = true;
                if (helmet != null) helmet.visible = false;
                part.setPos(0, 24, 0);
                collector.submitModelPart(part, stack, renderType, packedLight, packedOverlay, null);
            }
            case BODY -> {
                final ModelPart leftHand    = this.getZombieLeftHand();
                final ModelPart head        = this.getZombieHead();
                final ModelPart handDefence = this.getHandDefence().orElse(null);
                leftHand.visible = entity.hasHandDefence();
                head.visible     = false;
                if (handDefence != null) handDefence.visible = entity.hasHandDefence();
                collector.submitModelPart(this.getZombieWholeBody(), stack, renderType,
                        packedLight, packedOverlay, null);
            }
            default -> { }
        }
    }

    /* ------------------------------------------------------------------ */
    /* 本体渲染                                                            */
    /* ------------------------------------------------------------------ */

    @Override
    public void setupAnim(PvZZombieRenderState state) {
        // 触发 Model.setupAnim -> resetPose()，防止子类叠加自定 part 时跨帧累积
        super.setupAnim(state);
        this.refreshAnim();
        this.updateFreeParts(state);
        this.doWalkAnimation(state.walkAnimationPos, state.walkAnimationSpeed, state.yRot, state.xRot);
        this.performAttack(state);
        this.doRisingAnimation(state);
    }

    public void refreshAnim() {
    }

    public void updateFreeParts(PvZZombieRenderState state) {
        this.getZombieLeftHand().visible = state.hasHand;
        this.getZombieHead().visible = state.hasHead;
    }

    protected void doWalkAnimation(float limbSwing, float limbSwingAmount,
                                   float netHeadYaw, float headPitch) {
        this.getZombieHead().yRot = netHeadYaw / (180F / (float) Math.PI);
        this.getZombieHead().xRot = headPitch / (180F / (float) Math.PI);
        this.getZombieLeftLeg().xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.getZombieRightLeg().xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        if (this.isRightHandFree) {
            this.getZombieRightHand().xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
        if (this.isLeftHandFree) {
            this.getZombieLeftHand().xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }
    }

    protected void performAttack(PvZZombieRenderState state) {
        if (state.animTime > 0) {
            if (this.isLeftHandFree || this.isRightHandFree) {
                this.doHandEat(state);
            }
        } else {
            if (isZombieAngry(state)) {
                this.doPreAttackPose();
            }
        }
    }

    protected void doPreAttackPose() {
        if (this.isLeftHandFree || this.isRightHandFree) {
            if (this.isLeftHandFree) {
                this.getZombieLeftHand().xRot = HAND_MAX_ANGLE;
            }
            if (this.isRightHandFree) {
                this.getZombieRightHand().xRot = HAND_MAX_ANGLE;
            }
        }
    }

    protected void doHandEat(PvZZombieRenderState state) {
        if (this.isLeftHandFree) {
            this.getZombieLeftHand().xRot = HAND_MAX_ANGLE
                    - AnimationUtil.getUpDown(MAX_ANIM_CD - state.animTime, MAX_ANIM_CD, -70);
        }
        if (this.isRightHandFree) {
            this.getZombieRightHand().xRot = HAND_MAX_ANGLE
                    - AnimationUtil.getUpDown(MAX_ANIM_CD - state.animTime, MAX_ANIM_CD, -70);
        }
    }

    /**
     * 升起动画：身体整体下沉，随 {@code risingAnimTime} 从 {@code -RISING_CD} 逼近 0
     * 逐渐露出地面。
     *
     * <p>{@code risingAnimTime >= 0}（已完全露出）时不做任何处理。
     *
     * <p>偏移施加在 {@link #getZombieWholeBody()} 的 {@code y} 上，
     * 已由 {@code super.setupAnim(state)} 的 {@code resetPose()} 归零，安全。
     */
    protected void doRisingAnimation(PvZZombieRenderState state) {
        if (state.risingAnimTime >= 0) {
            return;
        }
        final float ratio = Mth.clamp(
                (state.risingAnimTime + MAX_RISING_CD) / (float) MAX_RISING_CD, 0.0F, 1.0F);
        this.getZombieWholeBody().y += RISING_DEPTH_UNITS * (1.0F - ratio);
    }

    protected boolean isZombieAngry(PvZZombieRenderState state) {
        return state.aggressive;
    }


    @Override
    public EntityModel<? extends EntityRenderState> getZombieModel() {
        return this;
    }

    public Optional<ModelPart> getHelmet() {
        return Optional.empty();
    }

    public Optional<ModelPart> getHandDefence() {
        return Optional.empty();
    }

    public abstract ModelPart getZombieLeftHand();
    public abstract ModelPart getZombieRightHand();
    public abstract ModelPart getZombieLeftLeg();
    public abstract ModelPart getZombieRightLeg();
    public abstract ModelPart getZombieHead();
    public abstract ModelPart getZombieUpBody();
    public abstract ModelPart getZombieWholeBody();
}