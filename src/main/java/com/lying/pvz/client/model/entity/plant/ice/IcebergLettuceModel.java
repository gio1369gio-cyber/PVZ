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


// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。结构极简：
//     total（1 方块：冰面）
//       └─ berg（2 方块：冰锥外层 + 冰锥内核）
//   ⚠️ berg 第 1 个方块 mirror=true（1.16.5 用于让冰块 UV 左右翻转以匹配异侧冰面）。
//      迁移时用 CubeListBuilder.create().mirror() 一次性开启，这样 berg 内 2 个方块都镜像——
//      但注意第 2 个方块在 1.16.5 里显式写了 false（不镜像）。由于 CubeListBuilder 的 mirror
//      是 per-builder 的状态，必须在第 2 个 addBox 之前显式 .mirror(false) 重置，否则会误镜像。
//      → 因此这里拆成两个 addOrReplaceChild，用两次 CubeListBuilder.create() 分别控制。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class IcebergLettuceModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("iceberg_lettuce"), "main");

	private final ModelPart total;

	public IcebergLettuceModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition berg = total.addOrReplaceChild("berg",
				CubeListBuilder.create().mirror()
						.texOffs(0, 0).addBox(-3.0F, -5.5F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));


		berg.addOrReplaceChild("core",
				CubeListBuilder.create()
						.texOffs(0, 13).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}