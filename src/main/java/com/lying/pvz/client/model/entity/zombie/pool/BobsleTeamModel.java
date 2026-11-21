package com.lying.pvz.client.model.entity.zombie.pool;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.PvZEntityModel;
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

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。雪橇车队模型（独立 PVZEntityModel，实现 IZombieModel，不继承 PVZZombieModel）：
//     total（根，setPos(0, 2, -8)，无方块）
//       ├─ z1 / z2 / z3 / z4（四个雪橇僵尸，各自 setPos(0, 4, 29/49/69/89)）
//       │    每个僵尸：head（10 方块）+ right_hand（1）+ left_hand（1）+ body（1）
//       │              + right_leg（2：腿 + 脚）+ left_leg（2：腿 + 脚）
//       │    静态旋转差异：
//       │      z1.head  Y -20°；z2.head2 Y 35°；z3.head3 X -20°；z4.head4 无旋转
//       │      四条腿均 X -75° + Y ±45°（屈膝坐姿）
//       │      z3.right_hand3 X -75° + Y 10°；z4.left_hand4 X -115°
//       ├─ mid（setPos(0, 8, 29)，6 个方块：雪橇底盘 + 4 个座位 + 尾板）
//       │    ├─ left（setPos(13, 4, 24)，1 个侧板）
//       │    └─ right（setPos(-13, 4, 25)，1 个侧板）
//       ├─ Head（setPos(0, 13, 4)，4 个方块：雪橇车头）
//       ├─ wheal / wheal2 / wheal3（setPos(13, 16, 6)）/ wheal4（setPos(13, 16, 81)）
//       │    每个轮子：7 个方块（轮毂）+ bone（X -40°，1 方块）+ bone2（X 40°，1 方块）
//       │    wheal2 / wheal4 的盒子坐标整体平移 -26；wheal3 的盒子坐标带 +70 左右平移
//       │    wheal3 的 bone5/bone6 盒子坐标带大数值偏移（-48.85/55.22、49.49/56.99）
//       └─ tail（setPos(0, 0, 96) + X 25°，2 个方块：尾翼）
//   setupAnim：空实现，原代码即如此。
//   tickPartAnim：空实现，原代码即如此。
//   renderBody：隐藏 z1~z4 后渲染 total（掉落身体时不显示僵尸）。
//   renderToBuffer：8 参数 (MatrixStack, IVertexBuilder, int, int, float×4)
//                   → 5 参数 (PoseStack, VertexConsumer, int, int, int color)
//   字段裁剪：setupAnim / tickPartAnim 均为空，renderBody 只访问 z1~z4，
//             renderToBuffer 只访问 total，因此只保留 total + z1~z4 共 5 个字段，
//             其余 40+ 个字段（mid/left/right/Head/wheal*/bone*/tail/各僵尸四肢）全部移除。

