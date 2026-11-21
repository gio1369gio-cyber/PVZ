package com.lying.pvz.client.model.entity.zombie.pool;


import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.PvZEntityModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。冰车僵尸模型（独立 PVZEntityModel，实现 IZombieModel）：
//     total（根，setPos(0, 23, 0)）
//       ├─ car（setPos(0, 1, -3)）
//       │    ├─ lunzi（setPos(0,0,0)）：4 个轮子，每个轮子 2 级骨骼链
//       │    │    ├─ tyre_leftfront（setPos(19,-7,-45)）
//       │    │    │    ├─ bone14（setPos(0,-3,-34)）→ bone12（2 方块）+ bone13（X -90°，2 方块）
//       │    │    │    └─ bone15（setPos(0,-5,-36) + X -45°）→ bone16（2 方块）+ bone17（X -90°，2 方块）
//       │    │    ├─ tyre_leftfront2（setPos(19,-7,-12)）：结构同上，bone2~bone7
//       │    │    ├─ tyre_leftfront3（setPos(-18,-7,-45)）：bone8~bone11，bone18/bone19
//       │    │    └─ tyre_leftfront4（setPos(-17,-7,-12)）：bone20~bone25
//       │    ├─ chair（setPos(0,-26,20)）→ bone（X -10°，3 方块）
//       │    ├─ mid（setPos(0,-10,2)，4 方块）→ bone26（X 65°）+ ice（4 方块）
//       │    └─ front（setPos(0,-10,-3)，1 方块）→ level1/level2/gai/ice2
//       └─ zombie（setPos(0,-32,12)）
//            ├─ head（setPos(0,-25,0)，3 方块）→ bone27/bone28/bone29
//            ├─ right_hand（X -70°，1 方块）
//            ├─ left_hand（X -70°，1 方块）
//            ├─ body（2 方块）
//            ├─ right_leg（3 方块）+ left_leg（3 方块）
//   setupAnim：
//     - ice2.xRot = sin(ageInTicks/20)/2（车头冰面的轻微摇摆）
//     - 4 个轮子 xRot = ageInTicks % 360（匀速旋转）
//   tickPartAnim：空实现。
//   renderBody：zombie.visible = false 后渲染 total（掉落部件时隐藏僵尸）。
//   renderToBuffer：8 参数 → 5 参数。
//   MathHelper → Mth：sin 方法不变。
//   字段裁剪：setupAnim 访问 ice2 与 4 个轮子，renderBody 访问 zombie，
//             renderToBuffer 访问 total，因此只保留 total / ice2 / 4 个轮子 / zombie 共 7 个字段。
//             其余 40+ 个字段（car/lunzi/chair/mid/front/gai/level*/body/head/四肢/各 bone）全部移除。

