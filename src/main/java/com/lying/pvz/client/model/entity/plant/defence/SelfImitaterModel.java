package com.lying.pvz.client.model.entity.plant.defence;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// SelfImitaterModel —— 模仿者
// 形状：三段叠成的躯干（body，上/中/下三块，中段外皮略膨胀、下段再大一圈），
//       顶部一个歪斜的薄片头（head + cube_r1/cube_r2），头顶正面一张由四块拼成的脸（face + cube_r3），
//       躯干两侧四只细手臂（hands 下 hand1~hand4，hand4 再带一层 hand4_r1）
public class SelfImitaterModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("self_imitater"), "main");

    private final ModelPart total;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart hands;
    private final ModelPart hand1;
    private final ModelPart hand2;
    private final ModelPart hand3;
    private final ModelPart hand4;
    private final ModelPart hand4_r1;
    private final ModelPart face;
    private final ModelPart cube_r3;
    private final ModelPart getPlantWholeBody;

    public SelfImitaterModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.body = this.total.getChild("body");
        this.head = this.total.getChild("head");
        this.cube_r1 = this.head.getChild("cube_r1");
        this.cube_r2 = this.head.getChild("cube_r2");
        this.hands = this.total.getChild("hands");
        this.hand1 = this.hands.getChild("hand1");
        this.hand2 = this.hands.getChild("hand2");
        this.hand3 = this.hands.getChild("hand3");
        this.hand4 = this.hands.getChild("hand4");
        this.hand4_r1 = this.hand4.getChild("hand4_r1");
        this.face = this.total.getChild("face");
        this.cube_r3 = this.face.getChild("cube_r3");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 53).addBox(-4.5F, -17.0F, -3.0F, 9.0F, 5.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(30, 52).addBox(-4.5F, -6.25F, -3.0F, 9.0F, 6.0F, 6.0F, new CubeDeformation(0.4F))
                        .texOffs(0, 41).addBox(-4.5F, -12.25F, -3.0F, 9.0F, 6.0F, 6.0F, new CubeDeformation(0.2F)),
                PartPose.ZERO);

        PartDefinition head = total.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 57).addBox(-1.0F, -2.25F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -17.0F, 0.0F));

        head.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(48, 35).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-1.5F, -0.75F, -1.5F, 0.0F, 0.0F, -0.1309F));

        head.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(32, 43).addBox(-4.0F, -1.0F, -2.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(-0.75F, -0.5F, -1.5F, 0.0F, 0.0F, -0.1309F));

        PartDefinition hands = total.addOrReplaceChild("hands",
                CubeListBuilder.create(),
                PartPose.ZERO);

        hands.addOrReplaceChild("hand1",
                CubeListBuilder.create()
                        .texOffs(56, 56).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-4.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

        hands.addOrReplaceChild("hand2",
                CubeListBuilder.create()
                        .texOffs(56, 54).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 0.0F, 0.48F, -0.1745F));

        hands.addOrReplaceChild("hand3",
                CubeListBuilder.create()
                        .texOffs(56, 52).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, 0.0F, -2.6616F, 0.6981F));

        PartDefinition hand4 = hands.addOrReplaceChild("hand4",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(3.0F, -14.0F, 2.0F, 0.0F, 2.7053F, 0.6981F));

        hand4.addOrReplaceChild("hand4_r1",
                CubeListBuilder.create()
                        .texOffs(28, 56).addBox(0.0F, -14.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-3.0F, 14.0F, -2.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition face = total.addOrReplaceChild("face",
                CubeListBuilder.create()
                        .texOffs(31, 46).addBox(-3.5F, 2.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F))
                        .texOffs(32, 53).addBox(-2.5F, 3.0F, -3.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                        .texOffs(32, 53).addBox(1.5F, 3.0F, -3.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                        .texOffs(24, 43).addBox(0.5F, 2.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offset(0.0F, -18.0F, 0.5F));

        face.addOrReplaceChild("cube_r3",
                CubeListBuilder.create()
                        .texOffs(0, 54).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
                        .texOffs(32, 53).addBox(-4.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offsetAndRotation(2.0F, 4.5F, -3.5F, -0.0436F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        if (state.isAlive  &&   state.isImitating  &&  state.imitaterChance  > 0) {
            this.total.yRot = state.ageInTicks / 3.0F;
        }
    }




    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}