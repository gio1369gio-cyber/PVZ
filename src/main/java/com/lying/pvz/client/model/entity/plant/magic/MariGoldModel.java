package com.lying.pvz.client.model.entity.plant.magic;


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


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。万寿菊（MariGold）模型：
//     total
//       ├─ head（3 个方块 + cube_r1/cube_r2 两根眉毛 + 6 片花瓣链）
//       │   ├─ HUABAN → HUABAN2 → HUABAN3（外圈花瓣链，每片 -0.5 膨胀，z=-7.1）
//       │   └─ HUABAN4 → HUABAN5 → HUABAN6（内圈花瓣链，每片 0 膨胀，z=-6.0）
//       ├─ body（1 个方块 4×35×4 + cube_r3 花茎底座）
//       └─ YEZI_1 / YEZI_2 / YEZI_3 / YEZI_4（4 片地叶）
//
//   ⚠️ 花瓣链结构（HUABAN → HUABAN2 → HUABAN3）是"递归子节点"，不是兄弟节点！
//      这与 TwinSunFlower 的花瓣结构完全一致，是 1.16.5 用"轴心叠加"实现花瓣
//      从中心辐射铺开的手法，**不能改成兄弟节点**，否则每片花瓣的旋转中心会跑到
//      花盘中心，视觉错乱。
//
//   ⚠️ 外圈 3 片（HUABAN1~3）膨胀 -0.5、z=-7.1；内圈 3 片（HUABAN4~6）膨胀 0、z=-6.0。
//      这是 1.16.5 作者刻意做的"花瓣层次感"，逐值保留。
//
//   泛型：类声明是 MariGoldModel<T extends MariGoldEntity>，与 WallNutModel 一致。
//   setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class MariGoldModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("marigold"), "main");

	private final ModelPart total;

	public MariGoldModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(73, 100).addBox(-11.0F, -15.0F, -5.99F, 22.0F, 21.0F, 4.0F, new CubeDeformation(-0.02F))
						.texOffs(0, 123).addBox(-5.0F, 5.95F, -5.99F, 10.0F, 1.0F, 4.0F, new CubeDeformation(-0.02F))
						.texOffs(85, 100).addBox(-1.5F, -0.25F, -6.25F, 3.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -32.0F, 0.0F));


		head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(107, 88).addBox(-3.5F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-4.2776F, -11.4466F, -5.75F, 0.0F, 0.0F, -0.3054F));

		head.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(106, 94).addBox(-4.0F, 0.0F, 0.0F, 7.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(5.5F, -12.25F, -6.25F, 0.0F, 0.0F, 0.3054F));


		PartDefinition huaban = head.addOrReplaceChild("HUABAN",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0F, -18.0F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(29, 63).addBox(-3.0F, 9.8564F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(42, 36).addBox(10.9282F, -4.0718F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(73, 73).addBox(-16.9282F, -4.0718F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition huaban2 = huaban.addOrReplaceChild("HUABAN2",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.4641F, -17.8564F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(29, 63).addBox(-2.4641F, 10.0F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(42, 36).addBox(11.4641F, -3.9282F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(73, 73).addBox(-16.3923F, -3.9282F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban2.addOrReplaceChild("HUABAN3",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.0718F, -17.4641F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(29, 63).addBox(-2.0718F, 10.3923F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(42, 36).addBox(11.8564F, -3.5359F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(73, 73).addBox(-16.0F, -3.5359F, -7.1F, 6.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));


		PartDefinition huaban4 = head.addOrReplaceChild("HUABAN4",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-4.0F, -16.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-4.0F, 9.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(10.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition huaban5 = huaban4.addOrReplaceChild("HUABAN5",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.4641F, -15.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.4641F, 10.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.4641F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.3923F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban5.addOrReplaceChild("HUABAN6",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0718F, -15.4641F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.0718F, 10.3923F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.8564F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.0F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(112, 43).addBox(-2.0F, -34.0F, -2.0F, 4.0F, 35.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));


		body.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(23, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -34.7071F, -0.1213F, 0.7854F, 0.0F, 0.0F));


		total.addOrReplaceChild("YEZI_4",
				CubeListBuilder.create()
						.texOffs(78, 26).addBox(-11.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(114, 28).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, 0.0F));

		total.addOrReplaceChild("YEZI_3",
				CubeListBuilder.create()
						.texOffs(114, 19).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 36).addBox(-4.0F, -2.0F, -11.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -2.0F));

		total.addOrReplaceChild("YEZI_1",
				CubeListBuilder.create()
						.texOffs(114, 0).addBox(0.0F, -1.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(79, 0).addBox(3.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("YEZI_2",
				CubeListBuilder.create()
						.texOffs(78, 12).addBox(-4.0F, -2.0F, 3.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(114, 9).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 2.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}