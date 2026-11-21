package com.lying.pvz.client.model.entity.zombie;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;

/**
 * "无掉落肢体"的僵尸模型基类。
 *
 * <p>墓碑、棺材、蹦极僵尸、僵尸海豚、海豚骑士等不会掉手掉头，使用本基类。
 * 提供 {@link IZombieModel} 所需方法：
 * <ul>
 *   <li>{@link #tickPartAnim} / {@link #renderBody} 空实现（无掉落肢体）</li>
 *   <li>{@link #getZombieModel()} 返回自身</li>
 * </ul>
 *
 * <p>泛型 {@code S} 允许两种用法：
 * <ul>
 *   <li>继承 {@code PvZZombieRenderState}——绝大多数僵尸模型</li>
 *   <li>继承 {@code LivingEntityRenderState}——如海豚骑士（不关心僵尸专属字段）</li>
 * </ul>
 */
public abstract class SimpleZombieModel<S extends LivingEntityRenderState>
        extends EntityModel<S> implements IZombieModel {

    protected SimpleZombieModel(ModelPart root) {
        super(root);
    }

    @Override
    public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {
        // 无掉落肢体，无需实现
    }

    @Override
    public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector,
                           RenderType renderType, int packedLight, int packedOverlay) {
        // 无掉落肢体，无需实现
    }

    @Override
    public EntityModel<? extends EntityRenderState> getZombieModel() {
        return this;
    }
}
