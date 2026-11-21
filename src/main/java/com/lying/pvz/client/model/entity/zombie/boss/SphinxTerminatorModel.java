package com.lying.pvz.client.model.entity.zombie.boss;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;

public class SphinxTerminatorModel extends EntityModel<PvZZombieRenderState> implements IZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(StringUtil.id("sphinx_terminator"), "main");

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart jaw;
    private final ModelPart nemes;
    private final ModelPart body;
    private final ModelPart leg;
    private final ModelPart left_front;
    private final ModelPart right_front;
    private final ModelPart left_hind;
    private final ModelPart right_hind;

    public SphinxTerminatorModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.head = this.total.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.jaw = this.head.getChild("jaw");
        this.nemes = this.head.getChild("nemes");
        this.body = this.total.getChild("body");
        this.leg = this.total.getChild("leg");
        this.left_front = this.leg.getChild("left_front");
        this.right_front = this.leg.getChild("right_front");
        this.left_hind = this.leg.getChild("left_hind");
        this.right_hind = this.leg.getChild("right_hind");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 7.0F));

        PartDefinition head = total.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -15.0F, -12.75F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 1.75F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(38, 24).addBox(-3.0F, -16.0F, -19.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.25F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(34, 35).addBox(-5.0F, -6.0F, -17.0F, 10.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.25F));

        PartDefinition nemes = head.addOrReplaceChild("nemes", CubeListBuilder.create().texOffs(0, 39).addBox(-8.0F, -18.0F, -13.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 39).addBox(6.0F, -18.0F, -13.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-8.0F, -20.0F, -13.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.25F));

        PartDefinition body = total.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -1.0F, -4.5F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -3.5F));

        PartDefinition leg = total.addOrReplaceChild("leg", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition left_front = leg.addOrReplaceChild("left_front", CubeListBuilder.create().texOffs(16, 39).addBox(-5.0F, 0.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_front = leg.addOrReplaceChild("right_front", CubeListBuilder.create().texOffs(24, 39).addBox(3.0F, 0.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_hind = leg.addOrReplaceChild("left_hind", CubeListBuilder.create().texOffs(32, 43).addBox(-5.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_hind = leg.addOrReplaceChild("right_hind", CubeListBuilder.create().texOffs(40, 43).addBox(3.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    /**
     * 按 BOSS 状态驱动动画。渲染器把状态 ordinal 写进 {@code state.summonTime}，
     * attackTime 提供计时。
     * <ul>
     *   <li>SUMMON：jaw 张合（张嘴召唤僵尸）；</li>
     *   <li>MISSILE：眼睛放大发亮；</li>
     *   <li>DASH_CHARGE：身体后仰 + 前右腿反复刨地蓄力 + 眼睛闪烁；</li>
     *   <li>DASH：身体前倾冲向摧毁方向 + 四腿交替蹬跑；</li>
     *   <li>JUMP：四腿收拢。</li>
     * </ul>
     *
     * <p>蓄力/冲刺阶段实体已通过 {@code setYBodyRot(dashYaw)} 让整体朝向冲刺方向，
     * 渲染器用 bodyRot 旋转模型，故模型无需再额外补偿朝向。
     */
    @Override
    public void setupAnim(PvZZombieRenderState state) {
        super.setupAnim(state);
        // 复位（含四肢，避免上一个状态的腿角度残留）
        this.jaw.xRot = 0;
        this.body.xRot = 0;
        this.total.yRot = 0;
        // 眼睛（head 部件）默认跟随 netHeadYaw（头部绝对朝向 = bodyRot + yRot）。
        // 蓄力/冲刺时在下方 case 3/4 覆盖为精确锁定 dashYaw。
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.eyes.visible = true;
        this.eyes.xScale = 1F;
        this.eyes.yScale = 1F;
        this.left_front.xRot = 0;
        this.right_front.xRot = 0;
        this.left_hind.xRot = 0;
        this.right_hind.xRot = 0;

        final int status = state.summonTime;
        final int t = state.attackTime;

        switch (status) {
            case 1 -> { // SUMMON：jaw 张合
                this.jaw.xRot = t < 12 ? (t / 12F) * 0.6F : (1F - (t - 12) / 12F) * 0.6F;
            }
            case 2 -> { // MISSILE：眼睛放大
                this.eyes.xScale = 1.3F;
                this.eyes.yScale = 1.3F;
            }
            case 3 -> { // DASH_CHARGE：后仰 + 前右腿刨地蓄力 + 眼睛闪
                this.body.xRot = -0.25F;
                this.eyes.visible = (t % 8 < 4);
                // 眼睛精确锁定冲刺方向：head 绝对朝向 = bodyRot + head.yRot = dashYaw
                this.head.yRot = Mth.wrapDegrees(state.dashYaw - state.bodyRot) * Mth.DEG_TO_RAD;
                // 前右腿反复向后刨（蓄力蹬地），幅度随蓄力进度加大
                this.right_front.xRot = 0.25F + Math.abs(Mth.sin(t * 0.18F)) * 0.55F;
            }
            case 4 -> { // DASH：前倾冲向摧毁方向 + 四腿交替蹬跑
                this.body.xRot = 0.45F;
                this.head.yRot = Mth.wrapDegrees(state.dashYaw - state.bodyRot) * Mth.DEG_TO_RAD;
                final float gait = state.ageInTicks * 1.1F;
                this.left_front.xRot = (float) Math.sin(gait) * 0.7F;
                this.right_front.xRot = (float) Math.sin(gait + Math.PI) * 0.7F;
                this.left_hind.xRot = (float) Math.sin(gait + Math.PI) * 0.7F;
                this.right_hind.xRot = (float) Math.sin(gait) * 0.7F;
            }
            case 5 -> { // JUMP：收腿
                this.left_front.xRot = -0.5F;
                this.right_front.xRot = -0.5F;
                this.left_hind.xRot = 0.5F;
                this.right_hind.xRot = 0.5F;
            }
            default -> { }
        }
    }

    @Override
    public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {

    }

    @Override
    public EntityModel<? extends EntityRenderState> getZombieModel() {
        return this;
    }
}
