package com.lying.pvz.client.model.entity.plant.arma;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// ChorusFruitPultModel —— 紫颂果投掷器
// 形状：底部一颗大紫颂果（chorusfruit，左右各一个耳状凸起 + 顶部一圈装饰盖 dicoration），
//       果体上方伸出炮臂（pult），炮臂末端发射口（out）内含一颗方形弹丸（bullet）
public class ChorusFruitPultModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("chorus_fruit_pult"), "main");

    private final ModelPart total;
    private final ModelPart chorusfruit;
    private final ModelPart dicoration;
    private final ModelPart pult;
    private final ModelPart out;
    private final ModelPart bullet;

    public ChorusFruitPultModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.chorusfruit = this.total.getChild("chorusfruit");
        this.dicoration = this.chorusfruit.getChild("dicoration");
        this.pult = this.total.getChild("pult");
        this.out = this.pult.getChild("out");
        this.bullet = this.out.getChild("bullet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition chorusfruit = total.addOrReplaceChild("chorusfruit",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.5F, -8.0F, -4.5F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
                        .texOffs(32, 24).addBox(-5.5F, -4.0F, -4.5F, 2.0F, 4.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(32, 24).addBox(4.5F, -4.0F, -4.5F, 2.0F, 4.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(0, 38).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(0, 28).addBox(-3.5F, -9.0F, -4.5F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        chorusfruit.addOrReplaceChild("dicoration",
                CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-4.0F, -8.0F, -5.0F, 9.0F, 1.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pult = total.addOrReplaceChild("pult",
                CubeListBuilder.create()
                        .texOffs(33, 34).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition out = pult.addOrReplaceChild("out",
                CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(29, 13).addBox(-3.0F, -11.0F, 0.0F, 6.0F, 6.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, -1.309F, 0.0F, 0.0F));

        out.addOrReplaceChild("bullet",
                CubeListBuilder.create()
                        .texOffs(33, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -9.0F, 1.0F, 1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        if (state.attackTime > 0) {
            float percent = 1 - state.attackTime * 1.0f / state.pultAnimTime;
            this.pult.xRot = (1.0f - Mth.abs(Mth.cos(percent * 3.14159f))) * 1.5f;
            this.bullet.visible = (percent < 0.5);
        } else {
            this.pult.xRot = Mth.sin(state.ageInTicks / 10) / 8;
            this.bullet.visible = true;
        }
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}