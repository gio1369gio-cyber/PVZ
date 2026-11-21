package com.lying.pvz.client.model.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// MJZombieModel —— MJ 僵尸
// 形状：较瘦长的标准僵尸骨架 + MJ 头（头主体 + 头顶帽 + 两侧鬓发 + 后脑小方 + 上身），
//       三种状态：召唤、攻击时自转 + 双臂高举、平时 super
public class MJZombieModel extends PvZZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("mj_zombie"), "main");

    private final ModelPart total;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart up;
    private final ModelPart left_hand;
    private final ModelPart right_hand;
    private final ModelPart head;
    private final ModelPart body;

    public MJZombieModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.right_leg = this.total.getChild("right_leg");
        this.left_leg = this.total.getChild("left_leg");
        this.up = this.total.getChild("up");
        this.left_hand = this.up.getChild("left_hand");
        this.right_hand = this.up.getChild("right_hand");
        this.head = this.up.getChild("head");
        this.body = this.up.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(218, 239).addBox(-4.0F, 24.0F, -6.0F, 8.0F, 6.0F, 11.0F, new CubeDeformation(-0.5F))
                        .texOffs(0, 226).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(5.0F, -30.0F, 0.0F));

        total.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(36, 239).addBox(-4.0F, 24.0F, -6.0F, 8.0F, 6.0F, 11.0F, new CubeDeformation(-0.5F))
                        .texOffs(96, 226).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(-5.0F, -30.0F, 0.0F));

        PartDefinition up = total.addOrReplaceChild("up",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -30.0F, 0.0F));

        up.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(60, 188).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(11.0F, -21.0F, 1.0F));

        up.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(6, 179).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(-11.0F, -22.0F, 0.0F));

        up.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-6.0F, -12.0F, -5.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(196, 84).addBox(-8.0F, -18.0F, -5.0F, 16.0F, 6.0F, 14.0F, CubeDeformation.NONE)
                        .texOffs(226, 0).addBox(-10.0F, -15.0F, -4.0F, 4.0F, 12.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(226, 0).addBox(6.0F, -15.0F, -4.0F, 4.0F, 12.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(162, 212).addBox(-6.0F, -12.0F, 7.0F, 12.0F, 8.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        up.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(141, 224).addBox(-8.0F, -24.0F, -3.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(PvZZombieRenderState state) {
        if (state.summonTime  > 0) {
            this.total.yRot = 0.0F;
            this.left_hand.xRot = 0.0F;
            this.right_hand.xRot = -2.5F;
            this.left_leg.xRot = 0.0F;
            this.right_leg.xRot = 0.0F;
        } else if (state.attackTime > 0) {
            int tick = state.attackTime;
            int max = 100;
            this.total.yRot = -Mth.sin(6.28318F * (float) tick / (float) max);
            this.right_hand.xRot = -3.0F * Mth.abs(Mth.sin(12.56636F * (float) tick / (float) max));
            this.left_hand.xRot = -3.0F * Mth.abs(Mth.sin(12.56636F * (float) tick / (float) max));
            this.left_leg.xRot = 0.0F;
            this.right_leg.xRot = 0.0F;
        } else {
            super.setupAnim(state);
        }
    }

    public void refreshAnim() {
        this.total.yRot = 0.0F;
    }

    public ModelPart getZombieLeftHand() {
        return this.left_hand;
    }

    public ModelPart getZombieRightHand() {
        return this.right_hand;
    }

    public ModelPart getZombieLeftLeg() {
        return this.left_leg;
    }

    public ModelPart getZombieRightLeg() {
        return this.right_leg;
    }

    public ModelPart getZombieHead() {
        return this.head;
    }

    public ModelPart getZombieUpBody() {
        return this.up;
    }

    public ModelPart getZombieWholeBody() {
        return this.total;
    }
}