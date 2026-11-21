package com.lying.pvz.client.model.entity.plant.spear;

import java.util.Optional;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.spear.CactusEntity;
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
//   几何 1:1 搬运。仙人掌（Cactus）全项目最大模型之一，90+ 部件。
//   结构总览：
//     total
//       ├─ body（2 个方块 + 子树）
//       │   ├─ branch（5 个方块：躯干 + 4 面外皮）
//       │   ├─ mouse（7 个方块：刺嘴）
//       │   ├─ hat（2 个方块 + 8 个 cube_r 花冠 + 3 个 small 花蕊）
//       │   ├─ spikes3（7 个 boneN 普通尖刺）
//       │   └─ blue_spikes3（7 个 boneN 蓝色尖刺）
//       ├─ right_hand（5 方块 + spikes1 7 组 + blue_spikes1 7 组）
//       ├─ left_hand（5 方块 + spikes2 7 组 + blue_spikes2 7 组，绕 Y 轴 π 镜像）
//       ├─ body2（3 方块 + spikes4 7 组 + blue_spikes4 7 组）
//       ├─ body3（3 方块 + spikes5 7 组 + blue_spikes5 7 组）
//       ├─ body4（3 方块 + spikes6 7 组 + blue_spikes6 7 组）
//       └─ body5（3 方块 + spikes7 7 组 + blue_spikes7 7 组）
//
//   ⚠️ 尖刺组是"普通 spikesN" 与 "蓝色 blue_spikesN" 完全同形、同位置的两套几何，
//      靠 setupAnim 切 visible 实现"充能变色"。7 组 × (7 bone + 7 blue bone) = 98 个 bone。
//      逐条展开保留（虽然后 6 组几何与前 1 组完全相同，但纹理偏移不同）。
//
//   setupAnim 逻辑：
//     1. body2~5 按 cactusHeight / SEGMENT_HEIGHT 逐级显示（长高的仙人掌）
//     2. spikesN 与 blue_spikesN 按 isCactusPowered() 互斥切换
//     3. 注意：spikes4~7 的可见性只在对应 bodyN 可见时才更新
//        —— 这是 1.16.5 的写法（避免不可见 body 的 spike 残留 visible=true），保留
//     4. 原版还注释掉了 super.setupAnim(...) 调用，迁移后保留该注释
//
//   继承 PlantShooterModel<CactusEntity>，需要重写：
//     - getBodyModel()：返回 Optional.ofNullable(this.total) —— **注意是 total 不是 body**（原版就是如此）
//     - getMaxRotAngle()：返回 10F