public class ZomboniModel extends PvZEntityModel<PvZZombieRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("zomboni"), "main");

	private final ModelPart total;

	private final ModelPart ice2;
	private final ModelPart tyre_leftfront;
	private final ModelPart tyre_leftfront2;
	private final ModelPart tyre_leftfront3;
	private final ModelPart tyre_leftfront4;

	private final ModelPart zombie;

	public ZomboniModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		ModelPart car = this.total.getChild("car");
		ModelPart lunzi = car.getChild("lunzi");
		this.tyre_leftfront = lunzi.getChild("tyre_leftfront");
		this.tyre_leftfront2 = lunzi.getChild("tyre_leftfront2");
		this.tyre_leftfront3 = lunzi.getChild("tyre_leftfront3");
		this.tyre_leftfront4 = lunzi.getChild("tyre_leftfront4");
		this.ice2 = car.getChild("front").getChild("ice2");
		this.zombie = this.total.getChild("zombie");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 23.0F, 0.0F));


		PartDefinition car = total.addOrReplaceChild("car",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 1.0F, -3.0F));

		PartDefinition lunzi = car.addOrReplaceChild("lunzi",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition tyre_leftfront = lunzi.addOrReplaceChild("tyre_leftfront",
				CubeListBuilder.create(),
				PartPose.offset(19.0F, -7.0F, -45.0F));


		PartDefinition bone14 = tyre_leftfront.addOrReplaceChild("bone14",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -3.0F, -34.0F));

		bone14.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(478, 459).addBox(-4.0F, 3.0F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(481, 436).addBox(-4.0F, -4.4853F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone14.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(478, 412).addBox(-4.0F, -35.7574F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(481, 390).addBox(-4.0F, -43.2426F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone15 = tyre_leftfront.addOrReplaceChild("bone15",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -5.0F, -36.0F, -0.7854F, 0.0F, 0.0F));

		bone15.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(481, 372).addBox(-4.0F, -21.8492F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(480, 345).addBox(-4.0F, -29.3345F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone15.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(478, 324).addBox(-4.0F, -30.5772F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(480, 297).addBox(-4.0F, -38.0624F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre_leftfront2 = lunzi.addOrReplaceChild("tyre_leftfront2",
				CubeListBuilder.create(),
				PartPose.offset(19.0F, -7.0F, -12.0F));

		PartDefinition bone2 = tyre_leftfront2.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -3.0F, -34.0F));

		bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(481, 270).addBox(-4.0F, 3.0F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(481, 241).addBox(-4.0F, -4.4853F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(486, 216).addBox(-4.0F, -35.7574F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(486, 192).addBox(-4.0F, -43.2426F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone5 = tyre_leftfront2.addOrReplaceChild("bone5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -5.0F, -36.0F, -0.7854F, 0.0F, 0.0F));

		bone5.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(488, 145).addBox(-4.0F, -21.8492F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(484, 169).addBox(-4.0F, -29.3345F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone5.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(486, 124).addBox(-4.0F, -30.5772F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(486, 100).addBox(-4.0F, -38.0624F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre_leftfront3 = lunzi.addOrReplaceChild("tyre_leftfront3",
				CubeListBuilder.create(),
				PartPose.offset(-18.0F, -7.0F, -45.0F));

		PartDefinition bone8 = tyre_leftfront3.addOrReplaceChild("bone8",
				CubeListBuilder.create(),
				PartPose.offset(37.0F, -3.0F, -34.0F));

		bone8.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(486, 75).addBox(-38.0F, 3.0F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(484, 56).addBox(-38.0F, -4.4853F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone8.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(486, 36).addBox(-38.0F, -35.7574F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(486, 12).addBox(-38.0F, -43.2426F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone11 = tyre_leftfront3.addOrReplaceChild("bone11",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(37.0F, -5.0F, -36.0F, -0.7854F, 0.0F, 0.0F));

		bone11.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(456, 11).addBox(-38.0F, -21.8492F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(452, 38).addBox(-38.0F, -29.3345F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone11.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(456, 57).addBox(-38.0F, -30.5772F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(457, 80).addBox(-38.0F, -38.0624F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition tyre_leftfront4 = lunzi.addOrReplaceChild("tyre_leftfront4",
				CubeListBuilder.create(),
				PartPose.offset(-17.0F, -7.0F, -12.0F));

		PartDefinition bone20 = tyre_leftfront4.addOrReplaceChild("bone20",
				CubeListBuilder.create(),
				PartPose.offset(36.0F, -3.0F, -34.0F));

		bone20.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(449, 100).addBox(-38.0F, 3.0F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(454, 124).addBox(-38.0F, -4.4853F, 31.0F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone20.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(454, 152).addBox(-38.0F, -35.7574F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(457, 176).addBox(-38.0F, -43.2426F, 9.7574F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone23 = tyre_leftfront4.addOrReplaceChild("bone23",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(36.0F, -5.0F, -36.0F, -0.7854F, 0.0F, 0.0F));

		bone23.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(454, 200).addBox(-38.0F, -21.8492F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(454, 220).addBox(-38.0F, -29.3345F, 25.8198F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone23.addOrReplaceChild("bone25",
				CubeListBuilder.create()
						.texOffs(452, 246).addBox(-38.0F, -30.5772F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(452, 268).addBox(-38.0F, -38.0624F, -15.0919F, 4.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition chair = car.addOrReplaceChild("chair",
				CubeListBuilder.create()
						.texOffs(438, 492).addBox(-10.0F, -1.0F, -12.0F, 20.0F, 1.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -26.0F, 20.0F));


		chair.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(384, 483).addBox(-10.0F, -22.9848F, -0.1736F, 20.0F, 22.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(454, 472).addBox(10.0F, -2.9848F, -1.1736F, 1.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(459, 457).addBox(-11.0F, -2.9848F, -1.1736F, 1.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.1745F, 0.0F, 0.0F));


		PartDefinition mid = car.addOrReplaceChild("mid",
				CubeListBuilder.create()
						.texOffs(4, 491).addBox(-15.0F, -1.0F, -7.0F, 30.0F, 3.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(89, 488).addBox(-15.0F, -16.0F, 8.0F, 30.0F, 18.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(171, 491).addBox(-15.0F, -16.0F, 9.0F, 29.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(299, 497).addBox(-8.0F, -8.0F, 9.0F, 16.0F, 4.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, 2.0F));


		mid.addOrReplaceChild("bone26",
				CubeListBuilder.create()
						.texOffs(262, 497).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, 17.0F, 1.1345F, 0.0F, 0.0F));


		mid.addOrReplaceChild("ice",
				CubeListBuilder.create()
						.texOffs(400, 472).addBox(-8.0F, 1.0F, 0.0F, 16.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 470).addBox(-9.0F, 5.0F, -1.0F, 18.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(56, 467).addBox(-11.0F, 6.0F, -3.0F, 22.0F, 1.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(372, 374).addBox(-13.0F, 7.0F, -5.0F, 26.0F, 2.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition front = car.addOrReplaceChild("front",
				CubeListBuilder.create()
						.texOffs(6, 363).addBox(-15.0F, -35.0F, -50.0F, 30.0F, 38.0F, 48.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, -3.0F));


		front.addOrReplaceChild("level1",
				CubeListBuilder.create()
						.texOffs(176, 452).addBox(1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -28.0F, -1.0F, 1.1345F, 0.0F, 0.0F));


		front.addOrReplaceChild("level2",
				CubeListBuilder.create()
						.texOffs(230, 459).addBox(1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-13.0F, -28.0F, -1.0F, 1.1345F, 0.0F, 0.0F));


		front.addOrReplaceChild("gai",
				CubeListBuilder.create()
						.texOffs(4, 291).addBox(-17.0F, -7.0F, 0.0F, 34.0F, 21.0F, 45.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -36.0F, -52.0F));


		front.addOrReplaceChild("ice2",
				CubeListBuilder.create()
						.texOffs(432, 359).addBox(-8.0F, 1.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(338, 359).addBox(-9.0F, 2.0F, -1.0F, 18.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(263, 348).addBox(-11.0F, 3.0F, -3.0F, 22.0F, 2.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 2.0F, -25.0F));


		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -32.0F, 12.0F));

		PartDefinition head = zombie.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(291, 449).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(369, 440).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(294, 435).addBox(-7.0F, -17.0F, 0.0F, 14.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		head.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(179, 443).addBox(0.0F, 0.0F, -2.0F, 8.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(8.0F, -14.0F, 0.0F, 0.0F, -0.3491F, 1.3963F));


		head.addOrReplaceChild("bone28",
				CubeListBuilder.create()
						.texOffs(219, 446).addBox(-7.0F, -1.0F, -2.0F, 8.0F, 1.0F, 4.0F, CubeDeformation.NONE).mirror(),
				PartPose.offsetAndRotation(-8.0F, -13.0F, 0.0F, 0.0F, 0.3491F, -1.3963F));


		head.addOrReplaceChild("bone29",
				CubeListBuilder.create()
						.texOffs(264, 444).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -17.0F, 8.0F, 0.3491F, 0.0F, 0.0F));


		zombie.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(176, 403).addBox(-7.0F, -3.0F, -3.0F, 6.0F, 21.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -21.0F, 0.0F, -1.2217F, 0.0F, 0.0F));


		zombie.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(217, 406).addBox(0.0F, -3.0F, -3.0F, 6.0F, 21.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(8.0F, -21.0F, 0.0F, -1.2217F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(252, 400).addBox(-8.0F, -23.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(321, 409).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 6.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));


		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(358, 398).addBox(-2.0F, 0.0F, -13.0F, 6.0F, 6.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(414, 418).addBox(-3.0F, 18.0F, -16.0F, 8.0F, 4.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(168, 377).addBox(-2.0F, 6.0F, -13.0F, 6.0F, 12.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(212, 366).addBox(-2.0F, 0.0F, -13.0F, 6.0F, 6.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(274, 375).addBox(-3.0F, 18.0F, -16.0F, 8.0F, 4.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(326, 373).addBox(-2.0F, 6.0F, -13.0F, 6.0F, 12.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 512);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		this.ice2.xRot = Mth.sin(state.ageInTicks / 20) / 2;
		this.tyre_leftfront.xRot = state.ageInTicks % 360;
		this.tyre_leftfront2.xRot = state.ageInTicks % 360;
		this.tyre_leftfront3.xRot = state.ageInTicks % 360;
		this.tyre_leftfront4.xRot = state.ageInTicks % 360;
	}


	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
							 float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		this.zombie.visible = false;
	}

	@Override
	public EntityModel<? extends EntityRenderState> getZombieModel() {
		return this;
	}


}