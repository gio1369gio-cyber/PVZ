package com.lying.pvz.client.model.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// OriginShroomModel —— 原生菇（毒系）
// 形状：一个双层大蘑菇（total，下段短柄 + 上段大伞盖 + 柄上下各一圈薄环 + 伞盖前方一个小凸起 + 一个平口），
//       没有子节点，全部 cube 直接挂在 total 上
public class OriginShroomModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("origin_shroom"), "main");

    private final ModelPart total;
    private final ModelPart getPlantWholeBody;

    public OriginShroomModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(1, 105).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 10.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(51, 101).addBox(-8.0F, -21.0F, -8.0F, 16.0F, 10.0F, 16.0F, CubeDeformation.NONE)
                        .texOffs(1, 88).addBox(-7.0F, -11.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
                        .texOffs(1, 70).addBox(-7.0F, -22.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
                        .texOffs(4, 63).addBox(-1.0F, -17.0F, -10.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(16, 60).addBox(-2.0F, -18.0F, -13.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }


}