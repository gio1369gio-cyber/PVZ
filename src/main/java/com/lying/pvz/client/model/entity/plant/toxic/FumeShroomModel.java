package com.lying.pvz.client.model.entity.plant.toxic;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。大喷菇模型：
//     total（根，6 个方块，无旋转）
//       ├─ 3 个堆叠方块（菌盖 12×10×12、16×10×16、14×1×14）
//       ├─ 1 个横向片（14×1×14，位于 -22 高度）
//       └─ 2 个方块（嘴部 2×2×2 + 4×4×3，向 -Z 伸出）
//   setupAnim：空实现，原代码即如此。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class FumeShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("fume_shroom"), "main");

	private final ModelPart total;

	public FumeShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
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