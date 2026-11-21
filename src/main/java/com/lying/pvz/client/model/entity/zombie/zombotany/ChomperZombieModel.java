package com.lying.pvz.client.model.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// ChomperZombieModel —— 大嘴花僵尸
// 形状：标准僵尸骨架 + 一个巨大的嘴头：
//   head 主体（12×10×4）+ 头顶小天线（bone14→bone15）
//   up_mouse / down_mouse 上下两片带牙的大嘴（各含大量 1×1 小牙块）
//   leaf 下方 6 片围绕排布的薄叶（bone16~bone21）
//   tongue → tongue2 两层舌头
// 攻击动画：嘴巴开合、头前后摆动、舌头随机抖动
public class ChomperZombieModel extends PvZZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("chomper_zombie"), "main");

    private final ModelPart total;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart up;
    private final ModelPart body;
    private final ModelPart left_hand;
    private final ModelPart right_hand;
    private final ModelPart head;
    private final ModelPart bone14;
    private final ModelPart bone15;
    private final ModelPart up_mouse;
    private final ModelPart bone9;
    private final ModelPart bone8;
    private final ModelPart bone10;
    private final ModelPart bone11;
    private final ModelPart bone12;
    private final ModelPart bone13;
    private final ModelPart down_mouse;
    private final ModelPart leaf;
    private final ModelPart bone16;
    private final ModelPart bone17;
    private final ModelPart bone18;
    private final ModelPart bone19;
    private final ModelPart bone20;
    private final ModelPart bone21;
    private final ModelPart tongue;
    private final ModelPart tongue2;

    public ChomperZombieModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.right_leg = this.total.getChild("right_leg");
        this.left_leg = this.total.getChild("left_leg");
        this.up = this.total.getChild("up");
        this.body = this.up.getChild("body");
        this.left_hand = this.up.getChild("left_hand");
        this.right_hand = this.up.getChild("right_hand");
        this.head = this.up.getChild("head");
        this.bone14 = this.head.getChild("bone14");
        this.bone15 = this.bone14.getChild("bone15");
        this.up_mouse = this.head.getChild("up_mouse");
        this.bone9 = this.up_mouse.getChild("bone9");
        this.bone8 = this.bone9.getChild("bone8");
        this.bone10 = this.up_mouse.getChild("bone10");
        this.bone11 = this.bone10.getChild("bone11");
        this.bone12 = this.up_mouse.getChild("bone12");
        this.bone13 = this.bone12.getChild("bone13");
        this.down_mouse = this.head.getChild("down_mouse");
        this.leaf = this.head.getChild("leaf");
        this.bone16 = this.leaf.getChild("bone16");
        this.bone17 = this.leaf.getChild("bone17");
        this.bone18 = this.leaf.getChild("bone18");
        this.bone19 = this.leaf.getChild("bone19");
        this.bone20 = this.leaf.getChild("bone20");
        this.bone21 = this.leaf.getChild("bone21");
        this.tongue = this.head.getChild("tongue");
        this.tongue2 = this.tongue.getChild("tongue2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-4.0F, -24.0F, 0.0F));

        total.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(4.0F, -24.0F, 0.0F));

        PartDefinition up = total.addOrReplaceChild("up",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        up.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        up.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(12.0F, -20.0F, 0.0F));

        up.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-12.0F, -20.0F, 0.0F));

        PartDefinition head = up.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(200, 113).addBox(-6.0F, -5.0F, -2.0F, 12.0F, 10.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -29.1569F, 1.2471F, -0.1863F, 0.0F, 0.0F));

        PartDefinition bone14 = head.addOrReplaceChild("bone14",
                CubeListBuilder.create()
                        .texOffs(233, 107).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, -0.9599F, 0.0F, 0.0F));

        bone14.addOrReplaceChild("bone15",
                CubeListBuilder.create()
                        .texOffs(163, 45).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition up_mouse = head.addOrReplaceChild("up_mouse",
                CubeListBuilder.create()
                        .texOffs(203, 94).addBox(6.0F, -1.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(150, 96).addBox(-6.0F, -2.0F, -12.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(231, 89).addBox(-5.0F, -1.0F, -13.0F, 10.0F, 4.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(228, 70).addBox(-7.0F, -1.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-6.0F, 3.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-6.0F, 3.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-6.0F, 3.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-6.0F, 3.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-6.0F, 3.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-5.0F, 3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-3.0F, 3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(-1.0F, 3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(1.0F, 3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(3.0F, 3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(5.0F, 3.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(5.0F, 3.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(5.0F, 3.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(5.0F, 3.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(141, 110).addBox(5.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition bone9 = up_mouse.addOrReplaceChild("bone9",
                CubeListBuilder.create()
                        .texOffs(137, 99).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.0F, -10.0F, -0.0873F, 0.0F, 0.0F));

        bone9.addOrReplaceChild("bone8",
                CubeListBuilder.create()
                        .texOffs(179, 44).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition bone10 = up_mouse.addOrReplaceChild("bone10",
                CubeListBuilder.create()
                        .texOffs(132, 90).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.0F, -6.0F, -0.1745F, 0.0F, 0.0F));

        bone10.addOrReplaceChild("bone11",
                CubeListBuilder.create()
                        .texOffs(190, 44).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition bone12 = up_mouse.addOrReplaceChild("bone12",
                CubeListBuilder.create()
                        .texOffs(160, 89).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, -0.2618F, 0.0F, 0.0F));

        bone12.addOrReplaceChild("bone13",
                CubeListBuilder.create()
                        .texOffs(147, 91).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        head.addOrReplaceChild("down_mouse",
                CubeListBuilder.create()
                        .texOffs(174, 79).addBox(6.0F, -1.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(133, 63).addBox(-6.0F, -1.0F, -12.0F, 12.0F, 4.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(202, 88).addBox(-5.0F, -1.0F, -13.0F, 10.0F, 3.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(201, 71).addBox(-7.0F, -1.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-6.0F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(5.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-6.0F, -2.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-6.0F, -2.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-6.0F, -2.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-6.0F, -2.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-4.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(-2.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(0.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(2.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(4.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(5.0F, -2.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(5.0F, -2.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(5.0F, -2.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(133, 110).addBox(5.0F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition leaf = head.addOrReplaceChild("leaf",
                CubeListBuilder.create()
                        .texOffs(192, 73).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 2.0F));

        leaf.addOrReplaceChild("bone16",
                CubeListBuilder.create()
                        .texOffs(186, 65).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 0.0F));

        leaf.addOrReplaceChild("bone17",
                CubeListBuilder.create()
                        .texOffs(198, 64).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 1.0472F));

        leaf.addOrReplaceChild("bone18",
                CubeListBuilder.create()
                        .texOffs(220, 64).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 2.0944F));

        leaf.addOrReplaceChild("bone19",
                CubeListBuilder.create()
                        .texOffs(236, 61).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.2618F, 0.0F, -3.1416F));

        leaf.addOrReplaceChild("bone20",
                CubeListBuilder.create()
                        .texOffs(247, 61).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -1.0472F));

        leaf.addOrReplaceChild("bone21",
                CubeListBuilder.create()
                        .texOffs(209, 57).addBox(-1.0F, -3.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -2.0944F));

        PartDefinition tongue = head.addOrReplaceChild("tongue",
                CubeListBuilder.create()
                        .texOffs(135, 53).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, -3.0F));

        tongue.addOrReplaceChild("tongue2",
                CubeListBuilder.create()
                        .texOffs(151, 52).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, -4.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(PvZZombieRenderState state) {
        super.setupAnim(state);

        int tick = state.attackTime;
        if (tick > 0 && tick <= 15) {
            this.head.xRot = 0.25F - 0.075F * (float) tick;
            if (tick <= 10) {
                this.up_mouse.xRot = -0.52F - 0.05F * (float) tick;
                this.down_mouse.xRot = 0.52F + 0.05F * (float) tick;
            } else {
                float tmp = (float) tick - 20.0F;
                this.up_mouse.xRot = 0.82F + 0.2F * tmp;
                this.down_mouse.xRot = -0.82F - 0.2F * tmp;
                this.head.xRot = 0.47F;
            }
        } else if (tick > 15) {
            int tmp = tick - 15;
            this.head.xRot = -0.5F + 0.075F * (float) tmp;
            this.up_mouse.xRot = -0.05F * (float) tmp;
            this.down_mouse.xRot = 0.05F * (float) tmp;
        } else {
            if (state.isResting) {
                this.tongue.xRot = state.random.nextFloat() - 0.5F;
                this.tongue2.xRot = state.random.nextFloat() - 0.5F;
                this.head.xRot = 1.04F;
                this.up_mouse.xRot = -0.15F;
                this.down_mouse.xRot = 0.15F;
                this.leaf.zRot = 0.52F;
            } else {
                this.tongue.xRot = 0.0F;
                this.tongue2.xRot = 0.0F;
                this.head.xRot = 0.25F;
                this.up_mouse.xRot = -0.52F;
                this.down_mouse.xRot = 0.52F;
                this.leaf.zRot = 0.0F;
            }
        }
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