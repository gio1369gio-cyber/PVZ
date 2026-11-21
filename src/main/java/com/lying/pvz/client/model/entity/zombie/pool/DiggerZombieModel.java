package com.lying.pvz.client.model.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.pool.DiggerZombieEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。矿工僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-24,0)，1 个腿方块）
//       └─ up（setPos(0,-24,0)）
//            ├─ body（setPos(0,-7,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-20,0)，1 个手方块）
//            ├─ right_hand（setPos(-12,-20,0)，1 个手方块）
//            │    └─ pickaxe（setPos(0,18.5858,-6.2426) + X -10°，2 个方块：镐柄 + 镐头）
//            │         └─ bone（setPos(0,6,-18) + X 30°，1 个方块：镐尖）
//            └─ head（setPos(0,-24,0)，1 个头部方块）
//                 └─ hat（setPos(0,-16,0)，10 个方块：矿工帽多层）
//   setupAnim：
//     attackTime ∈ (0, MAX_OUT_TIME) 时，total.yRot = ageInTicks / 1f（挖掘时高速旋转）
//     否则 total.yRot = 0
//     hasPickaxe() 时，right_hand.xRot = -π/2 + sin(ageInTicks)*0.5F（挥镐）
//     否则 right_hand.xRot = cos(limbSwing*0.6662F)*1.4F*limbSwingAmount（正常摆臂）
//     最后调用 super.setupAnim，并设置 pickaxe.visible = entity.hasMetal()
//   updateFreeParts：pickaxe.visible = hasPickaxe()，isRightHandFree = !hasPickaxe()
//   注意：setupAnim 末尾的 pickaxe.visible = entity.hasMetal() 与 updateFreeParts 中的
//         pickaxe.visible = hasPickaxe() 是两套独立的可见性判断，原样保留。
//   MathHelper → Mth：cos / sin 方法不变。
//   bone / hat 在 setupAnim 中从未被访问，无需取回为字段。

public class DiggerZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("digger_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;

	private final ModelPart pickaxe;
	private final ModelPart head;

	public DiggerZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.pickaxe = this.right_hand.getChild("pickaxe");
		this.head = this.up.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(221, 221).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(182, 220).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(122, 217).addBox(-8.0F, -17.0F, -5.0F, 16.0F, 24.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(83, 220).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(43, 219).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));


		PartDefinition pickaxe = right_hand.addOrReplaceChild("pickaxe",
				CubeListBuilder.create()
						.texOffs(8, 231).addBox(-1.0F, -11.0434F, -18.7538F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.1F))
						.texOffs(193, 177).addBox(-1.0F, -1.0F, -19.0F, 2.0F, 2.0F, 28.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.0F, 18.5858F, -6.2426F, -0.1745F, 0.0F, 0.0F));

		pickaxe.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(27, 243).addBox(-1.0F, -1.1439F, -0.1677F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 6.0F, -18.0F, 0.5236F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(118, 177).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		head.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(44, 192).addBox(-9.0F, -1.0F, -8.0F, 18.0F, 1.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(52, 173).addBox(-8.0F, -3.0F, -6.0F, 16.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(57, 159).addBox(-8.0F, -5.0F, -3.0F, 16.0F, 2.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(120, 168).addBox(-8.0F, -6.0F, 0.0F, 16.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(170, 161).addBox(-1.0F, -4.0F, -9.0F, 2.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(200, 161).addBox(-2.0F, -5.0F, -11.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(213, 143).addBox(-9.0F, 0.0F, 8.0F, 18.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(166, 143).addBox(-9.0F, 0.0F, -9.0F, 18.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(123, 131).addBox(8.0F, 0.0F, -8.0F, 1.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(79, 132).addBox(-9.0F, 0.0F, -8.0F, 1.0F, 1.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		if (state.attackTime > 0 && state.attackTime < DiggerZombieEntity.MAX_OUT_TIME) {
			total.yRot = state.ageInTicks / 1f;
		} else {
			total.yRot = 0;
		}
		if (state.hasPickaxe) {
			this.right_hand.xRot = -1.57F + Mth.sin(state.ageInTicks) * 0.5F;
		} else {
			this.right_hand.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		}
		super.setupAnim(state);
		this.pickaxe.visible = state.hasMetal;
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasPickaxe =state.hasPickaxe;
		this.pickaxe.visible = hasPickaxe;
		this.isRightHandFree = !hasPickaxe;
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