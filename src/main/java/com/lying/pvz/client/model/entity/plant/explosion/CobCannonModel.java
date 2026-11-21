package com.lying.pvz.client.model.entity.plant.explosion;


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


// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 全模型 60+ 部件，是 pom 中最庞大的单个模型。几何 1:1 搬运，未改任何数值。
//   2. 结构：
//        total
//          ├─ wheels（4 轮）
//          │   ├─ wheel1 (9,0,-10)   —— 2 个直接方块 + 15 个 X 轴均布 cube_r1..r15
//          │   ├─ wheel2 (-9,0,-10)  —— 同上，r16..r30
//          │   ├─ wheel3 (9,0,11)    —— 同上，r31..r45
//          │   └─ wheel4 (-9,0,11)   —— 同上，r46..r60
//          ├─ base (0,-7,0)         —— 5 个方块（底座）
//          └─ body (0,-9,10)        —— 绕 X 轴 1.5708（正前仰角）
//              ├─ corn (0,3,-2)     —— 玉米弹（仅 time < animCD/2 可见）
//              ├─ outer (0,0,0)     —— 4 层外皮 + 4 片叶
//              │   ├─ layer1..4     —— 每层 7 个 -0.25 薄片
//              │   └─ leaves.leaf1..4 → lN → cube_rN + boneN
//              ├─ tail (0,6,0)      —— 尾巴：2 直接方块 + cube_r65..r68
//              └─ face              —— 2 只眼睛（-0.45 膨胀）
//
//   3. 轮辐 cube_r1..r60：4 个轮子几何完全相同，只是 tx/ty 分别指向 (0,0) / (0,7) / (0,14) / (0,21)。
//      每轮 15 个 cube_r 的 X 轴旋转角也完全相同：
//        { 2.7489, 2.3562, 1.9635, 1.5708, 1.1781, 0.7854, 0.3927, 3.1416,
//         -2.7489, -2.3562, -1.9635, -1.5708, -1.1781, -0.7854, -0.3927 }
//      即 ±22.5° 一圈 16 等分（缺一个在 +0 位置，由 wheelN 自身的直接方块补足）。
//      为便于日后 diff，逐条展开写出，不合并循环。
//
//   4. setupAnim 依赖：
//      - entity.isPlayerRiding()  → 轮子旋转
//      - entity.getAttackTime() / getAnimCD() / getCornNum() → 发射时仰角 & 玉米可见性
//      - AnimationUtil.getUpDown(time, animCD, 90) → 仰角曲线
//      上述 API 若在 1.21.1 未改名则原样编译；本批仅完成几何迁移 + 逻辑保留。

