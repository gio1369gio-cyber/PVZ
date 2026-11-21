package com.lying.pvz.client.model.entity.zombie.roof;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.PvZEntityModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.roof.GargantuarEntity;
import com.lying.pvz.utils.AnimationUtil;
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
//   1. 几何 1:1 搬运。巨人僵尸（Gargantuar）全项目最大僵尸模型，30+ 部件：
//        total
//          ├─ left_leg / right_leg（各 2 方块）
//          ├─ up (0,-39,0) —— 上半身容器
//          │   ├─ back (0,-25,23) —— 背包（5 方块 + imp 挂点）
//          │   │   └─ imp：背后"小鬼僵尸"（含独立的 leg/arm/head）
//          │   ├─ head (0,-40,-11)
//          │   │   ├─ angry（愤怒眼）
//          │   │   ├─ bone3（头顶刺，绕 X 70°）
//          │   │   └─ hurt2（hurt21/22/23 三个小伤害标记）
//          │   ├─ right_arm / left_arm（各 1 主方块）
//          │   │   └─ left_arm 下挂：hurt1 / pole / sign / doll
//          │   └─ body (0,39,0) —— 躯干
//   2. 三种武器（POLE/SIGN/DOLL）通过 setupAnim 的 `getToolType()` 切换 visible；
//      每把武器都是一大套子结构（pole 7 方块、sign 9 方块 + bone、doll 一整套"小僵尸模型"）。
//   3. hurt1/hurt2 是"血量分级伤害标记"（<2/3 显示 hurt1，<1/3 显示 hurt2）。
//   4. ⚠️ 多处使用 `xRot` / `yRot` 中把 degree 直接当弧度用（如 `netHeadYaw / (180F/π)`）——
//        这是 1.16.5 就有的写法，1.21.1 的 ModelPart.xRot 同样是弧度制，保持原样。
//   5. ⚠️ MatrixStack → PoseStack、IVertexBuilder → VertexConsumer、
//        renderToBuffer 末尾颜色参数从 float×4 改为 int。
//   6. setupAnim 三态：
//        - getAttackTime() == 0：走路摆动（leg/arm 用 cos(limbSwing * 0.6662F)）
//        - getAttackTime() > 0：普通砸地攻击（left_arm 分两段，up 上下摆）
//        - getAttackTime() < 0：投掷（right_arm 分两段，用 getUp/getDown 而非 getUpDown）
//   7. tickPartAnim：死亡动画三阶段（每 40 tick 一段 + 最后 getUp 倒地）。
//      在 1.21.1 中此方法由 IZombieModel 接口在每 tick 调用，保持原样。
//   8. renderBody：外部渲染"僵尸身体"时隐藏所有工具/愤怒/imp，仅暴露 total —— 用于光照/阴影通道。

