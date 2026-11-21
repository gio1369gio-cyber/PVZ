package com.lying.pvz.client.model.entity.plant.arma;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import net.minecraft.util.Mth;

// GoldenMelonPultModel —— 黄金瓜投掷器
// 形状：底座一个大方块主体（total），四角向外各伸一片扁平叶片（nw_r1/sw_r1/se_r1/ne_r1），
//       主体上方伸出炮臂（pult），炮臂末端骨（bone）上托一颗金瓜（melon）
public class GoldenMelonPultModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("golden_melon_pult"), "main");

    private final ModelPart total;
    private final ModelPart nw_r1;
    private final ModelPart sw_r1;
    private final ModelPart se_r1;
    private final ModelPart ne_r1;
    private final ModelPart pult;
    private final ModelPart bone;
    private final ModelPart melon;
    private final ModelPart getPlantWholeBody;

    public GoldenMelonPultModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.nw_r1 = this.total.getChild("nw_r1");
        this.sw_r1 = this.total.getChild("sw_r1");
        this.se_r1 = this.total.getChild("se_r1");
        this.ne_r1 = this.total.getChild("ne_r1");
        this.pult = this.total.getChild("pult");
        this.bone = this.pult.getChild("bone");
        this.melon = this.bone.getChild("melon");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-7.0F, -10.5F, -5.0F, 14.0F, 10.0F, 10.0F, CubeDeformation.NONE)
                        .texOffs(32, 44).addBox(-7.0F, -8.0F, -5.2F, 14.0F, 2.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("nw_r1",
                CubeListBuilder.create()
                        .texOffs(29, 0).addBox(-1.0F, 0.0F, -8.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.0873F));

        total.addOrReplaceChild("sw_r1",
                CubeListBuilder.create()
                        .texOffs(13, 47).addBox(-1.0F, 0.0F, -1.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, -0.0873F));

        total.addOrReplaceChild("se_r1",
                CubeListBuilder.create()
                        .texOffs(-9, 47).addBox(-10.0F, 0.0F, -1.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0873F));

        total.addOrReplaceChild("ne_r1",
                CubeListBuilder.create()
                        .texOffs(39, 18).addBox(-10.0F, 0.0F, -8.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0873F));

        PartDefinition pult = total.addOrReplaceChild("pult",
                CubeListBuilder.create()
                        .texOffs(38, 51).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 11.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bone = pult.addOrReplaceChild("bone",
                CubeListBuilder.create()
                        .texOffs(48, 9).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(0, 20).addBox(-5.0F, -2.0F, 3.0F, 10.0F, 5.0F, 10.0F, CubeDeformation.NONE)
                        .texOffs(0, 44).addBox(-4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, -0.2182F, 0.0F, 0.0F));

        bone.addOrReplaceChild("melon",
                CubeListBuilder.create()
                        .texOffs(32, 27).addBox(-4.0F, -3.5F, -3.0F, 8.0F, 9.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -5.5F, 8.0F, -0.6545F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        if (state.attackTime > 0) {
            float percent = 1.0F - (float) state.attackTime * 1.0F / (float) state.pultAnimTime;
            this.pult.xRot = (1.0F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
            this.melon.visible = (double) percent < 0.5;
        } else {
            this.pult.xRot = Mth.sin(state.ageInTicks / 10.0F) / 8.0F;
            this.melon.visible = true;
        }
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }


}