public class CobCannonModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("cob_cannon"), "main");

	private final ModelPart total;

	private final ModelPart wheel1;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;
	private final ModelPart body;
	private final ModelPart corn;

	public CobCannonModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		final ModelPart wheels = this.total.getChild("wheels");
		this.wheel1 = wheels.getChild("wheel1");
		this.wheel2 = wheels.getChild("wheel2");
		this.wheel3 = wheels.getChild("wheel3");
		this.wheel4 = wheels.getChild("wheel4");
		this.body = this.total.getChild("body");
		this.corn = this.body.getChild("corn");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition wheels = total.addOrReplaceChild("wheels",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition wheel1 = wheels.addOrReplaceChild("wheel1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(61, 77).addBox(-0.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(9.0F, 0.0F, -10.0F));


		wheel1.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.7489F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9635F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.7489F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9635F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1781F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		wheel1.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition wheel2 = wheels.addOrReplaceChild("wheel2",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(45, 102).addBox(-1.75F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(-9.0F, 0.0F, -10.0F));

		wheel2.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.7489F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9635F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.7489F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r26",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9635F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r27",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r28",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1781F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r29",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		wheel2.addOrReplaceChild("cube_r30",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition wheel3 = wheels.addOrReplaceChild("wheel3",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(58, 85).addBox(-0.25F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(9.0F, 0.0F, 11.0F));

		wheel3.addOrReplaceChild("cube_r31",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.7489F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r32",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r33",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9635F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r34",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r35",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r36",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r37",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r38",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r39",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.7489F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r40",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r41",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9635F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r42",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r43",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1781F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r44",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		wheel3.addOrReplaceChild("cube_r45",
				CubeListBuilder.create()
						.texOffs(0, 14).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition wheel4 = wheels.addOrReplaceChild("wheel4",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(58, 93).addBox(-1.75F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(-9.0F, 0.0F, 11.0F));

		wheel4.addOrReplaceChild("cube_r46",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.7489F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r47",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r48",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9635F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r49",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r50",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r51",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r52",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r53",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r54",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.7489F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r55",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r56",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9635F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r57",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r58",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1781F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r59",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		wheel4.addOrReplaceChild("cube_r60",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));


		total.addOrReplaceChild("base",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, 1.0F, -16.0F, 3.0F, 4.0F, 32.0F, CubeDeformation.NONE)
						.texOffs(40, 4).addBox(5.0F, 1.0F, -16.0F, 3.0F, 4.0F, 32.0F, CubeDeformation.NONE)
						.texOffs(79, 0).addBox(-5.0F, 3.0F, -16.0F, 10.0F, 2.0F, 32.0F, CubeDeformation.NONE)
						.texOffs(72, 76).addBox(-9.0F, 2.0F, -6.5F, 2.0F, 2.0F, 14.0F, new CubeDeformation(-0.75F))
						.texOffs(93, 62).addBox(7.0F, 2.0F, -6.5F, 2.0F, 2.0F, 14.0F, new CubeDeformation(-0.75F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -9.0F, 10.0F, 1.5708F, 0.0F, 0.0F));


		PartDefinition corn = body.addOrReplaceChild("corn",
				CubeListBuilder.create()
						.texOffs(0, 37).addBox(-4.5F, -28.0F, -2.5F, 9.0F, 29.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(41, 19).addBox(-3.5F, -28.75F, -1.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 3.0F, -2.0F));


		PartDefinition outer = body.addOrReplaceChild("outer",
				CubeListBuilder.create()
						.texOffs(99, 35).addBox(-5.0F, 4.25F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(-0.25F))
						.texOffs(106, 48).addBox(-4.0F, 4.5F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		outer.addOrReplaceChild("layer1",
				CubeListBuilder.create()
						.texOffs(10, 23).addBox(-5.25F, -22.0F, -5.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(38, 41).addBox(-5.25F, -19.0F, -5.0F, 1.0F, 24.0F, 10.0F, new CubeDeformation(-0.25F))
						.texOffs(20, 22).addBox(-5.25F, -20.5F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(10, 16).addBox(-5.25F, -20.5F, -5.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(22, 16).addBox(-5.25F, -22.0F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(11, 10).addBox(-5.25F, -23.5F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
						.texOffs(21, 10).addBox(-5.25F, -23.5F, 3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		outer.addOrReplaceChild("layer2",
				CubeListBuilder.create()
						.texOffs(10, 0).addBox(-5.25F, -22.0F, -5.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(60, 41).addBox(-5.25F, -19.0F, -5.0F, 1.0F, 24.0F, 10.0F, new CubeDeformation(-0.25F))
						.texOffs(20, 1).addBox(-5.25F, -20.5F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(41, 0).addBox(-5.25F, -20.5F, -5.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(54, 0).addBox(-5.25F, -22.0F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(40, 8).addBox(-5.25F, -23.5F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
						.texOffs(40, 14).addBox(-5.25F, -23.5F, 3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(9.5F, 0.0F, 0.0F));

		outer.addOrReplaceChild("layer3",
				CubeListBuilder.create()
						.texOffs(51, 8).addBox(-5.25F, -22.0F, -5.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(83, 40).addBox(-5.25F, -19.0F, -5.0F, 1.0F, 24.0F, 10.0F, new CubeDeformation(-0.25F))
						.texOffs(80, 0).addBox(-5.25F, -20.5F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(80, 8).addBox(-5.25F, -20.5F, -5.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(81, 16).addBox(-5.25F, -22.0F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(82, 24).addBox(-5.25F, -23.5F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
						.texOffs(92, 24).addBox(-5.25F, -23.5F, 3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));


		outer.addOrReplaceChild("layer4",
				CubeListBuilder.create()
						.texOffs(92, 16).addBox(-5.25F, -22.0F, -5.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(0, 76).addBox(-5.25F, -19.0F, -5.0F, 1.0F, 24.0F, 10.0F, new CubeDeformation(-0.25F))
						.texOffs(93, 0).addBox(-5.25F, -20.5F, 1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(92, 8).addBox(-5.25F, -20.5F, -5.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
						.texOffs(14, 77).addBox(-5.25F, -22.0F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F))
						.texOffs(0, 78).addBox(-5.25F, -23.5F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
						.texOffs(0, 38).addBox(-5.25F, -23.5F, 3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition leaves = outer.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leaf1 = leaves.addOrReplaceChild("leaf1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition l1 = leaf1.addOrReplaceChild("l1",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-4.25F, -22.5F, 4.25F, 0.0F, -0.7854F, 0.0F));

		l1.addOrReplaceChild("cube_r61",
				CubeListBuilder.create()
						.texOffs(25, 77).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		l1.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(23, 82).addBox(-1.0F, -1.2013F, -0.8827F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -0.5F, 1.5F));


		PartDefinition leaf2 = leaves.addOrReplaceChild("leaf2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition l2 = leaf2.addOrReplaceChild("l2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-4.25F, -22.5F, 4.25F, 0.0F, -0.7854F, 0.0F));

		l2.addOrReplaceChild("cube_r62",
				CubeListBuilder.create()
						.texOffs(23, 86).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		l2.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(24, 92).addBox(-1.0F, -1.2013F, -0.8827F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -0.5F, 1.5F));


		PartDefinition leaf3 = leaves.addOrReplaceChild("leaf3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition l3 = leaf3.addOrReplaceChild("l3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-4.25F, -22.5F, 4.25F, 0.0F, -0.7854F, 0.0F));

		l3.addOrReplaceChild("cube_r63",
				CubeListBuilder.create()
						.texOffs(24, 98).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		l3.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(24, 104).addBox(-1.0F, -1.2013F, -0.8827F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -0.5F, 1.5F));

		PartDefinition leaf4 = leaves.addOrReplaceChild("leaf4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition l4 = leaf4.addOrReplaceChild("l4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-4.25F, -22.5F, 4.25F, 0.0F, -0.7854F, 0.0F));

		l4.addOrReplaceChild("cube_r64",
				CubeListBuilder.create()
						.texOffs(37, 77).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		l4.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(35, 82).addBox(-1.0F, -1.2013F, -0.8827F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -0.5F, 1.5F));


		PartDefinition tail = body.addOrReplaceChild("tail",
				CubeListBuilder.create()
						.texOffs(49, 76).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.75F))
						.texOffs(45, 88).addBox(-1.0F, 1.2474F, -2.0557F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offset(0.0F, 6.0F, 0.0F));

		tail.addOrReplaceChild("cube_r65",
				CubeListBuilder.create()
						.texOffs(45, 94).addBox(-1.0F, -2.8192F, -1.5736F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 4.1305F, -0.6718F, -0.6109F, 0.0F, 0.0F));

		tail.addOrReplaceChild("cube_r66",
				CubeListBuilder.create()
						.texOffs(35, 102).addBox(-1.0F, -2.5F, -1.866F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 4.0681F, -1.2443F, -1.0472F, 0.0F, 0.0F));

		tail.addOrReplaceChild("cube_r67",
				CubeListBuilder.create()
						.texOffs(36, 94).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 2.5516F, -1.6933F, -1.6581F, 0.0F, 0.0F));

		tail.addOrReplaceChild("cube_r68",
				CubeListBuilder.create()
						.texOffs(34, 88).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 1.0382F, -2.3037F, 0.7418F, 0.0F, 0.0F));


		body.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(77, 79).addBox(-5.5F, -7.0F, 1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.45F))
						.texOffs(58, 101).addBox(4.5F, -7.0F, 1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.isPlayerRiding) {
			this.wheel1.xRot = state.ageInTicks / 2;
			this.wheel2.xRot = state.ageInTicks / 2;
			this.wheel3.xRot = state.ageInTicks / 2;
			this.wheel4.xRot = state.ageInTicks / 2;
		} else {
			this.wheel1.xRot = 0;
			this.wheel2.xRot = 0;
			this.wheel3.xRot = 0;
			this.wheel4.xRot = 0;
		}

		if (state.attackTime > 0) {
			int time = state.animCD - state.attackTime + 1;
			this.body.xRot = 1.5708F - AnimationUtil.getUpDown(time, state.animCD, 90);
			this.corn.visible = (time < state.animCD / 2);
		} else {
			this.corn.visible = (state.cornNum > 0);
			this.body.xRot = 1.5708F;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}