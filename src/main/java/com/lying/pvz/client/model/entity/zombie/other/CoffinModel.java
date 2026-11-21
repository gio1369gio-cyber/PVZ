package com.lying.pvz.client.model.entity.zombie.other;


import com.lying.pvz.client.model.entity.zombie.SimpleZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
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
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。棺材僵尸模型（独立 EntityModel，不继承 PVZZombieModel）：
//     total（根，无方块）
//       ├─ coffin（setPos(0,-48,0)，无方块）
//       │    ├─ bone2（3 个方块：棺身三层）+ bone3（空）
//       │    └─ bone（4 个方块：棺盖四层）
//       ├─ zombie1（setPos(29,0,-16)，标准僵尸：双腿 + 躯干 + 左手 + 右手 + 头 + 头饰）
//       ├─ zombie2（setPos(-28,0,-16)，标准僵尸，但左手/右手的旋转内嵌在 righthand_r1/lefthand_r1 子级中）
//       ├─ zombie3（setPos(29,0,32)，up3 偏移 -45，body3/left_hand3/right_hand3/head3 都带 +45 偏移）
//       └─ zombie4（setPos(-28,0,32)，up4 偏移 -1，body4/left_hand4/right_hand4/head4 都带 +1 偏移）
//   setupAnim：
//     updateParts(entity)：按血量百分比切换 zombie1~4 的可见性（>5/6、>4/6、>3/6、>2/6）
//     四个僵尸各自驱动双腿 + 一只手摆动（zombie1 左、zombie2 右、zombie3 左、zombie4 右）
//     棺材血量 ≤1/6 时，coffin.zRot = sin(ageInTicks*0.8F)*0.2F（濒死摇晃）
//   renderToBuffer：8 参数 (MatrixStack, IVertexBuilder, int, int, float×4)
//                   → 5 参数 (PoseStack, VertexConsumer, int, int, int color)
//   MathHelper → Mth：cos / sin 方法不变。

