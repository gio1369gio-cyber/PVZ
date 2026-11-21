package com.lying.pvz.client.model.entity.plant.light;


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
//     body（setPos(0,24,0)，1 方块：菇柄）
//       └─ hat（setPos(0,-14,0)，4 方块：4 层伞盖）
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class SunShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sun_shroom"), "main");

	private final ModelPart body;

	public SunShroomModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		body.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(2, 97).addBox(-11.0F, -7.0F, -11.0F, 22.0F, 7.0F, 22.0F, CubeDeformation.NONE)
						.texOffs(1, 32).addBox(-10.0F, -12.0F, -10.0F, 20.0F, 5.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(62, 4).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(77, 27).addBox(-6.0F, -16.0F, -6.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -14.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.body;
	}
}