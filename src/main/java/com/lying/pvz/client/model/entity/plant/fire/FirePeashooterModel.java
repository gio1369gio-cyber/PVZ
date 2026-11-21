package com.lying.pvz.client.model.entity.plant.fire;


import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// FirePeashooterModel —— 火焰豌豆射手
// 形状：细茎（body，xRot=-0.0873 前倾）顶端一个大方头（head，xRot=+0.0873 回正），
//       头前方两层炮口凸起，头后一簇后飘的叶片（hair_r1，xRot=-0.2618）；
//       底部向下四片呈十字展开的叶片（down 下的 n_r1/w_r1/e_r1/s_r1）
public class FirePeashooterModel extends PlantShooterModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("fire_peashooter"), "main");

    private final ModelPart total;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart hair_r1;
    private final ModelPart down;
    private final ModelPart n_r1;
    private final ModelPart w_r1;
    private final ModelPart e_r1;
    private final ModelPart s_r1;

    public FirePeashooterModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.body = this.total.getChild("body");
        this.head = this.body.getChild("head");
        this.hair_r1 = this.head.getChild("hair_r1");
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
                CubeListBuilder.create()
                        .texOffs(0, 31).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(30, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(44, 0).addBox(-3.0F, -6.0F, -9.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE)
                        .texOffs(40, 8).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        head.addOrReplaceChild("hair_r1",
                CubeListBuilder.create()
                        .texOffs(28, 30).addBox(-5.0F, -3.0F, -7.0F, 10.0F, 7.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -10.0F, 5.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition down = total.addOrReplaceChild("down",
                CubeListBuilder.create(),
                PartPose.ZERO);

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
    public ModelPart getPlantWholeBody() {
        return this.total;
    }

}