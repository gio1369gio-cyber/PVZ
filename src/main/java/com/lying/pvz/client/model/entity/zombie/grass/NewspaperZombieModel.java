package com.lying.pvz.client.model.entity.zombie.grass;

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

// Made with Blockbench 3.7.1
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。读报僵尸模型：
//     total（根，无方块）
//       ├─ left_leg（setPos(4,-24,0)，2 个方块：腿 + 脚）
//       ├─ right_leg（setPos(-4,-24,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,-24,0)）
//            ├─ head（setPos(0,-24,0)，3 个方块：头 + 两条发丝）
//            │    ├─ bone3（setPos(0,-10,0) + X 20°，7 个方块：眼镜框）
//            │    ├─ red_eyes（setPos(0,0,0)，2 个方块，Z=-7.05）
//            │    └─ normal_eyes（setPos(0,0,0)，2 个方块，Z=-7.05）
//            ├─ right_hand（setPos(-11,-21,0) + X -60°，1 个手方块）
//            │    └─ paper（setPos(11,23,5) + X 70°，报纸）
//            │         ├─ bone2（setPos(10,-13.8186,1.2498) + X -90° + Y 40°）
//            │         └─ bone（setPos(-10,-13.8186,1.2498) + X -90° + Y -40°）
//            ├─ left_hand（setPos(11,-21,0) + X -60°，1 个手方块）
//            └─ body（setPos(0,-7,0)，1 个躯干方块）
//   updateFreeParts：
//     isPaperDestroyed = entity.isAngry()
//     → isLeftHandFree / isRightHandFree 切换
//     → red_eyes.visible = isPaperDestroyed，normal_eyes.visible = !isPaperDestroyed
//     → paper.visible = !isPaperDestroyed
//   refreshAnim：双手 X 恢复为 -1.0472F（报纸保持举起）。
//   isZombieAngry：entity.isAngry() || super.isZombieAngry(entity)。
//   getHandDefence：返回 paper。
//   rightHandOriginAngel = -60（供 tickPartAnim 计算掉落动画起始角）。

public class NewspaperZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("newspaper_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart paper;
	private final ModelPart head;
	private final ModelPart red_eyes;
	private final ModelPart normal_eyes;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart body;

	public NewspaperZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.head = this.up.getChild("head");
		this.red_eyes = this.head.getChild("red_eyes");
		this.normal_eyes = this.head.getChild("normal_eyes");
		this.right_hand = this.up.getChild("right_hand");
		this.paper = this.right_hand.getChild("paper");
		this.left_hand = this.up.getChild("left_hand");
		this.body = this.up.getChild("body");
		this.rightHandOriginAngel = -60;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(230, 225).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(196, 242).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(230, 192).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(191, 220).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(66, 182).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(130, 207).addBox(-7.0F, -15.0F, 2.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(169, 208).addBox(-6.0F, -15.0F, -3.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		head.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(200, 188).addBox(-8.0F, -1.0F, -10.0F, 1.0F, 1.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(140, 180).addBox(7.0F, -1.0F, -10.0F, 1.0F, 1.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(183, 187).addBox(-7.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(206, 172).addBox(5.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(227, 175).addBox(-1.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(241, 169).addBox(-5.0F, -2.0F, -10.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(174, 175).addBox(1.0F, -2.0F, -10.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.3491F, 0.0F, 0.0F));


		head.addOrReplaceChild("red_eyes",
				CubeListBuilder.create()
						.texOffs(45, 187).addBox(-6.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 186).addBox(1.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		head.addOrReplaceChild("normal_eyes",
				CubeListBuilder.create()
						.texOffs(47, 178).addBox(-6.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(20, 175).addBox(1.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(148, 222).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-11.0F, -21.0F, 0.0F, -1.0472F, 0.0F, 0.0F));


		PartDefinition paper = right_hand.addOrReplaceChild("paper",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(11.0F, 23.0F, 5.0F, 1.2217F, 0.0F, 0.0F));


		paper.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(88, 212).addBox(0.0F, 0.0F, -13.0F, 1.0F, 16.0F, 25.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -13.8186F, 1.2498F, -1.5708F, 0.6981F, 0.0F));


		paper.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(5, 174).addBox(-1.0F, 0.0F, -13.0F, 1.0F, 16.0F, 25.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, -13.8186F, 1.2498F, -1.5708F, -0.6981F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(59, 221).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(11.0F, -21.0F, 0.0F, -1.0472F, 0.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(3, 221).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}
	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean isPaperDestroyed = state.isAngry;
		this.isLeftHandFree = isPaperDestroyed;
		this.isRightHandFree = isPaperDestroyed;
		this.red_eyes.visible = isPaperDestroyed;
		this.normal_eyes.visible = !isPaperDestroyed;
		this.paper.visible = !isPaperDestroyed;
	}


	@Override
	public void refreshAnim() {
		this.getZombieLeftHand().xRot = -1.0472F;
		this.getZombieRightHand().xRot = -1.0472F;
	}


	@Override
	protected boolean isZombieAngry(PvZZombieRenderState state) {
		return state.isAngry || super.isZombieAngry(state);
	}

	@Override
	public Optional<ModelPart> getHandDefence() {
		return Optional.ofNullable(this.paper);
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