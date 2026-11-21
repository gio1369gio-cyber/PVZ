package com.lying.pvz.client.model.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.Optional;

// BeeShooterModel —— 蜜蜂射手植物
// 形状：细长茎杆（stick_r1）顶端一个大方头（head），头前方有两层小凸起（嘴/眼），
//       头后下方一片下垂小叶（leafl_r1）；
//       底部向下四片呈十字展开的叶片（down 下的 n_r1/w_r1/e_r1/s_r1）
public class BeeShooterModel extends PlantShooterModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bee_shooter"), "main");

    private final ModelPart total;
    private final ModelPart body;
    private final ModelPart stick_r1;
    private final ModelPart head;
    private final ModelPart hair;
    private final ModelPart leafl_r1;
    private final ModelPart down;
    private final ModelPart n_r1;
    private final ModelPart w_r1;
    private final ModelPart e_r1;
    private final ModelPart s_r1;

    public BeeShooterModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.body = this.total.getChild("body");
        this.stick_r1 = this.body.getChild("stick_r1");
        this.head = this.body.getChild("head");
        this.hair = this.head.getChild("hair");
        this.leafl_r1 = this.hair.getChild("leafl_r1");
        this.down = this.total.getChild("down");
        this.n_r1 = this.down.getChild("n_r1");
        this.w_r1 = this.down.getChild("w_r1");
        this.e_r1 = this.down.getChild("e_r1");
        this.s_r1 = this.down.getChild("s_r1");
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
                        .texOffs(2, 33).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(32, 2).addBox(-1.0F, -16.0F, -7.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(46, 2).addBox(-2.0F, -17.0F, -8.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-4.0F, -21.0F, -4.0F, 10.0F, 10.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(42, 10).addBox(-1.0F, -11.0F, -1.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(-1.0F, -1.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -16.5F, 12.0F));

        hair.addOrReplaceChild("leafl_r1",
                CubeListBuilder.create()
                        .texOffs(10, 36).addBox(-1.0F, -1.5F, -5.0F, 3.0F, 3.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0872F, 0.0F, 0.0F));

        PartDefinition down = total.addOrReplaceChild("down",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        down.addOrReplaceChild("n_r1",
                CubeListBuilder.create()
                        .texOffs(6, 22).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.0F));

        down.addOrReplaceChild("w_r1",
                CubeListBuilder.create()
                        .texOffs(8, 29).addBox(1.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, -0.1745F));

        down.addOrReplaceChild("e_r1",
                CubeListBuilder.create()
                        .texOffs(28, 29).addBox(-7.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.1745F));

        down.addOrReplaceChild("s_r1",
                CubeListBuilder.create()
                        .texOffs(26, 22).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public Optional<ModelPart> getHeadModel() {
        return Optional.ofNullable(this.head);
    }

    public Optional<ModelPart> getBodyModel() {
        return Optional.ofNullable(this.body);
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}