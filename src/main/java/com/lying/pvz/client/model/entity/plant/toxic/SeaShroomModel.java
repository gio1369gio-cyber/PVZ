package com.lying.pvz.client.model.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。海蘑菇模型：
//     total（根，7 个方块：4 层菌盖 + 嘴 2 层 + 下沿 1 个）
//       └─ tail（setPos(0,0,0)，5 条独立尾巴链，从骨骼根部放射）
//            ├─ bone → cube_r1
//            │    └─ bone2 → cube_r2
//            │         └─ bone3 → cube_r3（左前，3 段）
//            ├─ bone4 → cube_r4
//            │    └─ bone5 → cube_r5
//            │         └─ bone6 → cube_r6（右前，3 段）
//            ├─ bone7 → cube_r7
//            │    └─ bone8 → cube_r8
//            │         └─ bone9 → cube_r9（左后，3 段）
//            ├─ bone10 → cube_r10
//            │    └─ bone11 → cube_r11
//            │         └─ bone12 → cube_r12（右后，3 段）
//            └─ bone13 → cube_r13
//                 └─ bone14 → cube_r14
//                      └─ bone15 → cube_r15（最后，3 段，带 -0.5236F X 旋转）
//   setupAnim：空实现，原代码即如此。
//   所有方块使用 CubeDeformation，膨胀 0.1F 用于所有尾巴片。
//   骨骼链偏移坐标带多位小数（0.9772、0.8941、0.8706 等），1:1 保留。

public class SeaShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sea_shroom"), "main");

	private final ModelPart total;

	public SeaShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(31, 47).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(14, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 4.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 17).addBox(-5.0F, -15.0F, -5.0F, 10.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 31).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(1, 42).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(9, 41).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 14.0F, 0.0F));

		PartDefinition tail = total.addOrReplaceChild("tail",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = tail.addOrReplaceChild("bone",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.1309F, 0.0F, 0.1309F));

		bone.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(58, 50).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.9772F, 0.348F));

		bone2.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(59, 43).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone3.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(54, 40).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 3.8941F, 0.863F, 0.3491F, 0.0F, 0.0F));

		PartDefinition bone4 = tail.addOrReplaceChild("bone4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.1309F, 0.0F, -0.1309F));

		bone4.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(9, 59).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.9914F, 0.8706F, 0.017F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bone5 = bone4.addOrReplaceChild("bone5",
				CubeListBuilder.create(),
				PartPose.offset(0.9914F, 3.8478F, 0.365F));

		bone5.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(4, 58).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition bone6 = bone5.addOrReplaceChild("bone6",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone6.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 47).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 3.8941F, 0.863F, 0.3491F, 0.0F, 0.0F));


		PartDefinition bone7 = tail.addOrReplaceChild("bone7",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.0F, 0.0F, -2.0F, 0.1309F, -1.0472F, 0.1309F));

		bone7.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(1, 54).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bone8 = bone7.addOrReplaceChild("bone8",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.9772F, 0.348F));

		bone8.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(6, 48).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition bone9 = bone8.addOrReplaceChild("bone9",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone9.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(11, 49).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 3.8941F, 0.863F, 0.3491F, 0.0F, 0.0F));


		PartDefinition bone10 = tail.addOrReplaceChild("bone10",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(2.0F, 0.0F, -2.0F, 0.1309F, 1.0472F, -0.1309F));

		bone10.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(17, 48).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.4957F, 0.7585F, -0.8342F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bone11 = bone10.addOrReplaceChild("bone11",
				CubeListBuilder.create(),
				PartPose.offset(0.4957F, 3.7357F, -0.4863F));

		bone11.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(22, 50).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition bone12 = bone11.addOrReplaceChild("bone12",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone12.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(22, 41).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 3.8941F, 0.863F, 0.3491F, 0.0F, 0.0F));


		PartDefinition bone13 = tail.addOrReplaceChild("bone13",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, -3.0F));

		bone13.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(28, 42).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition bone14 = bone13.addOrReplaceChild("bone14",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 3.9772F, 0.348F, -0.5236F, 0.0F, 0.0F));

		bone14.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(34, 41).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.2046F, 0.0979F, -0.7978F, 0.1745F, 0.0F, -0.1745F));

		PartDefinition bone15 = bone14.addOrReplaceChild("bone15",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone15.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(59, 35).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.8752F, 3.9011F, -0.3679F, -0.0873F, 0.0F, -0.1745F));

		return LayerDefinition.create(mesh, 64, 64);
	}



	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}