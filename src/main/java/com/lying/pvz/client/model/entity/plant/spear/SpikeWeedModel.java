package com.lying.pvz.client.model.entity.plant.spear;


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
//   几何 1:1 搬运。结构极简：
//     total（2 个方块：地面 + 地面外皮 0.3 膨胀）
//       └─ bone（4 个 0 厚度薄片，组成"十字"尖刺）
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class SpikeWeedModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("spike_weed"), "main");

	private final ModelPart total;

	public SpikeWeedModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 19).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -3.2F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.3F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(32, 38).addBox(-8.0F, -6.0F, -4.0F, 16.0F, 4.0F, 0.0F, CubeDeformation.NONE)
						.texOffs(32, 42).addBox(-8.0F, -6.0F, 4.0F, 16.0F, 4.0F, 0.0F, CubeDeformation.NONE)
						.texOffs(0, 22).addBox(-4.0F, -6.0F, -8.0F, 0.0F, 4.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 26).addBox(4.0F, -6.0F, -8.0F, 0.0F, 4.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}