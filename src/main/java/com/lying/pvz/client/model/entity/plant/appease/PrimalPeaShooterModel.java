package com.lying.pvz.client.model.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.Optional;

// PrimalPeaShooterModel —— 原始豌豆射手植物
// 形状：细长茎杆（stick_r1）顶端大方头（head），头前方有一层朝上的炮口（head_r1），
//       头后上方一簇叶片（leafl_r1，下挂两片小侧叶 leafl_r1_r1 / leafl_r1_r2）；
//       底部向下四片呈十字展开的叶片（down 下的 n_r1/w_r1/e_r1/s_r1）
public class PrimalPeaShooterModel extends PlantShooterModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("primal_pea_shooter"), "main");

    private final ModelPart total;
    private final ModelPart body;
    private final ModelPart stick_r1;
    private final ModelPart head;
    private final ModelPart head_r1;
    private final ModelPart hair;
    private final ModelPart leafl_r1;
    private final ModelPart leafl_r1_r1;
    private final ModelPart leafl_r1_r2;
    private final ModelPart down;
    private final ModelPart n_r1;
    private final ModelPart w_r1;
    private final ModelPart e_r1;
    private final ModelPart s_r1;
    private final ModelPart getPlantWholeBody;

    public PrimalPeaShooterModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.body = this.total.getChild("body");
        this.stick_r1 = this.body.getChild("stick_r1");
        this.head = this.body.getChild("head");
        this.head_r1 = this.head.getChild("head_r1");
        this.hair = this.head.getChild("hair");
        this.leafl_r1 = this.hair.getChild("leafl_r1");
        this.leafl_r1_r1 = this.leafl_r1.getChild("leafl_r1_r1");
        this.leafl_r1_r2 = this.leafl_r1.getChild("leafl_r1_r2");
        this.down = this.total.getChild("down");
        this.n_r1 = this.down.getChild("n_r1");
        this.w_r1 = this.down.getChild("w_r1");
        this.e_r1 = this.down.getChild("e_r1");
        this.s_r1 = this.down.getChild("s_r1");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = total.addOrReplaceChild("body",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("stick_r1",
                CubeListBuilder.create()
                        .texOffs(0, 31).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(30, 0).addBox(-1.0F, -16.0F, -7.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(44, 0).addBox(-2.0F, -17.0F, -8.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-4.0F, -21.0F, -4.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE)
                        .texOffs(40, 8).addBox(-1.0F, -11.0F, -1.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(-1.0F, -1.0F, 0.0F));

        head.addOrReplaceChild("head_r1",
                CubeListBuilder.create()
                        .texOffs(18, 15).addBox(-5.0F, 2.2F, 10.0F, 10.0F, 2.8F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.0F, -15.0F, -14.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -16.5F, 12.0F));

        PartDefinition leafl_r1 = hair.addOrReplaceChild("leafl_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(0.0F, -0.5F, -6.0F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        leafl_r1.addOrReplaceChild("leafl_r1_r1",
                CubeListBuilder.create()
                        .texOffs(42, 19).addBox(-5.0F, 0.0F, -0.5F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.6292F, 2.9F, -3.5081F, 0.0F, -0.0436F, 1.6144F));

        leafl_r1.addOrReplaceChild("leafl_r1_r2",
                CubeListBuilder.create()
                        .texOffs(42, 19).addBox(1.0F, 1.0F, -0.5F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.0F, 2.9F, -3.5F, 0.0F, 0.0F, -1.6144F));

        PartDefinition down = total.addOrReplaceChild("down",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        down.addOrReplaceChild("n_r1",
                CubeListBuilder.create()
                        .texOffs(4, 20).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.0F));

        down.addOrReplaceChild("w_r1",
                CubeListBuilder.create()
                        .texOffs(6, 27).addBox(1.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, -0.1745F));

        down.addOrReplaceChild("e_r1",
                CubeListBuilder.create()
                        .texOffs(26, 27).addBox(-7.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.1745F));

        down.addOrReplaceChild("s_r1",
                CubeListBuilder.create()
                        .texOffs(24, 20).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public Optional<ModelPart> getHeadModel() {
        return Optional.ofNullable(this.head);
    }

    @Override
    public Optional<ModelPart> getBodyModel() {
        return Optional.ofNullable(this.body);
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}