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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required permissions
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。灯笼类模型，结构：
//     total（纯容器）
//       ├─ head  （10 个方块：灯芯 + 4 面侧板 + 2 层灯罩 + 2 层内芯 + 底座 + 灯芯小柱）
//       ├─ bottom（3 个方块：底部十字 + 中心小柱）
//       └─ stick1..stick6（6 个完全相同的小柱，几何全等，tx/ty 都是 (106,0)，膨胀 -0.01）
//
//   ⚠️ stick1~stick6 的 addBox 参数、texOffs、膨胀完全相同（都是 4×9×4 的小木棍），
//      是 1.16.5 用于"灯笼周围一圈栅栏"的重复件。逐个展开保留，不合并——方便日后对照 diff。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class PlanternModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("plantern"), "main");

	private final ModelPart total;

	public PlanternModel(ModelPart root, Function<Identifier, RenderType> renderType) {
		super(root,renderType);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -33.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 47).addBox(-6.0F, -29.0F, 0.0F, 12.0F, 3.0F, 0.0F, CubeDeformation.NONE)
						.texOffs(42, 32).addBox(0.0F, -29.0F, -6.0F, 0.0F, 3.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 23).addBox(-9.0F, -26.0F, -9.0F, 18.0F, 3.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-9.5F, -23.0F, -9.5F, 19.0F, 4.0F, 19.0F, CubeDeformation.NONE)
						.texOffs(45, 53).addBox(-5.5F, -25.0F, -5.5F, 11.0F, 11.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(0, 64).addBox(-5.0F, -25.0F, -5.0F, 10.0F, 11.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 44).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 6.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(89, 0).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		total.addOrReplaceChild("bottom",
				CubeListBuilder.create()
						.texOffs(42, 50).addBox(-5.0F, -3.0F, 0.0F, 10.0F, 3.0F, 0.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(0.0F, -3.0F, -5.0F, 0.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(89, 12).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick1",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick2",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick3",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick4",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick5",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("stick6",
				CubeListBuilder.create()
						.texOffs(106, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(-0.01F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}



	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}