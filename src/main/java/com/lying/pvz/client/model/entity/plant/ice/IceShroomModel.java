package com.lying.pvz.client.model.entity.plant.ice;


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
//   几何 1:1 搬运。结构：
//     total（3 个方块：菇柄 + 菇柄外壳 + 底座）
//       └─ hat（4 个方块：三层伞盖 + 顶盖）
//   两处 0.4 膨胀是"外皮比内核大 0.4 格"的层次感来源，逐条映射为 new CubeDeformation(0.4F)。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class IceShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ice_shroom"), "main");

	private final ModelPart total;

	public IceShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(72, 64).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 9.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(72, 87).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 6.0F, 14.0F, new CubeDeformation(0.4F))
						.texOffs(0, 105).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(0, 51).addBox(-9.0F, -15.0F, -9.0F, 18.0F, 6.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(0, 25).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-9.0F, -16.0F, -9.0F, 18.0F, 7.0F, 18.0F, new CubeDeformation(0.4F))
						.texOffs(60, 107).addBox(-8.5F, -9.0F, -8.5F, 17.0F, 4.0F, 17.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}