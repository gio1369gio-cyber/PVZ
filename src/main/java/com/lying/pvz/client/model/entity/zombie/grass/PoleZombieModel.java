package com.lying.pvz.client.model.entity.zombie.grass;

import java.util.Optional;

import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.api.entity.ability.BodyType;
import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;


// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。撑杆僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-26,0)，3 个方块：腿 + 脚 + 膝）
//       ├─ left_leg（setPos(4,-26,0)，3 个方块：腿 + 脚 + 膝）
//       └─ up（setPos(0,-26,0)）
//            ├─ body（1 个躯干方块）
//            ├─ left_hand（setPos(11,-23,0)，1 个手方块 + left_hand2 子级）
//            │    └─ left_hand2（setPos(-2,12,0)，1 个方块，膨胀 0.05F）
//            ├─ right_hand（setPos(-11,-23,0) + X -90°，1 个手方块，膨胀 -0.05F）
//            │    └─ right_hand2（setPos(-2,12,0) + X -90°，1 个方块，膨胀 0.05F）
//            │         └─ pole（setPos(0,9,1)，1 个 2×2×85 长杆，向 -Z 延伸）
//            └─ head（1 个头部方块）
//   setupAnim：调用 super 后，若 attackTime > 0，按 getPoleJumpCD()/4 分三段驱动撑杆跳：
//     段1（time < T）：right_hand 从 -90° 抬到 0°
//     段2（T ≤ time < 3T）：right_hand/up 逐渐翻转，双腿抬到 -120°
//     段3（3T ≤ time）：右手继续抬到 +90°，身体恢复，双腿落回
//   renderBody：调用 super 后，若 bodyType == BODY，right_hand2.xRot 按 hasHandDefence 切换
//   refreshAnim：right_hand.xRot = -1.5708F，up.xRot = 0。
//   updateFreeParts：pole.visible = hasPole()，isRightHandFree = !hasPole()，
//                    right_hand2.xRot = isRightHandFree ? 0 : -1.5708F
//   rightHandOriginAngel = -90。

public class PoleZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pole_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart right_hand2;
	private final ModelPart pole;
	private final ModelPart head;

	public PoleZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.right_hand2 = this.right_hand.getChild("right_hand2");
		this.pole = this.right_hand2.getChild("pole");
		this.head = this.up.getChild("head");
		this.rightHandOriginAngel = -90;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(161, 220).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(124, 239).addBox(-3.0F, 24.0F, -7.0F, 6.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(158, 202).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 4.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -26.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(228, 219).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(190, 238).addBox(-3.0F, 24.0F, -7.0F, 6.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(190, 198).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 4.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -26.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -26.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(2, 222).addBox(-8.0F, -26.0F, -3.0F, 16.0F, 26.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(49, 221).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 26.0F, 6.0F, new CubeDeformation(-0.05F)),
				PartPose.offset(11.0F, -23.0F, 0.0F));


		left_hand.addOrReplaceChild("left_hand2",
				CubeListBuilder.create()
						.texOffs(228, 195).addBox(-1.0F, 3.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.05F)),
				PartPose.offset(-2.0F, 12.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(49, 221).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(-11.0F, -23.0F, 0.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition right_hand2 = right_hand.addOrReplaceChild("right_hand2",
				CubeListBuilder.create()
						.texOffs(228, 195).addBox(-1.0F, 3.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.05F)),
				PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, -1.5708F, 0.0F, 0.0F));


		right_hand2.addOrReplaceChild("pole",
				CubeListBuilder.create()
						.texOffs(76, 4).addBox(1.0F, 2.1472F, -45.3617F, 2.0F, 2.0F, 85.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 9.0F, 1.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(196, 104).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -26.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.attackTime> 0) {
			int time = state.poleJumpCD- state.attackTime;
			final int T = state.poleJumpCD / 4;
			if (time < T) {
				this.right_hand.xRot = -AnimationUtil.byDegree(90) + AnimationUtil.getUp(time, T, 90);
			} else if (time < T * 3) {
				this.right_hand.xRot = AnimationUtil.getUp(time - T, T * 2, 90);
				this.up.xRot = -AnimationUtil.getUp(time - T, T * 2, 90);
				this.left_leg.xRot = -AnimationUtil.getUp(time - T, T * 2, 120);
				this.right_leg.xRot = -AnimationUtil.getUp(time - T, T * 2, 120);
			} else {
				this.right_hand.xRot = AnimationUtil.byDegree(90) - AnimationUtil.getUp(time - 3 * T, T, 90);
				this.up.xRot = -AnimationUtil.byDegree(90) + AnimationUtil.getUp(time - 3 * T, T, 90);
				this.left_leg.xRot = -AnimationUtil.byDegree(120) + AnimationUtil.getUp(time - 3 * T, T, 120);
				this.right_leg.xRot = -AnimationUtil.byDegree(120) + AnimationUtil.getUp(time - 3 * T, T, 120);
			}
		}
	}


	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		super.renderBody(entity, stack, collector, renderType, packedLight, packedOverlay);
		if (entity.getBodyType() == BodyType.BODY) {
			this.right_hand2.xRot = !entity.hasHandDefence() ? 0 : -1.5708F;
		}
	}

	@Override
	public void refreshAnim() {
		this.right_hand.xRot = -1.5708F;
		this.up.xRot = 0;
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasPole = state.hasPole;
		this.pole.visible = hasPole;
		this.isRightHandFree = !hasPole;
		this.right_hand2.xRot = this.isRightHandFree ? 0 : -1.5708F;
	}

	@Override
	public Optional<ModelPart> getHandDefence() {
		return Optional.ofNullable(this.pole);
	}

	@Override
	public ModelPart getZombieLeftHand() {
		return this.left_hand;
	}

	@Override
	public ModelPart getZombieRightHand() {
		return this.right_hand;
	}

	@Override
	public ModelPart getZombieLeftLeg() {
		return this.left_leg;
	}

	@Override
	public ModelPart getZombieRightLeg() {
		return this.right_leg;
	}

	@Override
	public ModelPart getZombieHead() {
		return this.head;
	}

	@Override
	public ModelPart getZombieUpBody() {
		return this.up;
	}

	@Override
	public ModelPart getZombieWholeBody() {
		return this.total;
	}
}