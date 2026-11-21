package com.lying.pvz.client.model.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.Optional;

// GooPeaShooterModel —— 黏液豌豆射手（毒系射手）
// 形状：细茎（stick_r1）顶端大方头（head，前方两层炮口凸起），
//       头后一簇前倾的叶片（leafl_r1，两片叠在一起）；
//       底部向下四片呈十字展开的叶片（down 下的 n_r1/w_r1/e_r1/s_r1）
public class GooPeaShooterModel extends PlantShooterModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("goo_pea_shooter"), "main");

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
    private final ModelPart getPlantWholeBody;

    public GooPeaShooterModel(ModelPart root) {
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
                PartPose.ZERO);

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

        PartDefinition hair = head.addOrReplaceChild("hair",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -16.5F, 12.0F));

        hair.addOrReplaceChild("leafl_r1",
                CubeListBuilder.create()
                        .texOffs(41, 18).addBox(-1.0F, -0.1F, -4.5F, 4.0F, 6.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(0.0F, -0.5F, -6.0F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

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