package com.lying.pvz.client.model.entity.zombie.roof;

import java.util.Optional;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
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
//   几何 1:1 搬运。梯子僵尸模型：
//     total（根，setPos(0, 24, 0)）
//       ├─ right_leg（setPos(-4,-23,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-23,0)，1 个腿方块）
//       └─ up（setPos(0,0,0)，无方块）
//            ├─ body（setPos(0,-23,0)，1 个躯干方块）
//            │    └─ hammer（setPos(9,-1,0.5) + X 25°，4 个方块：锤柄 + 锤头 + 锤尖）
//            ├─ left_hand（setPos(8,-48,0) + X -50°，1 个手方块）
//            ├─ right_hand（setPos(-8,-48,0) + X -50°，1 个手方块）
//            ├─ head（setPos(0,-48,0)，1 个头部方块）
//            │    ├─ sting（setPos(9,-17,-1) + X -35°）→ cube_r1（Y -12.5°，1 个方块，呆毛）
//            │    └─ beard（setPos(0,0.5,0)）→ cube_r2（Z 12.5°）+ cube_r3（Z -12.5°，各 1 个方块，胡须）
//            └─ ladder（setPos(0,-38,-20) + X 12.5°，4 个方块：左右梯柱 + 上下横档）
//                 └─ floor（setPos(0,0,0)，5 个方块：横档）
//   updateFreeParts：hasMetal() → ladder.visible / isLeftHandFree / isRightHandFree
//   refreshAnim：super 后，双手 X 恢复为 -50°（-0.8727F，扶梯姿势）
//   getHandDefence：返回 ladder
//   注意：up 的偏移是 (0,0,0)，body 的偏移是 (0,-23,0)，与普通僵尸的 up -24 / body -7 不同。

public class LadderZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ladder_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart ladder;

	public LadderZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.ladder = this.up.getChild("ladder");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -23.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -23.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition body = up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -25.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -23.0F, 0.0F));


		body.addOrReplaceChild("hammer",
				CubeListBuilder.create()
						.texOffs(0, 163).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 9.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 175).addBox(-1.0F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
						.texOffs(0, 179).addBox(-1.0F, -5.75F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.4F))
						.texOffs(6, 163).addBox(-1.0F, -5.75F, -3.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(9.0F, -1.0F, 0.5F, 0.4363F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(0.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(8.0F, -48.0F, 0.0F, -0.8727F, 0.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-8.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-8.0F, -48.0F, 0.0F, -0.8727F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -48.0F, 0.0F));


		PartDefinition sting = head.addOrReplaceChild("sting",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(9.0F, -17.0F, -1.0F, -0.6109F, 0.0F, 0.0F));


		sting.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 146).addBox(-0.8227F, -1.5736F, 0.7997F, 2.0F, 2.0F, 13.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2182F, 0.0F));


		PartDefinition beard = head.addOrReplaceChild("beard",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.5F, 0.0F));


		beard.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 140).addBox(0.0F, -1.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.75F, -6.0F, -8.5F, 0.0F, 0.0F, 0.2182F));


		beard.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(0, 133).addBox(-7.0F, -1.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.25F, -6.0F, -8.5F, 0.0F, 0.0F, -0.2182F));


		PartDefinition ladder = up.addOrReplaceChild("ladder",
				CubeListBuilder.create()
						.texOffs(15, 202).addBox(-12.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
						.texOffs(0, 202).addBox(9.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
						.texOffs(0, 194).addBox(8.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 185).addBox(-13.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -38.0F, -20.0F, 0.2182F, 0.0F, 0.0F));


		ladder.addOrReplaceChild("floor",
				CubeListBuilder.create()
						.texOffs(31, 250).addBox(-9.0F, 20.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(31, 242).addBox(-9.0F, 12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(30, 234).addBox(-9.0F, 4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(32, 225).addBox(-9.0F, -4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(30, 217).addBox(-9.0F, -12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasLadder = state.hasMetal;
		this.ladder.visible = hasLadder;
		this.isLeftHandFree = !hasLadder;
		this.isRightHandFree = !hasLadder;
	}


	@Override
	public void refreshAnim() {
		super.refreshAnim();
		this.right_hand.xRot = -0.8727F;
		this.left_hand.xRot = -0.8727F;
	}

	@Override
	public Optional<ModelPart> getHandDefence() {
		return Optional.ofNullable(this.ladder);
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