public class CoffinModel extends SimpleZombieModel<PvZZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("coffin"), "main");

	private final ModelPart total;
	private final ModelPart coffin;
	private final ModelPart zombie1;
	private final ModelPart zombie2;
	private final ModelPart zombie3;
	private final ModelPart zombie4;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart left_hand;
	private final ModelPart left_leg2;
	private final ModelPart right_leg2;
	private final ModelPart right_hand2;
	private final ModelPart left_leg3;
	private final ModelPart right_leg3;
	private final ModelPart left_hand3;
	private final ModelPart left_leg4;
	private final ModelPart right_leg4;
	private final ModelPart right_hand4;

	public CoffinModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.coffin = this.total.getChild("coffin");
		this.zombie1 = this.total.getChild("zombie1");
		this.zombie2 = this.total.getChild("zombie2");
		this.zombie3 = this.total.getChild("zombie3");
		this.zombie4 = this.total.getChild("zombie4");

		this.left_leg = this.zombie1.getChild("left_leg");
		this.right_leg = this.zombie1.getChild("right_leg");
		this.left_hand = this.zombie1.getChild("up").getChild("left_hand");

		this.left_leg2 = this.zombie2.getChild("left_leg2");
		this.right_leg2 = this.zombie2.getChild("right_leg2");
		this.right_hand2 = this.zombie2.getChild("up2").getChild("right_hand2");

		this.left_leg3 = this.zombie3.getChild("left_leg3");
		this.right_leg3 = this.zombie3.getChild("right_leg3");
		this.left_hand3 = this.zombie3.getChild("up3").getChild("left_hand3");

		this.left_leg4 = this.zombie4.getChild("left_leg4");
		this.right_leg4 = this.zombie4.getChild("right_leg4");
		this.right_hand4 = this.zombie4.getChild("up4").getChild("right_hand4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition coffin = total.addOrReplaceChild("coffin",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -48.0F, 0.0F));


		PartDefinition bone2 = coffin.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(213, 64).addBox(-16.0F, -24.0171F, -47.2149F, 32.0F, 11.0F, 96.0F, CubeDeformation.NONE)
						.texOffs(219, 197).addBox(-15.0F, -13.0171F, -46.2149F, 30.0F, 11.0F, 94.0F, CubeDeformation.NONE)
						.texOffs(206, 334).addBox(-18.0F, -2.0171F, -49.2149F, 36.0F, 2.0F, 100.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		coffin.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(18, 259).addBox(-19.0F, -28.0171F, -50.2149F, 38.0F, 3.0F, 102.0F, CubeDeformation.NONE)
						.texOffs(38, 389).addBox(-15.0F, -25.0171F, -46.2149F, 30.0F, 1.0F, 94.0F, CubeDeformation.NONE)
						.texOffs(14, 128).addBox(-17.0F, -29.0171F, -48.2149F, 34.0F, 1.0F, 98.0F, CubeDeformation.NONE)
						.texOffs(24, 13).addBox(-15.0F, -31.0171F, -44.2149F, 30.0F, 2.0F, 90.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition zombie1 = total.addOrReplaceChild("zombie1",
				CubeListBuilder.create(),
				PartPose.offset(29.0F, 0.0F, -16.0F));

		zombie1.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		zombie1.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up = zombie1.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(196, 6).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create(),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		right_hand.addOrReplaceChild("righthand_r1",
				CubeListBuilder.create()
						.texOffs(455, 41).addBox(-3.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 132).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		head.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(415, 18).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 7.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition zombie2 = total.addOrReplaceChild("zombie2",
				CubeListBuilder.create(),
				PartPose.offset(-28.0F, 0.0F, -16.0F));

		zombie2.addOrReplaceChild("right_leg2",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		zombie2.addOrReplaceChild("left_leg2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up2 = zombie2.addOrReplaceChild("up2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		up2.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition left_hand2 = up2.addOrReplaceChild("left_hand2",
				CubeListBuilder.create(),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		left_hand2.addOrReplaceChild("lefthand_r1",
				CubeListBuilder.create()
						.texOffs(196, 6).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		up2.addOrReplaceChild("right_hand2",
				CubeListBuilder.create()
						.texOffs(455, 41).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		PartDefinition head2 = up2.addOrReplaceChild("head2",
				CubeListBuilder.create()
						.texOffs(16, 132).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		head2.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(415, 18).addBox(-6.0F, -71.0F, -6.0F, 12.0F, 7.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 48.0F, 0.0F));


		PartDefinition zombie3 = total.addOrReplaceChild("zombie3",
				CubeListBuilder.create(),
				PartPose.offset(29.0F, 0.0F, 32.0F));

		zombie3.addOrReplaceChild("right_leg3",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 1.0F));

		zombie3.addOrReplaceChild("left_leg3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 1.0F));

		PartDefinition up3 = zombie3.addOrReplaceChild("up3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, -45.0F));

		up3.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 45.0F));

		up3.addOrReplaceChild("left_hand3",
				CubeListBuilder.create()
						.texOffs(196, 6).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 45.0F));

		PartDefinition right_hand3 = up3.addOrReplaceChild("right_hand3",
				CubeListBuilder.create(),
				PartPose.offset(-12.0F, -20.0F, 45.0F));

		right_hand3.addOrReplaceChild("righthand_r2",
				CubeListBuilder.create()
						.texOffs(455, 41).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition head3 = up3.addOrReplaceChild("head3",
				CubeListBuilder.create()
						.texOffs(16, 132).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 45.0F));

		head3.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(415, 18).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 7.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));


		PartDefinition zombie4 = total.addOrReplaceChild("zombie4",
				CubeListBuilder.create(),
				PartPose.offset(-28.0F, 0.0F, 32.0F));

		zombie4.addOrReplaceChild("right_leg4",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		zombie4.addOrReplaceChild("left_leg4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up4 = zombie4.addOrReplaceChild("up4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, -1.0F));

		up4.addOrReplaceChild("body4",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 1.0F));

		up4.addOrReplaceChild("left_hand4",
				CubeListBuilder.create()
						.texOffs(196, 6).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(12.0F, -20.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

		up4.addOrReplaceChild("right_hand4",
				CubeListBuilder.create()
						.texOffs(455, 41).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 1.0F));

		PartDefinition head4 = up4.addOrReplaceChild("head4",
				CubeListBuilder.create()
						.texOffs(16, 132).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 1.0F));

		head4.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(415, 18).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 7.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 512);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);

		this.updateParts(state);

		this.left_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.left_hand.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;

		this.left_leg2.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.right_leg2.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.right_hand2.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;

		this.left_leg3.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.right_leg3.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.left_hand3.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;

		this.left_leg4.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.right_leg4.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.right_hand4.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;

		if (state.percentHealth <= 1f / 6) {
			this.coffin.zRot = Mth.sin(state.ageInTicks * 0.8f) * 0.2f;
		} else {
			this.coffin.zRot = 0;
		}
	}
	public void updateParts(PvZZombieRenderState state) {
		float percent = state.percentHealth;
		this.zombie1.visible = percent > 5f / 6;
		this.zombie2.visible = percent > 4f / 6;
		this.zombie3.visible = percent > 3f / 6;
		this.zombie4.visible = percent > 2f / 6;
	}
}