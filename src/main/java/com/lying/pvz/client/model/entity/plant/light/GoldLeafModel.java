package com.lying.pvz.client.model.entity.plant.light;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
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
//   几何 1:1 搬运。金叶子模型，结构：
//     total（1 个主方块 + 4 个 cube_r + hair + face + left + right）
//       ├─ cube_r1..r4：4 根"叶脉"（各自 Z 轴旋转 15°/30°，膨胀 -0.05）
//       ├─ hair（6 个直接方块 + outer 子树）
//       │   └─ outer → bone1..bone6（6 片"叶子边缘"，Z 轴 ±10° / ±30° / ±60°）
//       ├─ face（3 个方块 + cube_r5/r6 两个嘴角，膨胀 -0.4）
//       ├─ left（1 方块，绕 Z 轴 -45°）
//       └─ right（1 方块，绕 Z 轴 +45°）
//
//   setupAnim：total.yRot = getAttackTime() * 0.5F —— 攻击时整片金叶旋转。
//   1.16.5 原样，保留。

public class GoldLeafModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gold_leaf"), "main");

	private final ModelPart total;

	public GoldLeafModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(40, 41).addBox(-5.0F, -10.0F, -1.0F, 10.0F, 10.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(24, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(6.132F, -7.6264F, 0.0F, 0.0F, 0.0F, -0.5236F));

		total.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(24, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(-6.1439F, -7.7346F, 0.0F, 0.0F, 0.0F, 0.5236F));


		total.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(24, 45).addBox(-2.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(-4.25F, -2.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		total.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(32, 47).addBox(0.5F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(3.75F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2182F));


		PartDefinition hair = total.addOrReplaceChild("hair",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-2.0F, -3.0F, -1.0F, 6.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(16, 60).addBox(0.0F, -4.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F))
						.texOffs(18, 57).addBox(-3.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(14, 56).addBox(4.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(58, 56).addBox(0.5052F, -5.4466F, -1.15F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.15F))
						.texOffs(54, 55).addBox(0.5052F, -5.4466F, -0.85F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.15F)),
				PartPose.offset(-1.0F, -11.0F, 0.0F));

		PartDefinition outer = hair.addOrReplaceChild("outer",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		outer.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(24, 56).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.0F, 5.75F, 0.0F, 0.0F, 0.0F, -0.1745F));

		outer.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(32, 56).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 5.75F, 0.0F, 0.0F, 0.0F, 0.1745F));

		outer.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(40, 58).addBox(-2.3489F, -4.1825F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		outer.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(48, 58).addBox(0.3489F, -4.1825F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		outer.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(56, 59).addBox(-1.5274F, -3.9477F, -1.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		outer.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(0, 53).addBox(-0.4726F, -3.9477F, -1.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, -2.0F, 0.0F, 0.0F, 0.0F, -1.0472F));


		PartDefinition face = total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(18, 48).addBox(-3.0F, -5.75F, -1.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 49).addBox(1.0F, -5.75F, -1.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(10, 50).addBox(-1.0F, -1.75F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		face.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(8, 56).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.6707F, -1.2725F, -1.0F, 0.0F, 0.0F, -0.2618F));

		face.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(26, 54).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.6366F, -1.5313F, -1.0F, 0.0F, 0.0F, 0.2618F));

		total.addOrReplaceChild("left",
				CubeListBuilder.create()
						.texOffs(40, 53).addBox(-0.7071F, -2.7071F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		total.addOrReplaceChild("right",
				CubeListBuilder.create()
						.texOffs(9, 52).addBox(-3.5858F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.total.yRot = state.attackTime * 0.5F;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}