public class CactusModel extends PlantShooterModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("cactus"), "main");

	private final ModelPart total;
	private final ModelPart body2;
	private final ModelPart body3;
	private final ModelPart body4;
	private final ModelPart body5;
	private final ModelPart spikes1;
	private final ModelPart blue_spikes1;
	private final ModelPart spikes2;
	private final ModelPart blue_spikes2;
	private final ModelPart spikes3;
	private final ModelPart blue_spikes3;
	private final ModelPart spikes4;
	private final ModelPart blue_spikes4;
	private final ModelPart spikes5;
	private final ModelPart blue_spikes5;
	private final ModelPart spikes6;
	private final ModelPart blue_spikes6;
	private final ModelPart spikes7;
	private final ModelPart blue_spikes7;

	public CactusModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		final ModelPart body = this.total.getChild("body");
		this.spikes3 = body.getChild("spikes3");
		this.blue_spikes3 = body.getChild("blue_spikes3");
		final ModelPart right_hand = this.total.getChild("right_hand");
		this.spikes1 = right_hand.getChild("spikes1");
		this.blue_spikes1 = right_hand.getChild("blue_spikes1");
		final ModelPart left_hand = this.total.getChild("left_hand");
		this.spikes2 = left_hand.getChild("spikes2");
		this.blue_spikes2 = left_hand.getChild("blue_spikes2");
		this.body2 = this.total.getChild("body2");
		this.spikes4 = this.body2.getChild("spikes4");
		this.blue_spikes4 = this.body2.getChild("blue_spikes4");
		this.body3 = this.total.getChild("body3");
		this.spikes5 = this.body3.getChild("spikes5");
		this.blue_spikes5 = this.body3.getChild("blue_spikes5");
		this.body4 = this.total.getChild("body4");
		this.spikes6 = this.body4.getChild("spikes6");
		this.blue_spikes6 = this.body4.getChild("blue_spikes6");
		this.body5 = this.total.getChild("body5");
		this.spikes7 = this.body5.getChild("spikes7");
		this.blue_spikes7 = this.body5.getChild("blue_spikes7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(47, 81).addBox(-4.0F, -28.0F, -5.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(16, 73).addBox(0.0F, -28.0F, -5.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		body.addOrReplaceChild("branch",
				CubeListBuilder.create()
						.texOffs(0, 97).addBox(-4.0F, -31.0F, -5.0F, 8.0F, 30.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(18, 88).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 32.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(50, 97).addBox(-4.0F, -31.0F, 4.0F, 8.0F, 30.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(68, 90).addBox(-5.0F, -31.0F, -4.0F, 1.0F, 30.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(86, 90).addBox(4.0F, -31.0F, -4.0F, 1.0F, 30.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		body.addOrReplaceChild("mouse",
				CubeListBuilder.create()
						.texOffs(0, 90).addBox(-1.5F, -22.5F, -11.5F, 3.0F, 3.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 82).addBox(-2.5F, -22.5F, -12.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(-0.5F))
						.texOffs(20, 78).addBox(-1.5F, -23.5F, -12.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.5F))
						.texOffs(42, 89).addBox(-2.5F, -23.5F, -13.5F, 5.0F, 5.0F, 2.0F, new CubeDeformation(-0.1F))
						.texOffs(54, 83).addBox(-3.5F, -23.5F, -14.002F, 7.0F, 5.0F, 3.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 72).addBox(-2.5F, -24.5F, -14.001F, 5.0F, 7.0F, 3.0F, new CubeDeformation(-0.5F))
						.texOffs(36, 83).addBox(-2.0F, -23.0F, -14.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offset(0.0F, 0.5F, 3.0F));

		PartDefinition hat = body.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(116, 27).addBox(-1.25F, -2.0F, -1.25F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(98, 32).addBox(-1.25F, -5.0F, -1.25F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(-0.25F, -32.75F, -0.5F));


		hat.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(116, 23).addBox(-1.5F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.75F, -4.0F, 0.75F, -1.0472F, 0.0F, 0.0F));

		hat.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(104, 26).addBox(-2.0F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.75F, -2.0F, 1.75F, -1.0472F, 0.0F, 0.0F));

		hat.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(102, 29).addBox(-1.5F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.75F, -4.0F, -0.25F, 1.0908F, 0.0F, 0.0F));

		hat.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(93, 31).addBox(-2.0F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.75F, -2.0F, -1.25F, 1.1345F, 0.0F, 0.0F));


		hat.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(120, 24).addBox(-0.7F, -1.0F, -0.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-1.25F, -4.0F, -0.25F, 0.0F, 0.0F, 0.5672F));

		hat.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(109, 26).addBox(-0.7F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-2.25F, -2.0F, -0.25F, 0.0F, 0.0F, 0.5672F));

		hat.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(108, 30).addBox(-0.5F, -0.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.75F, -4.0F, 0.75F, 0.0F, 0.0F, -0.5672F));

		hat.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(88, 34).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(1.75F, -2.0F, -0.25F, 0.0F, 0.0F, -0.5672F));

		hat.addOrReplaceChild("small1",
				CubeListBuilder.create()
						.texOffs(32, 59).addBox(-0.25F, -2.0F, -0.75F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F))
						.texOffs(18, 67).addBox(-0.25F, -2.0F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, 0.2618F, 0.0F, 0.0F));

		hat.addOrReplaceChild("small2",
				CubeListBuilder.create()
						.texOffs(37, 59).addBox(-0.75F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
						.texOffs(42, 57).addBox(-0.75F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, -4.5F, 0.25F, -0.3927F, -0.4363F, 0.0F));

		hat.addOrReplaceChild("small3",
				CubeListBuilder.create()
						.texOffs(49, 67).addBox(-0.25F, -2.0F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
						.texOffs(121, 20).addBox(-0.25F, -2.0F, -0.25F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.5F, -4.5F, 0.5F, -0.3927F, 0.3927F, 0.0F));


		PartDefinition spikes3 = body.addOrReplaceChild("spikes3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		spikes3.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(31, 78).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(35, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(3.0F, -29.0F, -3.0F, 0.5236F, 0.0F, 0.7854F));

		spikes3.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		spikes3.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(8, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		spikes3.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(26, 74).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		spikes3.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(70, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(65, 92).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		spikes3.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(57, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(39, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		spikes3.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(78, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 95).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition blue_spikes3 = body.addOrReplaceChild("blue_spikes3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		blue_spikes3.addOrReplaceChild("bone46",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(3.0F, -29.0F, -3.0F, 0.5236F, 0.0F, 0.7854F));

		blue_spikes3.addOrReplaceChild("bone47",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		blue_spikes3.addOrReplaceChild("bone48",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		blue_spikes3.addOrReplaceChild("bone49",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		blue_spikes3.addOrReplaceChild("bone50",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		blue_spikes3.addOrReplaceChild("bone51",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		blue_spikes3.addOrReplaceChild("bone52",
				CubeListBuilder.create()
						.texOffs(1, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition right_hand = total.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(97, 65).addBox(-9.0F, -2.0F, -3.5F, 9.0F, 4.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(94, 75).addBox(-10.0F, -3.0F, -3.5F, 11.0F, 6.0F, 6.0F, new CubeDeformation(-0.5F))
						.texOffs(104, 108).addBox(-11.0F, -12.0F, -3.5F, 6.0F, 14.0F, 6.0F, new CubeDeformation(-0.6F))
						.texOffs(104, 87).addBox(-11.0F, -13.0F, -3.5F, 6.0F, 15.0F, 6.0F, new CubeDeformation(-1.0F))
						.texOffs(69, 61).addBox(-12.0F, -12.0F, -3.5F, 8.0F, 14.0F, 6.0F, new CubeDeformation(-1.0F)),
				PartPose.offset(-5.0F, -17.0F, 0.0F));

		PartDefinition spikes1 = right_hand.addOrReplaceChild("spikes1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		spikes1.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(105, 109).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(105, 89).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-9.0F, -11.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		spikes1.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(98, 92).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(98, 88).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -7.0F, 1.0F, 0.0F, 0.0F, -1.1781F));

		spikes1.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(87, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(89, 89).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -8.0F, -1.0F, 0.7854F, 0.0F, 0.5236F));

		spikes1.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(83, 89).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(78, 90).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -3.0F, -1.0F, 0.0F, 0.0F, -2.0944F));

		spikes1.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(90, 85).addBox(-4.8301F, -5.5F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(90, 82).addBox(-4.8301F, -4.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, -3.0F, -1.0F, 0.2182F, 0.0F, -2.3998F));

		spikes1.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(86, 82).addBox(-4.8301F, -6.5F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 86).addBox(-4.8301F, -4.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -3.0F, -1.0F, -0.4363F, 0.0F, -2.6616F));

		spikes1.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(82, 81).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(78, 87).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -6.0F, 1.0F, -0.7854F, 0.0F, 0.2618F));

		PartDefinition blue_spikes1 = right_hand.addOrReplaceChild("blue_spikes1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		blue_spikes1.addOrReplaceChild("bone53",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-9.0F, -11.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		blue_spikes1.addOrReplaceChild("bone54",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -7.0F, 1.0F, 0.0F, 0.0F, -1.1781F));

		blue_spikes1.addOrReplaceChild("bone55",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -8.0F, -1.0F, 0.7854F, 0.0F, 0.5236F));

		blue_spikes1.addOrReplaceChild("bone56",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -3.0F, -1.0F, 0.0F, 0.0F, -2.0944F));

		blue_spikes1.addOrReplaceChild("bone57",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-4.8301F, -5.5F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-4.8301F, -4.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, -3.0F, -1.0F, 0.2182F, 0.0F, -2.3998F));

		blue_spikes1.addOrReplaceChild("bone58",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-4.8301F, -6.5F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-4.8301F, -4.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -3.0F, -1.0F, -0.4363F, 0.0F, -2.6616F));

		blue_spikes1.addOrReplaceChild("bone59",
				CubeListBuilder.create()
						.texOffs(1, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -6.0F, 1.0F, -0.7854F, 0.0F, 0.2618F));


		PartDefinition left_hand = total.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(39, 70).addBox(-9.0F, -2.0F, -3.5F, 9.0F, 4.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(94, 53).addBox(-10.0F, -3.0F, -3.5F, 11.0F, 6.0F, 6.0F, new CubeDeformation(-0.5F))
						.texOffs(104, 33).addBox(-11.0F, -12.0F, -3.5F, 6.0F, 14.0F, 6.0F, new CubeDeformation(-0.6F))
						.texOffs(75, 38).addBox(-11.0F, -13.0F, -3.5F, 6.0F, 15.0F, 6.0F, new CubeDeformation(-1.0F))
						.texOffs(47, 46).addBox(-12.0F, -12.0F, -3.5F, 8.0F, 14.0F, 6.0F, new CubeDeformation(-1.0F)),
				PartPose.offsetAndRotation(5.0F, -17.0F, -1.0F, 0.0F, 3.1416F, 0.0F));


		PartDefinition spikes2 = left_hand.addOrReplaceChild("spikes2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		spikes2.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(39, 70).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(34, 75).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, -10.0F, 1.0F, 0.0F, 0.0F, 0.2618F));

		spikes2.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(34, 70).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(30, 71).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -8.0F, 0.0F, -0.3927F, 0.0F, -1.0472F));

		spikes2.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(43, 65).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(38, 67).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -8.0F, -2.0F, 0.7854F, 0.0F, 0.5236F));

		spikes2.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(26, 69).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(21, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 1.0F, -2.618F, 0.0F, -0.5236F));

		spikes2.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(64, 69).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(32, 68).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, 0.0F, 1.0F, -2.618F, 0.0F, 0.0F));

		spikes2.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(38, 62).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(33, 64).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, 0.0F, -1.0F, -2.0944F, -1.309F, 0.0F));

		spikes2.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(29, 64).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(23, 66).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-3.0F, 1.0F, -2.0F, -1.309F, 2.8798F, 0.0F));


		PartDefinition blue_spikes2 = left_hand.addOrReplaceChild("blue_spikes2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		blue_spikes2.addOrReplaceChild("bone60",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, -10.0F, 1.0F, 0.0F, 0.0F, 0.2618F));

		blue_spikes2.addOrReplaceChild("bone61",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, -8.0F, 0.0F, -0.3927F, 0.0F, -1.0472F));

		blue_spikes2.addOrReplaceChild("bone62",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-7.0F, -8.0F, -2.0F, 0.7854F, 0.0F, 0.5236F));

		blue_spikes2.addOrReplaceChild("bone63",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 1.0F, -2.618F, 0.0F, -0.5236F));

		blue_spikes2.addOrReplaceChild("bone64",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-8.0F, 0.0F, 1.0F, -2.618F, 0.0F, 0.0F));

		blue_spikes2.addOrReplaceChild("bone65",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-10.0F, 0.0F, -1.0F, -2.0944F, -1.309F, 0.0F));

		blue_spikes2.addOrReplaceChild("bone66",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-3.0F, 1.0F, -2.0F, -1.309F, 2.8798F, 0.0F));


		PartDefinition body2 = total.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(2, 106).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 10.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(26, 110).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 107).addBox(-4.0F, -1.0F, 4.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spikes4 = body2.addOrReplaceChild("spikes4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		spikes4.addOrReplaceChild("bone23",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		spikes4.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(8, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		spikes4.addOrReplaceChild("bone25",
				CubeListBuilder.create()
						.texOffs(26, 74).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		spikes4.addOrReplaceChild("bone26",
				CubeListBuilder.create()
						.texOffs(70, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(65, 92).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		spikes4.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(57, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(39, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		spikes4.addOrReplaceChild("bone28",
				CubeListBuilder.create()
						.texOffs(78, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 95).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));

		PartDefinition blue_spikes4 = body2.addOrReplaceChild("blue_spikes4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		blue_spikes4.addOrReplaceChild("bone67",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		blue_spikes4.addOrReplaceChild("bone68",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		blue_spikes4.addOrReplaceChild("bone69",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		blue_spikes4.addOrReplaceChild("bone70",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		blue_spikes4.addOrReplaceChild("bone71",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		blue_spikes4.addOrReplaceChild("bone72",
				CubeListBuilder.create()
						.texOffs(2, 60).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 60).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));

		PartDefinition body3 = total.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(2, 106).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 10.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(26, 110).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 107).addBox(-4.0F, -1.0F, 4.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 10.0F, 0.0F));


		PartDefinition spikes5 = body3.addOrReplaceChild("spikes5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		spikes5.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		spikes5.addOrReplaceChild("bone29",
				CubeListBuilder.create()
						.texOffs(8, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		spikes5.addOrReplaceChild("bone30",
				CubeListBuilder.create()
						.texOffs(26, 74).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		spikes5.addOrReplaceChild("bone31",
				CubeListBuilder.create()
						.texOffs(70, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(65, 92).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		spikes5.addOrReplaceChild("bone32",
				CubeListBuilder.create()
						.texOffs(57, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(39, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		spikes5.addOrReplaceChild("bone33",
				CubeListBuilder.create()
						.texOffs(78, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 95).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));

		PartDefinition blue_spikes5 = body3.addOrReplaceChild("blue_spikes5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		blue_spikes5.addOrReplaceChild("bone73",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		blue_spikes5.addOrReplaceChild("bone74",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		blue_spikes5.addOrReplaceChild("bone75",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		blue_spikes5.addOrReplaceChild("bone76",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		blue_spikes5.addOrReplaceChild("bone77",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		blue_spikes5.addOrReplaceChild("bone78",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition body4 = total.addOrReplaceChild("body4",
				CubeListBuilder.create()
						.texOffs(2, 106).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 10.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(26, 110).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 107).addBox(-4.0F, -1.0F, 4.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 20.0F, 0.0F));


		PartDefinition spikes6 = body4.addOrReplaceChild("spikes6",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.9163F, 0.0F));

		spikes6.addOrReplaceChild("bone34",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		spikes6.addOrReplaceChild("bone35",
				CubeListBuilder.create()
						.texOffs(8, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		spikes6.addOrReplaceChild("bone36",
				CubeListBuilder.create()
						.texOffs(26, 74).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		spikes6.addOrReplaceChild("bone37",
				CubeListBuilder.create()
						.texOffs(70, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(65, 92).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		spikes6.addOrReplaceChild("bone38",
				CubeListBuilder.create()
						.texOffs(57, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(39, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		spikes6.addOrReplaceChild("bone39",
				CubeListBuilder.create()
						.texOffs(78, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 95).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition blue_spikes6 = body4.addOrReplaceChild("blue_spikes6",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.9163F, 0.0F));

		blue_spikes6.addOrReplaceChild("bone79",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		blue_spikes6.addOrReplaceChild("bone80",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		blue_spikes6.addOrReplaceChild("bone81",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		blue_spikes6.addOrReplaceChild("bone82",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		blue_spikes6.addOrReplaceChild("bone83",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		blue_spikes6.addOrReplaceChild("bone84",
				CubeListBuilder.create()
						.texOffs(2, 58).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition body5 = total.addOrReplaceChild("body5",
				CubeListBuilder.create()
						.texOffs(2, 106).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 10.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(26, 110).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 107).addBox(-4.0F, -1.0F, 4.0F, 8.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 30.0F, 0.0F));


		PartDefinition spikes7 = body5.addOrReplaceChild("spikes7",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		spikes7.addOrReplaceChild("bone40",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		spikes7.addOrReplaceChild("bone41",
				CubeListBuilder.create()
						.texOffs(8, 67).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 69).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		spikes7.addOrReplaceChild("bone42",
				CubeListBuilder.create()
						.texOffs(26, 74).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 70).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		spikes7.addOrReplaceChild("bone43",
				CubeListBuilder.create()
						.texOffs(70, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(65, 92).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		spikes7.addOrReplaceChild("bone44",
				CubeListBuilder.create()
						.texOffs(57, 91).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(39, 80).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		spikes7.addOrReplaceChild("bone45",
				CubeListBuilder.create()
						.texOffs(78, 93).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(82, 95).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));


		PartDefinition blue_spikes7 = body5.addOrReplaceChild("blue_spikes7",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		blue_spikes7.addOrReplaceChild("bone85",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.7854F, 0.0F, 1.0472F));

		blue_spikes7.addOrReplaceChild("bone86",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 1.309F));

		blue_spikes7.addOrReplaceChild("bone87",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-2.0F, -6.0F, -4.0F, 2.1817F, -0.5236F, 1.309F));

		blue_spikes7.addOrReplaceChild("bone88",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -23.0F, 4.0F, -1.309F, 0.0F, 0.2618F));

		blue_spikes7.addOrReplaceChild("bone89",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 4.0F, -1.309F, 0.0F, -2.3126F));

		blue_spikes7.addOrReplaceChild("bone90",
				CubeListBuilder.create()
						.texOffs(2, 59).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 3.0107F, -0.5236F, 1.309F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);

		final float height = state.cactusHeight;
		final boolean powered = state.isCactusPowered;

		this.body2.visible = height > 0;
		this.body3.visible = height > CactusEntity.SEGMENT_HEIGHT;
		this.body4.visible = height > 2 * CactusEntity.SEGMENT_HEIGHT;
		this.body5.visible = height > 3 * CactusEntity.SEGMENT_HEIGHT;


		this.spikes1.visible = !powered;
		this.blue_spikes1.visible = powered;
		this.spikes2.visible = !powered;
		this.blue_spikes2.visible = powered;
		this.spikes3.visible = !powered;
		this.blue_spikes3.visible = powered;


		if (this.body2.visible) {
			this.spikes4.visible = !powered;
			this.blue_spikes4.visible = powered;
		}
		if (this.body3.visible) {
			this.spikes5.visible = !powered;
			this.blue_spikes5.visible = powered;
		}
		if (this.body4.visible) {
			this.spikes6.visible = !powered;
			this.blue_spikes6.visible = powered;
		}
		if (this.body5.visible) {
			this.spikes7.visible = !powered;
			this.blue_spikes7.visible = powered;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}


	@Override
	public Optional<ModelPart> getBodyModel() {
		return Optional.ofNullable(this.total);
	}

	@Override
	public float getMaxRotAngle() {
		return 10F;
	}
}