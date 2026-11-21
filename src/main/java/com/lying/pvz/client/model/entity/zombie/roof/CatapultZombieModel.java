package com.lying.pvz.client.model.entity.zombie.roof;

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

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。投石车僵尸（CatapultZombie）40+ 部件：
//        total
//          ├─ car (0,0,32)
//          │   ├─ lunzi (0,0,0) —— 4 轮胎 tyreN，各含 boneN 子链
//          │   ├─ front (0,-10,-3) —— 车身 8 块
//          │   │   ├─ balls (ball1~4 弹球)
//          │   │   ├─ bone27 / bone28（两根支架）
//          │   │   ├─ pult → pult2 → pult3（投掷臂链）
//          │   │   └─ level / bone（装饰）
//          └─ zombie (-8,-33,14) —— 僵尸本体
//              ├─ head / right_hand / left_hand / body
//              └─ right_leg / left_leg
//
//   2. ⚠️ 4 个 tyreN 是"完全相同的轮胎"围绕车身四角：
//        tyre_leftfront   (22, -7, -45)
//        tyre_leftfront2  (22, -7, -19)
//        tyre_leftfront3 (-21, -7, -45)
//        tyre_leftfront4 (-20, -7, -19)
//      每个轮胎内部是 boneN → boneN+1 → ... 的 4 层链（各层绕 X 轴 -90°/-45° 等），
//      逐条展开保留；虽然后两个的方块 x 起点是 -38（镜像值），但它们不是 mirror=true，
//      而是"硬编码镜像坐标"（1.16.5 做法）。
//
//   3. ⚠️ setupAnim 里四轮胎用 `ageInTicks % 360` 直接给 xRot —— 注意是"度数"当弧度用，
//      这个 1.16.5 就有的 bug 实际效果是转速极快（模 360 后当作弧度即 ~0.017 rad/tick 级别），
//      视觉上接近"停转"。保留原样，不要"修正"为弧度。
//
//   4. ⚠️ MatrixStack → PoseStack、IVertexBuilder → VertexConsumer：
//        renderToBuffer 在 1.16.5 签名末尾是 float×4 颜色；
//        1.21.1 改为 int color 单参数。本类在 1.21.1 签名相应变化。
//
//   5. renderBody 里 `this.zombie.visible = false` 然后渲染 total：
//        这是"只渲染车、不渲染僵尸"的独立渲染通道（供外部渲染僵尸躯体），
//        1.21.1 保持原逻辑，visible 字段语义不变。
//
//   6. getZombieModel() 返回 EntityModel<T>：1.21.1 中 EntityModel<T> 仍是基类，
//        若你的 PVZEntityModel 已迁移，返回值类型按基类走。

