package com.lying.pvz.client.model.entity.plant.electric;


import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.Optional;

// MagnifyingGrassModel —— 棱镜草
// 形状：整体绕 Y 轴旋转 90°（total 初始 yRot=1.5708），
//       中心一个带镜片的高头（head，前壳 + 长条背鳍 + 中段主体），head 前面一片无厚度镜面（eye）；
//       四周四组放射状叶片（leaf1~leaf4，每组 3 张零厚度平面 cube_rX 斜向展开）
public class MagnifyingGrassModel extends PlantShooterModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("magnifying_grass"), "main");

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart eye;
    private final ModelPart leaf1;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart cube_r3;
    private final ModelPart leaf2;
    private final ModelPart cube_r4;
    private final ModelPart cube_r5;
    private final ModelPart cube_r6;
    private final ModelPart leaf3;
    private final ModelPart cube_r7;
    private final ModelPart cube_r8;
    private final ModelPart cube_r9;
    private final ModelPart leaf4;
    private final ModelPart cube_r10;
    private final ModelPart cube_r11;
    private final ModelPart cube_r12;

    public MagnifyingGrassModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.head = this.total.getChild("head");
        this.eye = this.head.getChild("eye");
        this.leaf1 = this.total.getChild("leaf1");
        this.cube_r1 = this.leaf1.getChild("cube_r1");
        this.cube_r2 = this.leaf1.getChild("cube_r2");
        this.cube_r3 = this.leaf1.getChild("cube_r3");
        this.leaf2 = this.total.getChild("leaf2");
        this.cube_r4 = this.leaf2.getChild("cube_r4");
        this.cube_r5 = this.leaf2.getChild("cube_r5");
        this.cube_r6 = this.leaf2.getChild("cube_r6");
        this.leaf3 = this.total.getChild("leaf3");
        this.cube_r7 = this.leaf3.getChild("cube_r7");
        this.cube_r8 = this.leaf3.getChild("cube_r8");
        this.cube_r9 = this.leaf3.getChild("cube_r9");
        this.leaf4 = this.total.getChild("leaf4");
        this.cube_r10 = this.leaf4.getChild("cube_r10");
        this.cube_r11 = this.leaf4.getChild("cube_r11");
        this.cube_r12 = this.leaf4.getChild("cube_r12");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 19.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = total.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5F, -8.0F, -5.5F, 5.0F, 8.0F, 11.0F, new CubeDeformation(0.01F))
                        .texOffs(24, 19).addBox(-1.0F, -13.0F, -2.0F, 2.0F, 18.0F, 4.0F, new CubeDeformation(0.01F))
                        .texOffs(0, 19).addBox(-2.0F, -11.0F, -4.0F, 4.0F, 14.0F, 8.0F, new CubeDeformation(0.01F)),
                PartPose.ZERO);

        head.addOrReplaceChild("eye",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(2.7F, -6.0F, -4.5F, 0.0F, 4.0F, 9.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        // 四组叶片结构一致（3 张零厚度平面），逐个硬编码保持与源码字段一一对应
        PartDefinition leaf1 = total.addOrReplaceChild("leaf1", CubeListBuilder.create(), PartPose.ZERO);
        leaf1.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(26, 6).addBox(-3.0F, 2.55F, 3.05F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.8727F, -0.3054F, 0.0F));
        leaf1.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(0, 19).addBox(-3.0F, -8.3F, 7.8F, 4.0F, 3.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.3054F, 0.0F));
        leaf1.addOrReplaceChild("cube_r3",
                CubeListBuilder.create()
                        .texOffs(32, 20).addBox(-3.0F, 0.0F, 0.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.1745F, -0.3054F, 0.0F));

        PartDefinition leaf2 = total.addOrReplaceChild("leaf2", CubeListBuilder.create(), PartPose.ZERO);
        leaf2.addOrReplaceChild("cube_r4",
                CubeListBuilder.create()
                        .texOffs(23, 0).addBox(-1.0F, 2.55F, 3.05F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.8727F, 0.3054F, 0.0F));
        leaf2.addOrReplaceChild("cube_r5",
                CubeListBuilder.create()
                        .texOffs(0, 6).addBox(-1.0F, -8.3F, 7.8F, 4.0F, 3.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.3054F, 0.0F));
        leaf2.addOrReplaceChild("cube_r6",
                CubeListBuilder.create()
                        .texOffs(28, 16).addBox(-1.0F, 0.0F, 0.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.1745F, 0.3054F, 0.0F));

        PartDefinition leaf3 = total.addOrReplaceChild("leaf3", CubeListBuilder.create(), PartPose.ZERO);
        leaf3.addOrReplaceChild("cube_r7",
                CubeListBuilder.create()
                        .texOffs(10, 19).addBox(-3.0F, 2.55F, 3.05F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.8727F, 2.8362F, 0.0F));
        leaf3.addOrReplaceChild("cube_r8",
                CubeListBuilder.create()
                        .texOffs(0, 3).addBox(-3.0F, -8.3F, 7.8F, 4.0F, 3.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 2.8362F, 0.0F));
        leaf3.addOrReplaceChild("cube_r9",
                CubeListBuilder.create()
                        .texOffs(28, 12).addBox(-3.0F, 0.0F, 0.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.1745F, 2.8362F, 0.0F));

        PartDefinition leaf4 = total.addOrReplaceChild("leaf4", CubeListBuilder.create(), PartPose.ZERO);
        leaf4.addOrReplaceChild("cube_r10",
                CubeListBuilder.create()
                        .texOffs(15, 0).addBox(-1.0F, 2.55F, 3.05F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.8727F, -2.8362F, 0.0F));
        leaf4.addOrReplaceChild("cube_r11",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0F, -8.3F, 7.8F, 4.0F, 3.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -2.8362F, 0.0F));
        leaf4.addOrReplaceChild("cube_r12",
                CubeListBuilder.create()
                        .texOffs(17, 6).addBox(-1.0F, 0.0F, 0.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.1745F, -2.8362F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public Optional<ModelPart> getHeadModel() {
        return Optional.ofNullable(this.head);
    }

    public Optional<ModelPart> getBodyModel() {
        return this.getHeadModel();
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}