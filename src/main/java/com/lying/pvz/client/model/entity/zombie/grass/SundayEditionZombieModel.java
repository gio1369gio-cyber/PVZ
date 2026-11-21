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


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。周日版僵尸模型（比 Newspaper/Old 多头发 + 眼镜 + 大报纸）：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，2 个方块：腿 + 脚）
//       ├─ left_leg（setPos(4,-24,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,0,0)，无方块；注意偏移是 0，不是 -24）
//            ├─ head（setPos(0,-48,0)，1 个头部方块）
//            │    ├─ hair（setPos(0,0,0)，4 个骨骼：bone7/bone6/bone4/bone5，构成乱发）
//            │    ├─ glass（setPos(0,-10,0) + X 20°，7 个方块：眼镜框）
//            │    ├─ red_eyes（setPos(0,0,0)，2 个方块，Z=-7.1）
//            │    └─ normal_eyes（setPos(0,0,0)，2 个方块，Z=-7.1）
//            ├─ right_hand（setPos(-11,-45,0) + X -67.5°，1 个手方块）
//            │    └─ paper（setPos(12,20,0.5) + X -10°，大报纸）
//            │         ├─ bone2（setPos(10.75,1.7244,2.8059) + Z 45°，1×22×45 长纸片）
//            │         └─ bone（setPos(-13.75,1.7244,2.8059) + Z -45°，1×22×45 长纸片）
//            ├─ left_hand（setPos(11,-45,0) + X -67.5° + Y -5°，1 个手方块；带 Y 旋转）
//            └─ body（setPos(0,-24,0)，1 个躯干方块）
//   updateFreeParts：isPaperDestroyed = entity.isAngry()
//     → isLeftHandFree / isRightHandFree 切换
//     → red_eyes.visible = isPaperDestroyed，normal_eyes.visible = !isPaperDestroyed
//     → paper.visible = !isPaperDestroyed
//   refreshAnim：双手 X 恢复为 -1.0472F（-60°，注意与初始 Pose 的 -67.5° 不同）。
//   isZombieAngry：entity.isAngry() || super.isZombieAngry(entity)。
//   getHandDefence：返回 paper。
//   rightHandOriginAngel = -67.5F（与 Newspaper/Old 的 -60 不同）。
//
//   与 NewspaperZombieModel / OldZombieModel 的差异：
//     - up 的偏移：0（vs -24）
//     - body 的偏移：-24（vs Newspaper -7 / Old 0）
//     - head 的偏移：-48（因为 up 在 0）
//     - left_hand 带 Y -5° 旋转（其他两个模型没有）
//     - rightHandOriginAngel：-67.5F（vs -60）
//     - paper 下有 2 个 1×22×45 的长纸片（其他两个模型是 1×16×25）
//     - head 下有 hair（4 骨骼乱发）+ glass（眼镜框），其他两个模型是 bone3（眼镜框）

public class SundayEditionZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sunday_edition_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart head;

	private final ModelPart red_eyes;
	private final ModelPart normal_eyes;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart paper;


	public SundayEditionZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.head = this.up.getChild("head");
		this.red_eyes = this.head.getChild("red_eyes");
		this.normal_eyes = this.head.getChild("normal_eyes");
		this.right_hand = this.up.getChild("right_hand");
		this.paper = this.right_hand.getChild("paper");
		this.left_hand = this.up.getChild("left_hand");
		this.rightHandOriginAngel = -67.5F;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(230, 192).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(191, 220).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(230, 225).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(196, 242).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(66, 182).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -48.0F, 0.0F));


		PartDefinition hair = head.addOrReplaceChild("hair",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		hair.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 9.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -14.0F, -2.0F, -0.3491F, 1.0472F, -0.6981F));


		hair.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(169, 208).addBox(-9.0F, -2.0F, -2.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, -13.0F, 1.0F, 0.0F, 0.0F, 0.5236F));

		hair.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 9.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, -0.8727F, -0.6981F));

		hair.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(130, 207).addBox(-1.0F, -1.0F, -1.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -0.6981F));


		head.addOrReplaceChild("glass",
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
						.texOffs(5, 187).addBox(-6.0F, -13.0F, -7.1F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(4, 176).addBox(1.0F, -13.0F, -7.1F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		head.addOrReplaceChild("normal_eyes",
				CubeListBuilder.create()
						.texOffs(30, 188).addBox(-6.0F, -13.0F, -7.1F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(26, 178).addBox(1.0F, -13.0F, -7.1F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(148, 222).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-11.0F, -45.0F, 0.0F, -1.1781F, 0.0F, 0.0F));

		PartDefinition paper = right_hand.addOrReplaceChild("paper",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(12.0F, 20.0F, 0.5F, -0.1745F, 0.0F, 0.0F));


		paper.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(160, 75).addBox(1.7677F, -4.1066F, -28.0F, 1.0F, 22.0F, 45.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.75F, 1.7244F, 2.8059F, 0.0F, 0.0F, 0.7854F));


		paper.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(160, 4).addBox(-0.8393F, -1.8085F, -28.0F, 1.0F, 22.0F, 45.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-13.75F, 1.7244F, 2.8059F, 0.0F, 0.0F, -0.7854F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(59, 221).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(11.0F, -45.0F, 0.0F, -1.1781F, -0.0873F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(3, 221).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

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