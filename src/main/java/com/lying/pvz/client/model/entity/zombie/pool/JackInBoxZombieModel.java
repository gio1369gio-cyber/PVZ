package com.lying.pvz.client.model.entity.zombie.pool;

import java.util.Optional;

import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.api.entity.ability.BodyType;
import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.JackInBoxZombieEntity;
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
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。玩偶盒僵尸模型：
//     total（根，setPos(0, 24, 0)）
//       ├─ left_leg（setPos(4,-24,0)，2 个方块：腿 + 脚）
//       ├─ right_leg（setPos(-4,-24,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,-24,0)）
//            ├─ body（setPos(0,0,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-20,0) + X -30°，1 个手方块）
//            │    ├─ bone（setPos(2,21,0) + X -60°，1 个方块，手部延伸）
//            │    └─ box（setPos(-10,27,-3) + X 30°，5 个方块：底板 + 左右侧 + 前后）
//            │         ├─ jack（setPos(1,-1,-1) + X 5°，1 个方块，弹簧小丑身体）
//            │         │    └─ jack_head（setPos(0,-18,0) + X 15°，1 个方块，小丑头）
//            │         │         ├─ h1（setPos(0,-3,-2) + X -25°，1 个方块，左须）
//            │         │         ├─ h3（setPos(0,-3,2) + X 25°，1 个方块，右须）
//            │         │         └─ h2（setPos(0,-4,0) + Z -15°，1 个方块，帽子）
//            │         └─ top（setPos(1,-11,-6) + X 120°，无方块，盒盖铰链）
//            │              └─ cube_r1（setPos(-3,0,0) + X -120°，1 个方块，盒盖）
//            ├─ right_hand（setPos(-12,-20,0) + X -40°，1 个手方块 + 1 个方块）
//            └─ head（setPos(0,-24,0)，1 个头部方块）
//   setupAnim：
//     先调用 super（走行走/攻击逻辑），然后：
//     attackTime ∈ [0, JACK_EXPLODE_CD]（小丑出盒阶段）：
//       top.xRot = 2.0944F - tick / 10f（盒盖逐渐打开）
//     否则（未触发或已结束）：
//       top.xRot = 2.0944F（盒盖复位）
//       right_hand.xRot = sin(ageInTicks) / 4f - 1f（右手轻微摆动）
//   updateFreeParts：
//     isLeftHandFree = !hasMetal()（持盒时左手不自由）
//     box.visible = hasMetal()（盒子是否存在）
//     jack.visible = (attackTime ∈ [0, JACK_EXPLODE_CD/4))（小丑只在出盒前 1/4 阶段可见）
//   renderBody：
//     bodyType == HAND 时 box.visible = false（掉落手时不显示盒子）
//     jack.visible = false（掉落任何部件时都不显示小丑）
//     再调用 super
//   getHandDefence：返回 box（盒子是左手的防御部件）
//   rightHandOriginAngel = -30（供 tickPartAnim 计算掉落动画起始角）
//   MathHelper → Mth：sin 方法不变。
//   bone / jack / jack_head / h1 / h2 / h3 / cube_r1 / box / top 中，
//   只有 box / jack / top 需要在 setupAnim / renderBody / updateFreeParts 中访问，
//   其余部件仅在 MeshDefinition 中构建。

public class JackInBoxZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("jack_in_box_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	private final ModelPart box;
	private final ModelPart jack;
	private final ModelPart top;

	public JackInBoxZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.box = this.left_hand.getChild("box");
		this.jack = this.box.getChild("jack");
		this.top = this.box.getChild("top");

		this.rightHandOriginAngel = -30;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 31).addBox(-3.0F, 23.0F, -6.0F, 6.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(25, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 35).addBox(-3.0F, 23.0F, -6.0F, 6.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(51, 0).addBox(-8.0F, -24.0F, -5.0F, 16.0F, 24.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(21, 31).addBox(-4.0F, -4.0F, -3.0F, 6.0F, 26.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(12.0F, -20.0F, 0.0F, -0.5236F, 0.0F, 0.0F));


		left_hand.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(1, 40).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.0F, 21.0F, 0.0F, -1.0472F, 0.0F, 0.0F));


		PartDefinition box = left_hand.addOrReplaceChild("box",
				CubeListBuilder.create()
						.texOffs(1, 97).addBox(-5.0F, -1.0F, -7.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(75, 35).addBox(-5.0F, -11.0F, -7.0F, 1.0F, 10.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(75, 59).addBox(6.0F, -11.0F, -7.0F, 1.0F, 10.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(50, 69).addBox(-4.0F, -11.0F, -7.0F, 10.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(58, 83).addBox(-4.0F, -11.0F, 4.0F, 10.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, 27.0F, -3.0F, 0.5236F, 0.0F, 0.0F));


		PartDefinition jack = box.addOrReplaceChild("jack",
				CubeListBuilder.create()
						.texOffs(106, 1).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 18.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -1.0F, -1.0F, 0.0873F, 0.0F, 0.0F));


		PartDefinition jack_head = jack.addOrReplaceChild("jack_head",
				CubeListBuilder.create()
						.texOffs(106, 22).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -18.0F, 0.0F, 0.2618F, 0.0F, 0.0F));


		jack_head.addOrReplaceChild("h1",
				CubeListBuilder.create()
						.texOffs(106, 32).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, -0.4363F, 0.0F, 0.0F));


		jack_head.addOrReplaceChild("h3",
				CubeListBuilder.create()
						.texOffs(103, 40).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, 0.4363F, 0.0F, 0.0F));


		jack_head.addOrReplaceChild("h2",
				CubeListBuilder.create()
						.texOffs(104, 50).addBox(-1.0F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.2618F));


		PartDefinition top = box.addOrReplaceChild("top",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(1.0F, -11.0F, -6.0F, 2.0944F, 0.0F, 0.0F));


		top.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(2, 112).addBox(-2.0F, -1.0F, 0.0F, 10.0F, 1.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -2.0944F, 0.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(48, 35).addBox(-2.0F, -4.0F, -4.0F, 6.0F, 26.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(2, 49).addBox(0.0F, 22.0F, 1.0F, 2.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -0.6981F, 0.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 68).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.attackTime >= 0 && state.attackTime <= JackInBoxZombieEntity.JACK_EXPLODE_CD) {
			final int tick = JackInBoxZombieEntity.JACK_EXPLODE_CD - state.attackTime;
			this.top.xRot = 2.0944F - tick / 10f;
		} else {
			this.top.xRot = 2.0944F;
			this.right_hand.xRot = Mth.sin(state.ageInTicks) / 4f - 1f;
		}
	}


	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		if (entity.getBodyType() == BodyType.HAND) {
			this.box.visible = false;
		}
		this.jack.visible = false;
		super.renderBody(entity, stack, collector, renderType, packedLight, packedOverlay);
	}

	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasBox = state.hasMetal;
		this.isLeftHandFree = !hasBox;
		this.box.visible = hasBox;
		this.jack.visible = (state.attackTime >= 0
				&& state.attackTime < JackInBoxZombieEntity.JACK_EXPLODE_CD / 4);
	}

	// 1.16.5: 返回 box / 1.21.1: 返回 ModelPart
	@Override
	public Optional<ModelPart> getHandDefence() {
		return Optional.ofNullable(this.box);
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