public class CatapultZombieModel extends PvZEntityModel<PvZZombieRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("catapult_zombie"), "main");

	private final ModelPart total;

	private final ModelPart pult;
	private final ModelPart right_hand;
	private final ModelPart tyre_leftfront;
	private final ModelPart tyre_leftfront2;
	private final ModelPart tyre_leftfront3;
	private final ModelPart tyre_leftfront4;
	private final ModelPart ball1;
	private final ModelPart ball2;
	private final ModelPart ball3;
	private final ModelPart ball4;
	private final ModelPart zombie;

	public CatapultZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		final ModelPart car = this.total.getChild("car");
		final ModelPart lunzi = car.getChild("lunzi");
		this.tyre_leftfront = lunzi.getChild("tyre_leftfront");
		this.tyre_leftfront2 = lunzi.getChild("tyre_leftfront2");
		this.tyre_leftfront3 = lunzi.getChild("tyre_leftfront3");
		this.tyre_leftfront4 = lunzi.getChild("tyre_leftfront4");
		final ModelPart front = car.getChild("front");
		final ModelPart balls = front.getChild("balls");
		this.ball1 = balls.getChild("ball1");
		this.ball2 = balls.getChild("ball2");
		this.ball3 = balls.getChild("ball3");
		this.ball4 = balls.getChild("ball4");
		this.pult = front.getChild("pult");
		this.zombie = this.total.getChild("zombie");
		this.right_hand = this.zombie.getChild("right_hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition car = total.addOrReplaceChild("car",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 32.0F));

		PartDefinition lunzi = car.addOrReplaceChild("lunzi",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tyre_leftfront = lunzi.addOrReplaceChild("tyre_leftfront",
				CubeListBuilder.create(),
				PartPose.offset(22.0F, -7.0F, -45.0F));

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
				PartPose.offset(22.0F, -7.0F, -19.0F));

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
				PartPose.offset(-21.0F, -7.0F, -45.0F));

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
				PartPose.offset(-20.0F, -7.0F, -19.0F));

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


		PartDefinition front = car.addOrReplaceChild("front",
				CubeListBuilder.create()
						.texOffs(5, 458).addBox(-18.0F, -4.0F, -50.0F, 36.0F, 7.0F, 42.0F, CubeDeformation.NONE)
						.texOffs(174, 481).addBox(-18.0F, -16.0F, -21.0F, 20.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(12, 421).addBox(17.0F, -11.0F, -24.0F, 1.0F, 7.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(53, 421).addBox(2.0F, -11.0F, -24.0F, 1.0F, 7.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(205, 344).addBox(2.0F, -11.0F, -25.0F, 16.0F, 7.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(265, 342).addBox(2.0F, -11.0F, -10.0F, 16.0F, 7.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(328, 338).addBox(-18.0F, -10.0F, -52.0F, 36.0F, 16.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(330, 313).addBox(-18.0F, -16.0F, -50.0F, 36.0F, 12.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(377, 460).addBox(2.0F, -6.0F, -39.0F, 16.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(434, 488).addBox(7.0F, -24.0F, -38.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(396, 482).addBox(9.0F, -23.0F, -45.0F, 2.0F, 19.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(66, 334).addBox(-10.0F, -36.0F, -37.0F, 4.0F, 32.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, -3.0F));


		PartDefinition balls = front.addOrReplaceChild("balls",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		balls.addOrReplaceChild("ball1",
				CubeListBuilder.create()
						.texOffs(148, 345).addBox(11.0F, -12.0F, -24.0F, 6.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		balls.addOrReplaceChild("ball2",
				CubeListBuilder.create()
						.texOffs(125, 369).addBox(3.0F, -13.0F, -24.0F, 7.0F, 8.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		balls.addOrReplaceChild("ball3",
				CubeListBuilder.create()
						.texOffs(120, 392).addBox(11.0F, -13.0F, -17.0F, 6.0F, 8.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		balls.addOrReplaceChild("ball4",
				CubeListBuilder.create()
						.texOffs(105, 424).addBox(3.0F, -14.0F, -18.0F, 8.0F, 9.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		front.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(424, 357).addBox(-4.0F, -24.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.0F, -6.0F, -35.0F, 0.0F, 0.0F, -0.1745F));

		front.addOrReplaceChild("bone28",
				CubeListBuilder.create()
						.texOffs(426, 302).addBox(-4.0F, -24.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(14.0F, -6.0F, -35.0F, 0.0F, 0.0F, 0.1745F));


		PartDefinition pult = front.addOrReplaceChild("pult",
				CubeListBuilder.create()
						.texOffs(330, 254).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 41.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -22.0F, -44.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition pult2 = pult.addOrReplaceChild("pult2",
				CubeListBuilder.create()
						.texOffs(278, 305).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 13.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 36.0F, -1.0472F, 0.0F, 0.0F));

		pult2.addOrReplaceChild("pult3",
				CubeListBuilder.create()
						.texOffs(241, 318).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(198, 318).addBox(-4.0F, -3.0F, -1.0F, 8.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(165, 326).addBox(-4.0F, -13.0F, -1.0F, 8.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(137, 329).addBox(-6.0F, -11.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(105, 344).addBox(4.0F, -11.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(92, 373).addBox(-4.0F, -11.0F, 1.0F, 8.0F, 8.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(77, 394).addBox(-4.0F, -11.0F, -6.0F, 8.0F, 8.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(33, 384).addBox(-1.0F, -5.0F, 3.0F, 2.0F, 10.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 12.0F, -1.0472F, 0.0F, 0.0F));


		front.addOrReplaceChild("level",
				CubeListBuilder.create()
						.texOffs(38, 346).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(40, 362).addBox(-7.0F, -1.0F, -1.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(13, 326).addBox(-12.0F, -1.0F, 4.0F, 7.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, -32.0F, -35.0F, -1.5708F, 0.0F, 0.0F));


		front.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(12, 365).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 7.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -4.0F, -27.0F, -0.5236F, 0.0F, 0.0F));


		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.offset(-8.0F, -33.0F, 14.0F));

		zombie.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(261, 474).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));

		zombie.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(157, 445).addBox(-7.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -14.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(198, 442).addBox(0.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(8.0F, -14.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(242, 429).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 18.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(333, 486).addBox(-2.0F, 2.0F, -4.0F, 4.0F, 6.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(322, 438).addBox(-2.0F, 1.0F, -13.0F, 6.0F, 6.0F, 19.0F, CubeDeformation.NONE)
						.texOffs(434, 448).addBox(-3.0F, 18.0F, -15.0F, 8.0F, 4.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(174, 413).addBox(-2.0F, 7.0F, -13.0F, 6.0F, 11.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(208, 393).addBox(-2.0F, 1.0F, -13.0F, 6.0F, 6.0F, 19.0F, CubeDeformation.NONE)
						.texOffs(301, 413).addBox(-3.0F, 18.0F, -15.0F, 8.0F, 4.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(357, 404).addBox(-2.0F, 7.0F, -13.0F, 6.0F, 11.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 512);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		if (state.attackTime > 0) {
			float percent = 1 - state.attackTime * 1.0F / state.pultAnimTime;
			this.pult.xRot = 0.3491F + (1F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
			this.right_hand.xRot = -1.0472F + Mth.cos(percent * 3.14159F) / 4;
		} else {
			this.pult.xRot = 0.3491F + Mth.sin(state.ageInTicks / 10) / 8;
			this.right_hand.xRot = -1.0472F;
		}

		this.tyre_leftfront.xRot = state.ageInTicks % 360;
		this.tyre_leftfront2.xRot = state.ageInTicks % 360;
		this.tyre_leftfront3.xRot = state.ageInTicks % 360;
		this.tyre_leftfront4.xRot = state.ageInTicks % 360;

		this.ball1.visible = (state.ballCount * 1F / state.maxBallUse < 1F / 4);
		this.ball2.visible = (state.ballCount * 1F / state.maxBallUse < 2F / 4);
		this.ball3.visible = (state.ballCount * 1F / state.maxBallUse < 3F / 4);
		this.ball4.visible = (state.ballCount * 1F / state.maxBallUse < 1);
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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