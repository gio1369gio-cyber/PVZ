package com.lying.pvz.client.model.entity.plant.enforce;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// RotateRadishModel —— 旋转萝卜
// 形状：一个萝卜主体（total，圆方身 + 底环 + 底部十字形两张零厚度叶片），
//       头顶左右两条细长叶片（bone2 向左、bone 向右，各自带一片 12 长零厚度长叶）
public class RotateRadishModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("rotate_radish"), "main");

    private final ModelPart total;
    private final ModelPart bone2;
    private final ModelPart bone;

    public RotateRadishModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.bone2 = this.total.getChild("bone2");
        this.bone = this.total.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, CubeDeformation.NONE)
                        .texOffs(0, 21).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(4, 4).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("bone2",
                CubeListBuilder.create()
                        .texOffs(0, 3).addBox(0.0F, -4.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
                        .texOffs(0, 15).addBox(-12.0F, -4.0F, -3.0F, 12.0F, 0.0F, 6.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        total.addOrReplaceChild("bone",
                CubeListBuilder.create()
                        .texOffs(18, 0).addBox(0.0F, -4.0F, -3.0F, 12.0F, 0.0F, 6.0F, new CubeDeformation(0.2F))
                        .texOffs(4, 0).addBox(0.0F, -4.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        // 转转萝卜持续旋转；state.entityType 恒为 ROTATE_RADISH（模型专用），
        // 原 isEntityValid(EntityType) 传参错误，直接以 ageInTicks 驱动旋转即可。
        this.total.yRot = state.ageInTicks / 5.0F;
    }



    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}