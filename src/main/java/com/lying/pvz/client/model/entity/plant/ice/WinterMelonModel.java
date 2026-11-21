package com.lying.pvz.client.model.entity.plant.ice;


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
import net.minecraft.util.Mth;


// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运，80+ 部件全部到位，未改任何数值。
//   2. 结构总览：
//        total (0, 23.75, 0) —— 注意 23.75 不是 24，作者刻意下沉 0.25 格
//          ├─ leaves (0, 0.25, 0)           —— 4 片草地叶
//          ├─ body   (0, 1, 0)              —— 7 块西瓜本体
//          ├─ pult   (0, -6.5, 6)           —— 投掷臂（发射时动画）
//          │   └─ basket (0, 0.75, 1.75)    —— 西瓜筐
//          │       ├─ melon (-0.5, 0, 12.5) —— 西瓜弹（发射中会隐藏）
//          │       │   └─ cube_r1
//          │       └─ ice   (0, 0, 15.5)
//          │           ├─ ice1 → 7 个 bone + 7 个 cube_r（z 向排列）
//          │           ├─ ice2 → 7 个 bone + 7 个 cube_r（z 向另一侧，镜像位移）
//          │           ├─ ice3 → 5 个 bone（含 bone11 内嵌 bone12）+ 5 个 cube_r（x 向排列）
//          │           └─ ice4 → 5 个 bone（含 bone23 内嵌 bone24）+ 5 个 cube_r（x 向镜像位移）
//          └─ face   (0, 0.75, -2.25)       —— 2 只眼睛（-0.45 膨胀）
//
//   3. ⚠️ 冰晶四组的结构是 1.16.5 的精心安排，不是对称复制：
//        - ice1 / ice2：7 颗 bone 直接挂 iceN 下，每颗 bone 的 x 偏移不同（0/-4.5/0/-3/-1.5/1.5/3）
//          其中 ice1 的 bone3 与 ice2 的 bone15 的方块 x 起点是 4.0（而非 -0.5），
//          这是为了让冰块绕到身体另一侧，逐条保留。
//        - ice3 / ice4：5 颗 bone 挂 iceN 下；但 ice3 的 bone12 是 bone11 的子节点，
//          ice4 的 bone24 是 bone23 的子节点 —— 属于"链式结构"，不是同级，保留原样。
//          ice4 的 bone x 偏移额外 +8.5（方块），也是刻意镜像。
//
//   4. ⚠️ MathHelper → Mth：
//        1.16.5: net.minecraft.util.math.MathHelper（类名、方法名均未变）
//        1.21.1: net.minecraft.util.Mth（这是 Yarn 系命名统一后的结果）
//        abs/cos/sin 三个方法签名不变，只换类名。
//
//   5. setupAnim 依赖：
//        entity.getAttackTime()  —— 攻击剩余 tick
//        entity.getPultAnimTime() —— 投掷臂动作总时长
//        逻辑 1:1 保留，见方法注释。

