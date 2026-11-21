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
//   几何 1:1 搬运。小喷菇模型：
//     total（根，6 个方块，无旋转）
//       ├─ 4 层堆叠方块（8×8×8 → 12×4×12 → 10×3×10 → 8×1×8，自下而上）
//       └─ 2 个嘴部方块（2×2×1 + 4×4×2，向 -Z 伸出）
//   setupAnim：空实现，原代码即如此。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class PuffShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("puff_shroom"), "main");

	private final ModelPart total;

	public PuffShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(31, 47).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 4.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 17).addBox(-5.0F, -15.0F, -5.0F, 10.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 31).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(1, 42).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(9, 41).addBox(-2.0F, -5.0F, -7.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}