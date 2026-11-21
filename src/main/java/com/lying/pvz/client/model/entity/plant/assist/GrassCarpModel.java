package com.lying.pvz.client.model.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// GrassCarpModel —— 草鱼
// 形状：一个圆滚滚的鱼身（total，方块 + 背部一小条 + 腹部一小条），
//       尾部两条向上/下斜的尾鳍（tails_r1 / tailn_r1），尾端一个方块状尾巴（tail），
//       背上一片小立叶（leaf），两侧一对会摆动的小鳍（right/left，各自带一层 right_r1/left_r1）
public class GrassCarpModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("grass_carp"), "main");

    private final ModelPart total;
    private final ModelPart tails_r1;
    private final ModelPart tailn_r1;
    private final ModelPart leaf;
    private final ModelPart tail;
    private final ModelPart right;
    private final ModelPart right_r1;
    private final ModelPart left;
    private final ModelPart left_r1;

    public GrassCarpModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.tails_r1 = this.total.getChild("tails_r1");
        this.tailn_r1 = this.total.getChild("tailn_r1");
        this.leaf = this.total.getChild("leaf");
        this.tail = this.total.getChild("tail");
        this.right = this.total.getChild("right");
        this.right_r1 = this.right.getChild("right_r1");
        this.left = this.total.getChild("left");
        this.left_r1 = this.left.getChild("left_r1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(10, 12).addBox(-1.0F, -8.0F, -3.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(1, 13).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("tails_r1",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        total.addOrReplaceChild("tailn_r1",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        total.addOrReplaceChild("leaf",
                CubeListBuilder.create()
                        .texOffs(0, 12).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -6.0F, 3.0F));

        total.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-1.5F, -1.5F, -0.7F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(24, 4).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 2.5F, 5.0F));

        PartDefinition right = total.addOrReplaceChild("right",
                CubeListBuilder.create(),
                PartPose.offset(-3.0F, -3.0F, 0.0F));
        right.addOrReplaceChild("right_r1",
                CubeListBuilder.create()
                        .texOffs(18, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition left = total.addOrReplaceChild("left",
                CubeListBuilder.create(),
                PartPose.offset(3.0F, -3.0F, 0.0F));
        left.addOrReplaceChild("left_r1",
                CubeListBuilder.create()
                        .texOffs(18, 3).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(mesh, 32, 32);
    }


    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        this.right.zRot = -0.2F + 0.4F * Mth.sin(state.ageInTicks * 0.2F);
        this.left.zRot = 0.2F - 0.4F * Mth.sin(state.ageInTicks * 0.2F);
    }



    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}