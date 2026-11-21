package com.lying.pvz.client.model.entity.plant.fire;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// BlazeWartModel —— 烈焰疣（火系近战植物）
// 形状：一个方胖主体（body，三层叠块，中段外皮略膨胀）+ 正面两只眼（face）；
//       左右各一条由 5 节骨链拼成的弯曲手臂（right_arm→right1~right5 / left_arm→left1~left5），
//       left_arm 初始朝后（yRot=3.1416），攻击时左右手交替摆动
public class BlazeWartModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("blaze_wart"), "main");

    private final ModelPart total;
    private final ModelPart right_arm;
    private final ModelPart right1;
    private final ModelPart right2;
    private final ModelPart right3;
    private final ModelPart right4;
    private final ModelPart right5;
    private final ModelPart left_arm;
    private final ModelPart left1;
    private final ModelPart left2;
    private final ModelPart left3;
    private final ModelPart left4;
    private final ModelPart left5;
    private final ModelPart face;
    private final ModelPart body;
    private final ModelPart getPlantWholeBody;

    public BlazeWartModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.right_arm = this.total.getChild("right_arm");
        this.right1 = this.right_arm.getChild("right1");
        this.right2 = this.right1.getChild("right2");
        this.right3 = this.right2.getChild("right3");
        this.right4 = this.right3.getChild("right4");
        this.right5 = this.right4.getChild("right5");
        this.left_arm = this.total.getChild("left_arm");
        this.left1 = this.left_arm.getChild("left1");
        this.left2 = this.left1.getChild("left2");
        this.left3 = this.left2.getChild("left3");
        this.left4 = this.left3.getChild("left4");
        this.left5 = this.left4.getChild("left5");
        this.face = this.total.getChild("face");
        this.body = this.total.getChild("body");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        // ===== 右臂 =====
        PartDefinition right_arm = total.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(89, 80).addBox(-4.0F, -3.5F, -1.0F, 4.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(-5.0F, -0.75F, 0.0F));

        PartDefinition right1 = right_arm.addOrReplaceChild("right1",
                CubeListBuilder.create()
                        .texOffs(63, 78).addBox(-2.8706F, -3.517F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right2 = right1.addOrReplaceChild("right2",
                CubeListBuilder.create()
                        .texOffs(114, 113).addBox(-3.75F, -3.567F, -0.5F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-3.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right3 = right2.addOrReplaceChild("right3",
                CubeListBuilder.create()
                        .texOffs(64, 85).addBox(-4.6464F, -3.6464F, -0.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right4 = right3.addOrReplaceChild("right4",
                CubeListBuilder.create()
                        .texOffs(97, 72).addBox(-5.0F, -4.0F, -1.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.3927F));

        right4.addOrReplaceChild("right5",
                CubeListBuilder.create()
                        .texOffs(14, 67).addBox(-4.0F, -5.0F, -2.0F, 4.0F, 4.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        // ===== 左臂（初始 yRot=3.1416，朝后）=====
        PartDefinition left_arm = total.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(73, 78).addBox(-4.0F, -3.5F, -1.0F, 4.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(5.0F, -0.75F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition left1 = left_arm.addOrReplaceChild("left1",
                CubeListBuilder.create()
                        .texOffs(57, 123).addBox(-2.8706F, -3.517F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition left2 = left1.addOrReplaceChild("left2",
                CubeListBuilder.create()
                        .texOffs(38, 74).addBox(-3.75F, -3.567F, -2.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition left3 = left2.addOrReplaceChild("left3",
                CubeListBuilder.create()
                        .texOffs(52, 74).addBox(-4.6464F, -3.6464F, -2.0F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition left4 = left3.addOrReplaceChild("left4",
                CubeListBuilder.create()
                        .texOffs(83, 75).addBox(-5.0F, -4.0F, -1.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(-3.8232F, -0.1768F, -0.5F, 0.0F, 0.0F, 0.3927F));

        left4.addOrReplaceChild("left5",
                CubeListBuilder.create()
                        .texOffs(68, 69).addBox(-4.0F, -5.0F, -2.5F, 4.0F, 4.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        // ===== face =====
        total.addOrReplaceChild("face",
                CubeListBuilder.create()
                        .texOffs(56, 116).addBox(0.5F, -8.0F, -6.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(-0.45F))
                        .texOffs(0, 88).addBox(-5.5F, -8.0F, -6.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offset(0.0F, -2.25F, -1.0F));

        // ===== body =====
        total.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 83).addBox(-1.0F, -4.5F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.4F))
                        .texOffs(0, 100).addBox(-2.0F, -10.5F, -7.0F, 14.0F, 10.0F, 14.0F, CubeDeformation.NONE)
                        .texOffs(56, 114).addBox(-1.0F, -11.25F, -6.0F, 12.0F, 2.0F, 12.0F, CubeDeformation.NONE),
                PartPose.offset(-5.0F, -0.75F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        if (state.attackTime == 0) {
            this.left_arm.xRot = 0.0F;
            this.left_arm.yRot = 3.1416F;
            this.left_arm.zRot = 0.0F;
            this.right_arm.xRot = 0.0F;
            this.right_arm.yRot = 0.0F;
            this.right_arm.zRot = 0.0F;
        } else {
            int now;
            int tot;
            if (state.attackTime < 0) {
                now = state.attackTime;
                tot = state.attackCD;
                this.right_arm.xRot = AnimationUtil.getUpDown((float) now, (float) tot, 60.0F);
                this.right_arm.yRot = AnimationUtil.getUpDown((float) now, (float) tot, -60.0F);
            } else {
                now = -state.attackTime;
                tot = state.attackCD;
                this.left_arm.xRot = AnimationUtil.getUpDown((float) now, (float) tot, -60.0F);
                this.left_arm.yRot = -(3.1416F - AnimationUtil.getUpDown((float) now, (float) tot, 60.0F));
            }
        }
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }

}