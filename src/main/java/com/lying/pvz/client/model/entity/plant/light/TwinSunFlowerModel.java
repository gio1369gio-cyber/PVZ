package com.lying.pvz.client.model.entity.plant.light;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.AnimationUtil;
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
// 迁移说明（1.16.5 → 1.21.1 NeForge）：
//   几何 1:1 搬运。双生向日葵，全模型最复杂之一。结构：
//     total
//       ├─ left_flower
//       │   ├─ body2 (setPos(0,-1,0) + Z+30°) —— 1 方块（主茎）
//       │   │   ├─ bone → bone2 → cube_r1（茎顶 + 花茎）
//       │   │   ├─ left_hand2 / right_hand2（各 2 个 addBox）
//       │   │   └─ head2（2 个方块 + 8 片花瓣 HUABAN7~12）
//       │   └─ 花瓣链 HUABAN7 → HUABAN8 → HUABAN9（每片各自 addBox 4 个）
//       │           HUABAN10 → HUABAN11 → HUABAN12
//       ├─ right_flower
//       │   ├─ body3 (setPos(0,-1,0) + Z-30°) —— 1 方块（主茎）
//       │   │   ├─ bone3 → bone4 → cube_r2（茎顶）
//       │   │   ├─ right_hand3 / left_hand3
//       │   │   └─ head3（2 个方块 + 8 片花瓣 HUABAN2~6 + HUABAN13）
//       │   └─ 花瓣链 HUABAN2 → HUABAN3 → HUABAN4
//       │           HUABAN5 → HUABAN6 → HUABAN13
//       └─ leaves
//           └─ YEZI_1 / YEZI_2 / YEZI_3 / YEZI_4（4 片地叶）
//
//   ⚠️ 花瓣链是"递归 addOrReplaceChild"结构：HUABAN7 → HUABAN8 → HUABAN9，
//      每片花瓣都挂在上一片下面（而非兄弟）。这是 1.16.5 用"轴心叠加"实现花瓣从中心
//      呈辐射状铺开的手法，逐条保留，不要改成兄弟节点。
//
//   setupAnim 双态：
//     1. 攻击/超级模式：4 只手 zRot 由 getUpDownUpDown(ageInTicks % 10, 10, -30) 驱动
//     2. 常态：left_flower.zRot 与 right_flower.zRot 由 60 tick 周期 getUpDown 驱动，
//        方向相反（±5°），是"两朵花朝相反方向摇摆"的钟摆感。

