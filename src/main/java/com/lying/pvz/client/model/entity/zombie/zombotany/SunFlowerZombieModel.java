package com.lying.pvz.client.model.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
// SunFlowerZombieModel —— 向日葵僵尸
// 形状：标准僵尸骨架，头部是三层叠成的向日葵花冠（中层 + 上层 + 顶层大花盘）
public class SunFlowerZombieModel extends PvZZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sun_flower_zombie"), "main");

    private final ModelPart total;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final ModelPart up;
    private final ModelPart head;
    private final ModelPart right_hand;
    private final ModelPart left_hand;
    private final ModelPart body;

    public SunFlowerZombieModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.left_leg = this.total.getChild("left_leg");
        this.right_leg = this.total.getChild("right_leg");
        this.up = this.total.getChild("up");
        this.head = this.up.getChild("head");
        this.right_hand = this.up.getChild("right_hand");
        this.left_hand = this.up.getChild("left_hand");
        this.body = this.up.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(4.0F, -24.0F, 0.0F));

        total.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-4.0F, -24.0F, 0.0F));

        PartDefinition up = total.addOrReplaceChild("up",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        up.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(158, 184).addBox(-6.0F, -14.0F, -2.0F, 12.0F, 12.0F, 8.0F, CubeDeformation.NONE)
                        .texOffs(130, 155).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 14.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(133, 130).addBox(-10.0F, -18.0F, -4.0F, 20.0F, 18.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        up.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-12.0F, -20.0F, 0.0F));

        up.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(12.0F, -20.0F, 0.0F));

        up.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
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