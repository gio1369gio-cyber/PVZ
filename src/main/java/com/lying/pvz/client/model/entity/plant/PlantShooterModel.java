package com.lying.pvz.client.model.entity.plant;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.utils.AnimationUtil;
import net.minecraft.client.model.geom.ModelPart;

import java.util.Optional;

/**
 * 射手植物模型基类。
 */
public abstract class PlantShooterModel extends PvZPlantModel {

    protected PlantShooterModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        // 触发 Model.setupAnim -> resetPose()，防止子类叠加自定 part 时跨帧累积
        super.setupAnim(state);

        this.getHeadModel().ifPresent(m -> m.xRot = 0.1f);
        this.getBodyModel().ifPresent(m -> m.xRot = -0.1f);

        if (state.superMode) {
            final int T = PlantShooterEntity.SHOOT_ANIM_CD;
            final int tick = state.superTime % T;
            if (tick >= 0) {
                this.getHeadModel().ifPresent(m ->
                        m.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle() / 5) + 0.1f);
                this.getBodyModel().ifPresent(m ->
                        m.xRot = AnimationUtil.getUpDownUpDown(tick, T, -getMaxRotAngle() / 5) - 0.1f);
            }
        } else {
            final int T = PlantShooterEntity.SHOOT_ANIM_CD;
            final int tick = state.shootTick + T - state.shootCD;
            if (tick >= 0) {
                this.getHeadModel().ifPresent(m ->
                        m.xRot = AnimationUtil.getUpDownUpDown(tick, T, -getMaxRotAngle() / 2) + 0.1f);
                this.getBodyModel().ifPresent(m ->
                        m.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle()) - 0.1f);
            }
        }
    }

    public float getMaxRotAngle() {
        return 15F;
    }

    public Optional<ModelPart> getHeadModel() {
        return Optional.empty();
    }

    public Optional<ModelPart> getBodyModel() {
        return Optional.empty();
    }
}