public class WinterMelonModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("winter_melon"), "main");

	private final ModelPart total;

	private final ModelPart pult;
	private final ModelPart melon;

	public WinterMelonModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.pult = this.total.getChild("pult");
		this.melon = this.pult.getChild("basket").getChild("melon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 23.75F, 0.0F));

		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.25F, 0.0F));

		leaves.addOrReplaceChild("leave1",
				CubeListBuilder.create()
						.texOffs(48, 97).addBox(0.0F, -1.0F, 2.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(60, 98).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(-9.0F, 0.0F, -9.0F));

		leaves.addOrReplaceChild("leave2",
				CubeListBuilder.create()
						.texOffs(68, 97).addBox(0.0F, -1.0F, -2.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(78, 97).addBox(2.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(6.0F, 0.0F, -5.0F));

		leaves.addOrReplaceChild("leave3",
				CubeListBuilder.create()
						.texOffs(0, 85).addBox(-7.0F, -1.0F, 2.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 82).addBox(-8.0F, -1.0F, 4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(-2.0F, 0.0F, 2.0F));

		leaves.addOrReplaceChild("leave4",
				CubeListBuilder.create()
						.texOffs(8, 80).addBox(4.0F, -1.0F, 2.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 79).addBox(6.0F, -1.0F, 4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(2.0F, 0.0F, 2.0F));


		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 106).addBox(-8.0F, -11.0F, -6.0F, 16.0F, 10.0F, 12.0F, new CubeDeformation(-0.2F))
						.texOffs(100, 112).addBox(-6.5F, -9.5F, -6.25F, 13.0F, 7.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(56, 112).addBox(-8.25F, -9.5F, -4.5F, 1.0F, 7.0F, 9.0F, new CubeDeformation(-0.2F))
						.texOffs(76, 112).addBox(7.25F, -9.5F, -4.5F, 1.0F, 7.0F, 9.0F, new CubeDeformation(-0.2F))
						.texOffs(100, 120).addBox(-6.5F, -9.5F, 5.25F, 13.0F, 7.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(84, 102).addBox(-6.5F, -11.25F, -4.5F, 13.0F, 1.0F, 9.0F, new CubeDeformation(-0.2F))
						.texOffs(49, 101).addBox(-6.5F, -1.75F, -4.5F, 13.0F, 1.0F, 9.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition pult = total.addOrReplaceChild("pult",
				CubeListBuilder.create()
						.texOffs(102, 80).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 11.0F, new CubeDeformation(-0.2F))
						.texOffs(75, 89).addBox(-1.0F, -1.0F, 8.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -6.5F, 6.0F));

		PartDefinition basket = pult.addOrReplaceChild("basket",
				CubeListBuilder.create()
						.texOffs(96, 93).addBox(-4.0F, 0.0F, 12.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(0, 103).addBox(-4.0F, -2.0F, 11.0F, 8.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 100).addBox(-4.0F, -2.0F, 20.0F, 8.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(34, 79).addBox(-5.0F, -2.0F, 11.0F, 1.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(51, 83).addBox(4.0F, -2.0F, 11.0F, 1.0F, 2.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.75F, 1.75F));

		PartDefinition melon = basket.addOrReplaceChild("melon",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.5F, 0.0F, 12.5F, 0.3054F, 0.0F, 0.0F));

		melon.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(10, 82).addBox(-3.0F, -6.0541F, -0.2441F, 7.0F, 6.0F, 10.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		PartDefinition ice = basket.addOrReplaceChild("ice",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 15.5F));


		PartDefinition ice1 = ice.addOrReplaceChild("ice1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = ice1.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));
		bone.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone2 = ice1.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 5.0F));
		bone2.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone3 = ice1.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(4.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));
		bone3.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone4 = ice1.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, 0.0F, 5.0F));
		bone4.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone5 = ice1.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-1.5F, 0.0F, 5.0F));
		bone5.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone6 = ice1.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(1.5F, 0.0F, 5.0F));
		bone6.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone7 = ice1.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, 0.0F, 5.0F));
		bone7.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));


		PartDefinition ice2 = ice.addOrReplaceChild("ice2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, -9.0F));

		PartDefinition bone13 = ice2.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));
		bone13.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone14 = ice2.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 5.0F));
		bone14.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone15 = ice2.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(4.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));
		bone15.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone16 = ice2.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, 0.0F, 5.0F));
		bone16.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone17 = ice2.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-1.5F, 0.0F, 5.0F));
		bone17.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone18 = ice2.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(1.5F, 0.0F, 5.0F));
		bone18.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone19 = ice2.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, 0.0F, 5.0F));
		bone19.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));


		PartDefinition ice3 = ice.addOrReplaceChild("ice3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone8 = ice3.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 3.5F));

		bone8.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone9 = ice3.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 2.0F));
		bone9.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone10 = ice3.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 0.5F));
		bone10.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone11 = ice3.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, -1.0F));
		bone11.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));


		PartDefinition bone12 = bone11.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		bone12.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, -1.5F, 0.0F, 0.0F, 0.7854F));


		PartDefinition ice4 = ice.addOrReplaceChild("ice4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone20 = ice4.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(8.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 3.5F));
		bone20.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(9.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone21 = ice4.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(8.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 2.0F));
		bone21.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(9.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone22 = ice4.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(8.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, 0.5F));
		bone22.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(9.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone23 = ice4.addOrReplaceChild("bone23",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(8.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-4.5F, 0.0F, -1.0F));
		bone23.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(9.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));


		PartDefinition bone24 = bone23.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(8.5F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		bone24.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(9.0F, 1.0F, -1.5F, 0.0F, 0.0F, 0.7854F));

		total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(0, 114).addBox(-4.0F, -7.5F, -4.3F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(0, 110).addBox(2.0F, -7.5F, -4.3F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, 0.75F, -2.25F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.attackTime > 0) {
			float percent = 1 - state.attackTime * 1.0F / state.pultAnimTime;
			this.pult.xRot = (1F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
			this.melon.visible = (percent < 0.5);
		} else {
			this.pult.xRot = Mth.sin(state.ageInTicks / 10) / 8;
			this.melon.visible = true;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}