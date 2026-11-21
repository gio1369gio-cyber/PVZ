package com.lying.pvz.client.model.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;


// SculkShroomModel —— 幽匿菇（毒系）
// 形状：两层菌柄底座（total，上段略膨胀 0.1），
//       上方一圈伞盖（head，四层叠块，带内收/外扩），正面两只眼（face）；
//       伞盖周围八根放射状触须（shoot 下 shoot1~shoot8，其中 shoot2/3/6/7 带一层 cube_rX 弯钩）
public class SculkShroomModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sculk_shroom"), "main");

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart face;
    private final ModelPart shoot;
    private final ModelPart shoot1;
    private final ModelPart shoot2;
    private final ModelPart cube_r1;
    private final ModelPart shoot3;
    private final ModelPart cube_r2;
    private final ModelPart shoot4;
    private final ModelPart shoot5;
    private final ModelPart shoot6;
    private final ModelPart cube_r3;
    private final ModelPart shoot7;
    private final ModelPart cube_r4;
    private final ModelPart shoot8;
    private final ModelPart getPlantWholeBody;

    public SculkShroomModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.head = this.total.getChild("head");
        this.face = this.head.getChild("face");
        this.shoot = this.head.getChild("shoot");
        this.shoot1 = this.shoot.getChild("shoot1");
        this.shoot2 = this.shoot.getChild("shoot2");
        this.cube_r1 = this.shoot2.getChild("cube_r1");
        this.shoot3 = this.shoot.getChild("shoot3");
        this.cube_r2 = this.shoot3.getChild("cube_r2");
        this.shoot4 = this.shoot.getChild("shoot4");
        this.shoot5 = this.shoot.getChild("shoot5");
        this.shoot6 = this.shoot.getChild("shoot6");
        this.cube_r3 = this.shoot6.getChild("cube_r3");
        this.shoot7 = this.shoot.getChild("shoot7");
        this.cube_r4 = this.shoot7.getChild("cube_r4");
        this.shoot8 = this.shoot.getChild("shoot8");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(46, 28).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(42, 48).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.1F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = total.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 40).addBox(-6.5F, -12.0F, -6.5F, 13.0F, 7.0F, 13.0F, new CubeDeformation(-0.3F))
                        .texOffs(45, 8).addBox(-6.5F, -11.0F, -6.5F, 13.0F, 6.0F, 13.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-5.5F, -11.0F, -7.5F, 11.0F, 6.0F, 15.0F, new CubeDeformation(-0.2F))
                        .texOffs(0, 21).addBox(-7.5F, -11.0F, -5.5F, 15.0F, 6.0F, 11.0F, new CubeDeformation(-0.2F)),
                PartPose.ZERO);

        head.addOrReplaceChild("face",
                CubeListBuilder.create()
                        .texOffs(0, 4).addBox(-5.0F, -12.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(2.0F, -12.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        PartDefinition shoot = head.addOrReplaceChild("shoot",
                CubeListBuilder.create(),
                PartPose.ZERO);

        shoot.addOrReplaceChild("shoot1",
                CubeListBuilder.create()
                        .texOffs(50, 28).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
                        .texOffs(41, 45).addBox(-1.5F, -1.5F, -3.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -8.5F, -7.25F, -0.4363F, 0.0F, 0.0F));

        PartDefinition shoot2 = shoot.addOrReplaceChild("shoot2",
                CubeListBuilder.create()
                        .texOffs(0, 8).addBox(-3.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-6.25F, -8.5F, -6.25F, 0.0F, -0.7854F, 0.0F));

        shoot2.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(5, 47).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.25F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition shoot3 = shoot.addOrReplaceChild("shoot3",
                CubeListBuilder.create()
                        .texOffs(0, 40).addBox(-3.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-7.25F, -8.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

        shoot3.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(0, 46).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.25F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

        shoot.addOrReplaceChild("shoot4",
                CubeListBuilder.create()
                        .texOffs(56, 4).addBox(-0.5F, -0.5F, -1.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
                        .texOffs(43, 0).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-6.25F, -8.5F, 6.25F, 0.0F, -0.7854F, 0.0F));

        shoot.addOrReplaceChild("shoot5",
                CubeListBuilder.create()
                        .texOffs(56, 0).addBox(-0.5F, -0.5F, -1.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
                        .texOffs(43, 5).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -8.5F, 7.25F, 0.4363F, 0.0F, 0.0F));

        PartDefinition shoot6 = shoot.addOrReplaceChild("shoot6",
                CubeListBuilder.create()
                        .texOffs(0, 27).addBox(1.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(6.25F, -8.5F, 6.25F, 0.0F, -0.7854F, 0.0F));

        shoot6.addOrReplaceChild("cube_r3",
                CubeListBuilder.create()
                        .texOffs(45, 27).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.75F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition shoot7 = shoot.addOrReplaceChild("shoot7",
                CubeListBuilder.create()
                        .texOffs(0, 21).addBox(1.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(7.25F, -8.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

        shoot7.addOrReplaceChild("cube_r4",
                CubeListBuilder.create()
                        .texOffs(7, 11).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.75F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

        shoot.addOrReplaceChild("shoot8",
                CubeListBuilder.create()
                        .texOffs(51, 2).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
                        .texOffs(43, 10).addBox(-1.5F, -1.5F, -3.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(6.25F, -8.5F, -6.25F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }



    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }

}