package com.lying.pvz.client.model.entity.plant.explosion;


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

// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。结构：body（纯容器）下挂
//     - bone → bone3（左侧茎 + 分叉小叶）
//     - bone2（右侧茎）
//     - head1（左樱桃，7 个方块：主体 + 4 面 + 上下盖）
//     - head2（右樱桃，与 head1 对称，但 tx/ty 各自独立）
//   head1/head2 的 7 个方块写法在 1.16.5 就是两份独立 addBox，不用循环合并，
//   保留原样便于日后 diff。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class CherryBombModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("cherry_bomb"), "main");

	private final ModelPart body;

	public CherryBombModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition body = root.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = body.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(52, 105).addBox(-2.0F, -17.0F, -1.0F, 2.0F, 20.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-9.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.7854F));


		bone.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(36, 113).addBox(-1.0F, -8.0F, -3.0F, 1.0F, 8.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.0F, -0.3491F, 0.6981F));

		body.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(23, 108).addBox(0.0F, -13.0F, -1.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -18.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		body.addOrReplaceChild("head1",
				CubeListBuilder.create()
						.texOffs(63, 61).addBox(-8.0F, 1.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(97, 30).addBox(8.0F, 2.0F, -7.0F, 1.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(64, 31).addBox(-9.0F, 2.0F, -7.0F, 1.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(97, 14).addBox(-7.0F, 2.0F, -9.0F, 14.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(64, 13).addBox(-7.0F, 2.0F, 8.0F, 14.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 85).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(1, 68).addBox(-7.0F, 17.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-11.0F, -18.0F, 0.0F, 0.0F, 0.2618F, 0.0F));


		body.addOrReplaceChild("head2",
				CubeListBuilder.create()
						.texOffs(63, 95).addBox(-8.0F, 1.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(29, 37).addBox(8.0F, 2.0F, -7.0F, 1.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(3, 7).addBox(-9.0F, 2.0F, -7.0F, 1.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(4, 140).addBox(-7.0F, 2.0F, -9.0F, 14.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(44, 139).addBox(-7.0F, 2.0F, 8.0F, 14.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(84, 138).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(8, 163).addBox(-7.0F, 17.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(11.0F, -18.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.body;
	}
}