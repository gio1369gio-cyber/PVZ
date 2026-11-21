package com.lying.pvz.client.model.entity.plant.enforce;


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
//   几何 1:1 搬运，未改任何数值。
//   本模型结构极简：body 承载 3 层堆叠方块（下宽上窄的南瓜轮廓），
//   bone 是顶端一个带 Z 轴旋转的小柄（茎）。
//   setupAnim 在 1.16.5 就是空实现 —— 说明动画由实体在别处驱动（或压根没有），
//   迁移后保持空实现。

public class SquashModel extends PvZPlantModel {


	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("squash"), "main");

	private final ModelPart body;

	public SquashModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(125, 202).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 16.0F, 32.0F, CubeDeformation.NONE)
						.texOffs(138, 153).addBox(-14.0F, -32.0F, -14.0F, 28.0F, 16.0F, 28.0F, CubeDeformation.NONE)
						.texOffs(40, 148).addBox(-11.0F, -44.0F, -11.0F, 22.0F, 12.0F, 22.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		body.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(235, 130).addBox(1.0F, -1.8038F, -2.0F, 4.0F, 9.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -50.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.body;
	}
}