public class TwinSunFlowerModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("twin_sun_flower"), "main");

	private final ModelPart total;

	private final ModelPart left_flower;
	private final ModelPart right_flower;
	private final ModelPart left_hand2;
	private final ModelPart right_hand2;
	private final ModelPart left_hand3;
	private final ModelPart right_hand3;

	public TwinSunFlowerModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_flower = this.total.getChild("left_flower");
		this.right_flower = this.total.getChild("right_flower");

		final ModelPart body2 = this.left_flower.getChild("body2");
		final ModelPart body3 = this.right_flower.getChild("body3");
		this.left_hand2 = body2.getChild("left_hand2");
		this.right_hand2 = body2.getChild("right_hand2");
		this.left_hand3 = body3.getChild("left_hand3");
		this.right_hand3 = body3.getChild("right_hand3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_flower = total.addOrReplaceChild("left_flower",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition head2 = left_flower.addOrReplaceChild("head2",
				CubeListBuilder.create()
						.texOffs(73, 100).addBox(-11.0F, -15.0F, -5.99F, 22.0F, 21.0F, 4.0F, new CubeDeformation(-0.02F))
						.texOffs(0, 123).addBox(-5.0F, 5.9F, -6.0F, 10.0F, 1.0F, 4.0F, new CubeDeformation(-0.02F)),
				PartPose.offset(16.0F, -46.0F, 0.0F));

		PartDefinition huaban7 = head2.addOrReplaceChild("HUABAN7",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0F, -18.0F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.0F, 9.8564F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(10.9282F, -4.0718F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.9282F, -4.0718F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition huaban8 = huaban7.addOrReplaceChild("HUABAN8",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.4641F, -17.8564F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-2.4641F, 10.0F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.4641F, -3.9282F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.3923F, -3.9282F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban8.addOrReplaceChild("HUABAN9",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.0718F, -17.4641F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-2.0718F, 10.3923F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.8564F, -3.5359F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.0F, -3.5359F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));


		PartDefinition huaban10 = head2.addOrReplaceChild("HUABAN10",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-4.0F, -16.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-4.0F, 9.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(10.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition huaban11 = huaban10.addOrReplaceChild("HUABAN11",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.4641F, -15.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.4641F, 10.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.4641F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.3923F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban11.addOrReplaceChild("HUABAN12",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0718F, -15.4641F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.0718F, 10.3923F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.8564F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.0F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition body2 = left_flower.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(112, 43).addBox(-2.0F, -31.0F, -2.0F, 4.0F, 32.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));


		PartDefinition bone = body2.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.2679F, -17.0F, -2.0F, 4.0F, 18.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -31.0F, 0.0F, 0.0F, 0.0F, -0.5236F));


		PartDefinition bone2 = bone.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -18.0F, 0.0F));

		bone2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-0.2679F, 2.3512F, 0.5254F, 0.7418F, 0.0F, 0.0F));

		PartDefinition left_hand2 = body2.addOrReplaceChild("left_hand2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(2.0563F, -1.2443F, -1.0F, 6.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-3.0197F, -0.3761F, 0.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.134F, -28.5F, 0.0F, 0.0F, 0.0F, -0.3491F));

		body2.addOrReplaceChild("right_hand2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.4088F, -1.1233F, -1.0F, 6.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.4088F, -0.1233F, 0.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.634F, -28.634F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition right_flower = total.addOrReplaceChild("right_flower",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body3 = right_flower.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(112, 43).addBox(-2.0F, -27.0F, -2.0F, 4.0F, 28.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		body3.addOrReplaceChild("right_hand3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.9848F, -0.8264F, -1.0F, 6.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.9848F, 0.1736F, 0.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.7679F, -19.4019F, 0.0F, 0.0F, 0.0F, 0.3054F));


		body3.addOrReplaceChild("left_hand3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(1.9848F, -0.8264F, -1.0F, 6.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.1063F, 0.2155F, 0.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.2321F, -19.4019F, 0.0F, 0.0F, 0.0F, -0.0436F));


		PartDefinition bone3 = body3.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.9319F, -7.4824F, -2.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone4.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(23, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0681F, -8.1895F, -0.1213F, 0.7854F, 0.0F, 0.0F));

		PartDefinition head3 = right_flower.addOrReplaceChild("head3",
				CubeListBuilder.create()
						.texOffs(73, 100).addBox(-11.0F, -15.0F, -5.99F, 22.0F, 21.0F, 4.0F, new CubeDeformation(-0.02F))
						.texOffs(0, 123).addBox(-5.0F, 5.9F, -6.0F, 10.0F, 1.0F, 4.0F, new CubeDeformation(-0.02F)),
				PartPose.offset(-16.0F, -33.0F, 0.0F));

		PartDefinition huaban2 = head3.addOrReplaceChild("HUABAN2",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0F, -18.0F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.0F, 9.8564F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(10.9282F, -4.0718F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.9282F, -4.0718F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition huaban3 = huaban2.addOrReplaceChild("HUABAN3",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.4641F, -17.8564F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-2.4641F, 10.0F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.4641F, -3.9282F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.3923F, -3.9282F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban3.addOrReplaceChild("HUABAN4",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-2.0718F, -17.4641F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-2.0718F, 10.3923F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.8564F, -3.5359F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-16.0F, -3.5359F, -6.0F, 6.0F, 6.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));


		PartDefinition huaban5 = head3.addOrReplaceChild("HUABAN5",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-4.0F, -16.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-4.0F, 9.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(10.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.9282F, -5.0718F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition huaban6 = huaban5.addOrReplaceChild("HUABAN6",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.4641F, -15.8564F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.4641F, 10.0F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.4641F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.3923F, -4.9282F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		huaban6.addOrReplaceChild("HUABAN13",
				CubeListBuilder.create()
						.texOffs(0, 62).addBox(-3.0718F, -15.4641F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 63).addBox(-3.0718F, 10.3923F, -6.0F, 8.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 36).addBox(11.8564F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(73, 73).addBox(-14.0F, -4.5359F, -6.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		leaves.addOrReplaceChild("YEZI_1",
				CubeListBuilder.create()
						.texOffs(114, 0).addBox(0.0F, -1.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(79, 0).addBox(3.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, 0.0F, 0.0F));


		leaves.addOrReplaceChild("YEZI_2",
				CubeListBuilder.create()
						.texOffs(78, 12).addBox(-4.0F, -2.0F, 3.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(114, 9).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 2.0F));

		leaves.addOrReplaceChild("YEZI_3",
				CubeListBuilder.create()
						.texOffs(114, 19).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 36).addBox(-4.0F, -2.0F, -11.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -2.0F));


		leaves.addOrReplaceChild("YEZI_4",
				CubeListBuilder.create()
						.texOffs(78, 26).addBox(-11.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(114, 28).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int tick = state.attackTime;
		if (tick > 0 && tick < state.animCD || state.isPlantInSuperMode) {
			final int T = 10;
			this.left_hand2.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
			this.right_hand2.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
			this.left_hand3.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
			this.right_hand3.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
		} else {
			this.left_hand2.zRot = 0;
			this.right_hand2.zRot = 0;
			this.left_hand3.zRot = 0;
			this.right_hand3.zRot = 0;
		}
		final int T = 60;
		final int time = state.existTick % 60;
		final float degree = 5F;
		this.left_flower.zRot = AnimationUtil.getUpDown(time, T, degree);
		this.right_flower.zRot = AnimationUtil.getUpDown(time, T, -degree);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}