public class GargantuarModel extends PvZEntityModel<PvZZombieRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gargantuar"), "main");

	private final ModelPart total;
	// 1.16.5: setupAnim / tickPartAnim / renderBody 里被驱动的部件
	// 1.21.1: 字段类型 ModelRenderer → ModelPart
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart back;
	private final ModelPart imp;
	private final ModelPart head;
	private final ModelPart angry;
	private final ModelPart hurt2;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart hurt1;
	private final ModelPart pole;
	private final ModelPart sign;
	private final ModelPart doll;

	public GargantuarModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.back = this.up.getChild("back");
		this.imp = this.back.getChild("imp");
		this.head = this.up.getChild("head");
		this.angry = this.head.getChild("angry");
		this.hurt2 = this.head.getChild("hurt2");
		this.right_arm = this.up.getChild("right_arm");
		this.left_arm = this.up.getChild("left_arm");
		this.hurt1 = this.left_arm.getChild("hurt1");
		this.pole = this.left_arm.getChild("pole");
		this.sign = this.left_arm.getChild("sign");
		this.doll = this.left_arm.getChild("doll");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(199, 235).addBox(-6.0F, 36.0F, -9.0F, 12.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(3, 205).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 40.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(7.0F, -39.0F, 1.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(198, 211).addBox(-5.0F, 36.0F, -9.0F, 12.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(47, 204).addBox(-4.0F, -4.0F, -5.0F, 10.0F, 40.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(-8.0F, -39.0F, 1.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -39.0F, 0.0F));


		PartDefinition back = up.addOrReplaceChild("back",
				CubeListBuilder.create()
						.texOffs(93, 236).addBox(-8.0F, -15.0F, -10.0F, 16.0F, 17.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(186, 186).addBox(-8.0F, 2.0F, -10.0F, 16.0F, 1.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(5, 167).addBox(8.0F, -15.0F, -10.0F, 1.0F, 17.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(50, 184).addBox(-8.0F, -15.0F, 7.0F, 16.0F, 17.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(94, 192).addBox(-9.0F, -15.0F, -10.0F, 1.0F, 17.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 23.0F));


		PartDefinition imp = back.addOrReplaceChild("imp",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.0F, -1.0F));

		imp.addOrReplaceChild("left_leg3",
				CubeListBuilder.create()
						.texOffs(408, 42).addBox(-1.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, -12.0F, 0.0F));

		imp.addOrReplaceChild("right_leg3",
				CubeListBuilder.create()
						.texOffs(408, 24).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, -12.0F, 0.0F));

		PartDefinition up3 = imp.addOrReplaceChild("up3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -12.0F, 0.0F));

		up3.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(408, 4).addBox(-5.0F, -16.0F, -2.0F, 10.0F, 15.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm2 = up3.addOrReplaceChild("right_arm2",
				CubeListBuilder.create(),
				PartPose.offset(-7.0F, -14.0F, 0.0F));

		right_arm2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(455, 29).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition left_arm2 = up3.addOrReplaceChild("left_arm2",
				CubeListBuilder.create(),
				PartPose.offset(7.0F, -14.0F, 0.0F));

		left_arm2.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(437, 19).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		up3.addOrReplaceChild("head3",
				CubeListBuilder.create()
						.texOffs(424, 39).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(192, 148).addBox(-8.0F, -8.0F, -14.0F, 16.0F, 17.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -40.0F, -11.0F));

		head.addOrReplaceChild("angry",
				CubeListBuilder.create()
						.texOffs(3, 140).addBox(2.0F, -6.0F, -14.05F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.02F))
						.texOffs(28, 141).addBox(-6.0F, -5.0F, -14.05F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.02F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		head.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(4, 160).addBox(-8.0F, -1.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 152).addBox(-9.0F, -1.0F, -12.0F, 18.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(49, 158).addBox(-9.0F, -1.0F, -11.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(47, 140).addBox(8.0F, -1.0F, -11.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.2217F, 0.0F, 0.0F));

		PartDefinition hurt2 = head.addOrReplaceChild("hurt2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		hurt2.addOrReplaceChild("hurt21",
				CubeListBuilder.create()
						.texOffs(240, 239).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(242, 242).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(8.5F, 2.0F, -8.75F, -0.7854F, 0.0F, 0.0F));

		hurt2.addOrReplaceChild("hurt22",
				CubeListBuilder.create()
						.texOffs(207, 239).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(242, 231).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-7.5F, -5.0F, -3.25F, -0.7854F, 0.0F, 0.0F));

		hurt2.addOrReplaceChild("hurt23",
				CubeListBuilder.create()
						.texOffs(208, 231).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(189, 240).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0F, -8.5F, -10.75F, -0.3927F, 0.0F, -1.5708F));


		up.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(100, 25).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 52.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(-19.0F, -34.0F, 1.0F));

		PartDefinition left_arm = up.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(207, 79).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 52.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(19.0F, -34.0F, 1.0F));


		PartDefinition hurt1 = left_arm.addOrReplaceChild("hurt1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		hurt1.addOrReplaceChild("hurt11",
				CubeListBuilder.create()
						.texOffs(0, 203).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(193, 239).addBox(-1.0F, -1.0F, -5.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(6.5F, 14.0F, 0.0F, -0.7854F, 0.0F, 0.0F));


		left_arm.addOrReplaceChild("pole",
				CubeListBuilder.create()
						.texOffs(0, 257).addBox(-2.0F, -3.0F, -50.0F, 4.0F, 4.0F, 72.0F, CubeDeformation.NONE)
						.texOffs(0, 300).addBox(-2.0F, -13.0F, -36.0F, 4.0F, 24.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(16, 307).addBox(-2.0F, -9.0F, -46.0F, 4.0F, 16.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(48, 300).addBox(-1.0F, -7.0F, -49.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(29, 297).addBox(-1.0F, 3.0F, -49.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(49, 320).addBox(-1.0F, -10.0F, -39.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(34, 317).addBox(-1.0F, 7.0F, -39.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 43.0F, -11.0F));

		PartDefinition sign = left_arm.addOrReplaceChild("sign",
				CubeListBuilder.create()
						.texOffs(0, 337).addBox(-2.0F, -2.0F, -37.0F, 4.0F, 4.0F, 52.0F, new CubeDeformation(-1.2F))
						.texOffs(0, 378).addBox(-2.0F, -2.0F, 13.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 257).addBox(-1.0F, -1.0F, -67.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
						.texOffs(3, 337).addBox(-2.0F, -4.0F, 12.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(5, 345).addBox(0.0F, -2.0F, 12.25F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(37, 381).addBox(-4.0F, -2.0F, 12.25F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(21, 381).addBox(-2.0F, 0.0F, 12.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(0, 257).addBox(-2.0F, -2.0F, -67.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(-1.2F)),
				PartPose.offset(0.0F, 42.0F, 0.0F));

		sign.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 335).addBox(-1.0F, -12.8284F, -7.1716F, 2.0F, 20.0F, 20.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, -53.0F, -0.7854F, 0.0F, 0.0F));


		PartDefinition doll = left_arm.addOrReplaceChild("doll",
				CubeListBuilder.create(),
				PartPose.offset(-19.0F, 69.0F, 4.0F));


		PartDefinition right_leg2 = doll.addOrReplaceChild("right_leg2",
				CubeListBuilder.create(),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		right_leg2.addOrReplaceChild("rightleg_r1",
				CubeListBuilder.create()
						.texOffs(306, 1).addBox(-8.0F, -24.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(23.0F, -3.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition left_leg2 = doll.addOrReplaceChild("left_leg2",
				CubeListBuilder.create(),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		left_leg2.addOrReplaceChild("leftleg_r1",
				CubeListBuilder.create()
						.texOffs(261, 1).addBox(0.0F, -24.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(15.0F, -3.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition up2 = doll.addOrReplaceChild("up2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = up2.addOrReplaceChild("body2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		body2.addOrReplaceChild("body_r1",
				CubeListBuilder.create()
						.texOffs(262, 42).addBox(-8.0F, -48.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(19.0F, 4.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition left_hand = up2.addOrReplaceChild("left_hand",
				CubeListBuilder.create(),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		left_hand.addOrReplaceChild("lefthand_r1",
				CubeListBuilder.create()
						.texOffs(358, 61).addBox(8.0F, -48.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(7.0F, 17.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition right_hand = up2.addOrReplaceChild("right_hand",
				CubeListBuilder.create(),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		right_hand.addOrReplaceChild("righthand_r1",
				CubeListBuilder.create()
						.texOffs(358, 1).addBox(-16.0F, -48.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(31.0F, 17.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition head2 = up2.addOrReplaceChild("head2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		head2.addOrReplaceChild("head_r1",
				CubeListBuilder.create()
						.texOffs(278, 97).addBox(-8.0F, -64.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(19.0F, 21.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(153, 4).addBox(-13.0F, -79.0F, -11.0F, 26.0F, 40.0F, 24.0F, CubeDeformation.NONE)
						.texOffs(83, 122).addBox(-6.0F, -80.0F, -10.0F, 1.0F, 1.0F, 24.0F, CubeDeformation.NONE)
						.texOffs(146, 123).addBox(5.0F, -80.0F, -10.0F, 1.0F, 1.0F, 24.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 39.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 512);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		if (state.attackTime == 0) {
			this.left_leg.xRot = Mth.cos(state.walkAnimationPos  * 0.6662F) * 1.4F * state.walkAnimationSpeed;
			this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
			this.right_arm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
			this.left_arm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
			this.up.xRot = 0;
		} else if (state.attackTime > 0) {
			int total = state.crushCD;
			int now = total - state.attackTime;
			int frontT = total * 2 / 3;
			if (now < frontT) {
				this.left_arm.xRot = AnimationUtil.getUpDown(now, frontT, -120);
				this.left_arm.yRot = 0;
			} else {
				this.left_arm.xRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
				this.left_arm.yRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
			}
			this.up.xRot = AnimationUtil.getUpDownUpDown(now, total, -15);
			this.right_leg.xRot = 0;
			this.left_leg.xRot = 0;
			this.right_arm.xRot = 0;
		} else {

			int total = state.animThrowCD;
			int now = total + state.attackTime;
			int frontT = total * 3 / 4;
			if (now < frontT) {
				this.right_arm.xRot = AnimationUtil.getUp(now, frontT, -225);
			} else {
				this.right_arm.xRot = AnimationUtil.getDown(now - frontT, total - frontT, -225);
			}
			this.left_arm.xRot = 0;
			this.right_leg.xRot = 0;
			this.left_leg.xRot = 0;
			this.up.xRot = 0;
		}

		// 1.16.5: 工具 / 血量 / 愤怒 / imp 可见性
		this.pole.visible = (state.toolType == GargantuarEntity.GargantuarType.POLE);
		this.sign.visible = (state.toolType == GargantuarEntity.GargantuarType.SIGN);
		this.doll.visible = (state.toolType == GargantuarEntity.GargantuarType.DOLL);
		this.hurt1.visible = (state.percentHealth < 2F / 3);
		this.hurt2.visible = (state.percentHealth < 1F / 3);
		this.angry.visible = state.isSad;
		this.imp.visible = state.hasImp;
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {
		final int cd = GargantuarEntity.DEATH_ANIM_CD;
		final int time = entity.getAnimTime();
		final int roundT = 40;
		if (time < roundT) {
			this.up.xRot = AnimationUtil.getUpDownUpDown(time, roundT, 15);
			this.total.xRot = AnimationUtil.getUpDownUpDown(time - roundT, roundT, 5);
		} else if (time < roundT * 2) {
			this.up.xRot = AnimationUtil.getUpDownUpDown(time - roundT, roundT, 30);
			this.total.xRot = AnimationUtil.getUpDownUpDown(time - roundT, roundT, 10);
		} else {
			this.total.xRot = AnimationUtil.getUp(time - 2 * roundT, cd - 2 * roundT, 80);
		}
	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector,
						   RenderType renderType, int packedLight, int packedOverlay) {
		this.pole.visible  = false;
		this.sign.visible  = false;
		this.doll.visible  = false;
		this.imp.visible   = false;
		this.angry.visible = entity.hasHandDefence();
		collector.submitModelPart(this.total, stack, renderType, packedLight, packedOverlay, null);
	}



	@Override
	public EntityModel<? extends EntityRenderState> getZombieModel() {
		return this;
	}
}