public class BobsleTeamModel extends PvZEntityModel<EntityRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bobsle_team"), "main");

	private final ModelPart total;
	private final ModelPart z1;
	private final ModelPart z2;
	private final ModelPart z3;
	private final ModelPart z4;

	public BobsleTeamModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.z1 = this.total.getChild("z1");
		this.z2 = this.total.getChild("z2");
		this.z3 = this.total.getChild("z3");
		this.z4 = this.total.getChild("z4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 2.0F, -8.0F));


		PartDefinition z1 = total.addOrReplaceChild("z1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.0F, 29.0F));

		z1.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(3, 192).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(109, 234).addBox(-7.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(59, 199).addBox(-7.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(100, 201).addBox(6.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(142, 200).addBox(6.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(187, 214).addBox(-6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 217).addBox(-6.0F, -10.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(181, 192).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 198).addBox(-5.0F, -11.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(134, 187).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		z1.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(77, 224).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-9.0F, -21.0F, 0.0F));

		z1.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(54, 227).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -21.0F, 0.0F));

		z1.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(5, 224).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -6.0F, 0.0F));

		z1.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(184, 229).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(149, 238).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, -1.309F, -0.7854F, 0.0F));

		z1.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(238, 228).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(203, 240).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -1.309F, 0.7854F, 0.0F));


		PartDefinition z2 = total.addOrReplaceChild("z2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.0F, 49.0F));

		z2.addOrReplaceChild("head2",
				CubeListBuilder.create()
						.texOffs(3, 192).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(109, 234).addBox(-7.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(59, 199).addBox(-7.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(100, 201).addBox(6.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(142, 200).addBox(6.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(187, 214).addBox(-6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 217).addBox(-6.0F, -10.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(181, 192).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 198).addBox(-5.0F, -11.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(134, 187).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, 0.0F, 0.6109F, 0.0F));

		z2.addOrReplaceChild("right_hand2",
				CubeListBuilder.create()
						.texOffs(77, 224).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-9.0F, -21.0F, 0.0F));

		z2.addOrReplaceChild("left_hand2",
				CubeListBuilder.create()
						.texOffs(54, 227).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -21.0F, 0.0F));

		z2.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(5, 224).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -6.0F, 0.0F));

		z2.addOrReplaceChild("right_leg2",
				CubeListBuilder.create()
						.texOffs(184, 229).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(149, 238).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, -1.309F, -0.7854F, 0.0F));

		z2.addOrReplaceChild("left_leg2",
				CubeListBuilder.create()
						.texOffs(238, 228).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(203, 240).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -1.309F, 0.7854F, 0.0F));

		PartDefinition z3 = total.addOrReplaceChild("z3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.0F, 69.0F));

		z3.addOrReplaceChild("head3",
				CubeListBuilder.create()
						.texOffs(3, 192).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(109, 234).addBox(-7.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(59, 199).addBox(-7.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(100, 201).addBox(6.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(142, 200).addBox(6.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(187, 214).addBox(-6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 217).addBox(-6.0F, -10.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(181, 192).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 198).addBox(-5.0F, -11.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(134, 187).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

		z3.addOrReplaceChild("right_hand3",
				CubeListBuilder.create()
						.texOffs(77, 224).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-9.0F, -21.0F, 0.0F, -1.309F, 0.1745F, 0.0F));

		z3.addOrReplaceChild("left_hand3",
				CubeListBuilder.create()
						.texOffs(54, 227).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -21.0F, 0.0F));

		z3.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(5, 224).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -6.0F, 0.0F));

		z3.addOrReplaceChild("right_leg3",
				CubeListBuilder.create()
						.texOffs(184, 229).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(149, 238).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, -1.309F, -0.7854F, 0.0F));

		z3.addOrReplaceChild("left_leg3",
				CubeListBuilder.create()
						.texOffs(238, 228).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(203, 240).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -1.309F, 0.7854F, 0.0F));


		PartDefinition z4 = total.addOrReplaceChild("z4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.0F, 89.0F));

		z4.addOrReplaceChild("head4",
				CubeListBuilder.create()
						.texOffs(3, 192).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(109, 234).addBox(-7.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(59, 199).addBox(-7.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(100, 201).addBox(6.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(142, 200).addBox(6.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(187, 214).addBox(-6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 217).addBox(-6.0F, -10.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(181, 192).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 198).addBox(-5.0F, -11.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(134, 187).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -23.0F, 0.0F));

		z4.addOrReplaceChild("right_hand4",
				CubeListBuilder.create()
						.texOffs(77, 224).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-9.0F, -21.0F, 0.0F));

		z4.addOrReplaceChild("left_hand4",
				CubeListBuilder.create()
						.texOffs(54, 227).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -21.0F, 0.0F, -2.0071F, 0.0F, 0.0F));

		z4.addOrReplaceChild("body4",
				CubeListBuilder.create()
						.texOffs(5, 224).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -6.0F, 0.0F));

		z4.addOrReplaceChild("right_leg4",
				CubeListBuilder.create()
						.texOffs(184, 229).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(149, 238).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, -1.309F, -0.7854F, 0.0F));

		z4.addOrReplaceChild("left_leg4",
				CubeListBuilder.create()
						.texOffs(238, 228).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 17.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(203, 240).addBox(-3.0F, 17.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -1.309F, 0.7854F, 0.0F));

		PartDefinition mid = total.addOrReplaceChild("mid",
				CubeListBuilder.create()
						.texOffs(284, 421).addBox(-14.0F, 4.0F, -17.0F, 28.0F, 1.0F, 84.0F, CubeDeformation.NONE)
						.texOffs(459, 402).addBox(-8.0F, -4.0F, -3.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(402, 399).addBox(-8.0F, -4.0F, 17.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(463, 372).addBox(-8.0F, -4.0F, 37.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(402, 368).addBox(-8.0F, -4.0F, 57.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(440, 350).addBox(-14.0F, -2.0F, 67.0F, 28.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 8.0F, 29.0F));

		mid.addOrReplaceChild("left",
				CubeListBuilder.create()
						.texOffs(9, 399).addBox(1.0F, -24.0F, -41.0F, 1.0F, 25.0F, 84.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 4.0F, 24.0F));


		mid.addOrReplaceChild("right",
				CubeListBuilder.create()
						.texOffs(336, 7).addBox(-2.0F, -24.0F, -42.0F, 1.0F, 25.0F, 84.0F, CubeDeformation.NONE),
				PartPose.offset(-13.0F, 4.0F, 25.0F));


		total.addOrReplaceChild("Head",
				CubeListBuilder.create()
						.texOffs(188, 449).addBox(-15.0F, -25.0F, -12.0F, 30.0F, 25.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(129, 436).addBox(-13.0F, -15.0F, -17.0F, 26.0F, 15.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(13, 378).addBox(-10.0F, -7.0F, -22.0F, 20.0F, 7.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(11, 340).addBox(-15.0F, -27.0F, -2.0F, 30.0F, 2.0F, 21.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 13.0F, 4.0F));


		PartDefinition wheal = total.addOrReplaceChild("wheal",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 16.0F, 6.0F));


		wheal.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));

		wheal.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));

		PartDefinition wheal2 = total.addOrReplaceChild("wheal2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-27.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 16.0F, 6.0F));

		wheal2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));

		wheal2.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		PartDefinition wheal3 = total.addOrReplaceChild("wheal3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, 70.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 68.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 84.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 85.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 76.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 67.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, 3.0F, 65.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 16.0F, 6.0F));


		wheal3.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -48.8519F, 55.2194F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));

		wheal3.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 49.4946F, 56.9854F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		PartDefinition wheal4 = total.addOrReplaceChild("wheal4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-27.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 16.0F, 81.0F));

		wheal4.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));

		wheal4.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		total.addOrReplaceChild("tail",
				CubeListBuilder.create()
						.texOffs(261, 382).addBox(14.0F, -11.0F, 0.0F, 1.0F, 11.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(184, 378).addBox(-15.0F, -11.0F, 0.0F, 1.0F, 11.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 96.0F, 0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 512);
	}


	@Override
	public void setupAnim(EntityRenderState state) {
		super.setupAnim(state);
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
							 float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector,
						   RenderType renderType, int packedLight, int packedOverlay) {
		this.z1.visible = false;
		this.z2.visible = false;
		this.z3.visible = false;
		this.z4.visible = false;
	}

	@Override
	public EntityModel<? extends EntityRenderState> getZombieModel() {
		return this;
	}

}