package com.lying.pvz.client.model.entity.plant.fire;

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
//     total（根）
//       ├─ 3 个直接方块（辣椒主体 + 外壳）
//       └─ cube_r1 / cube_r2 / cube_r3（三个带 X 轴旋转的装饰面）
//   注意 total 层第 2 个方块用了 -0.5 膨胀，作为外皮内缩；cube_r1 的第 1 个方块用 +0.6 膨胀，
//   是"辣椒顶部的火苗光环"，膨胀值不可改。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class JalapenoModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("jalapeno"), "main");

	private final ModelPart total;

	public JalapenoModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 13.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 38).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 13.0F, 10.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 30).addBox(0.0F, -29.0F, -7.0F, 0.0F, 9.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(32, 15).addBox(-4.0F, -1.3F, -4.25F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.6F))
						.texOffs(24, 30).addBox(-4.0F, -1.5F, -4.25F, 8.0F, 3.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -20.5F, 0.0F, 0.1309F, 0.0F, 0.0F));

		total.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-4.0F, -4.5F, -4.75F, 8.0F, 7.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, -0.2182F, 0.0F, 0.0F));

		total.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(30, 0).addBox(-3.0F, 1.5F, -3.0F, 6.0F, 5.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}