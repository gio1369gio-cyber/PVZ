package com.lying.pvz.client.model.entity.creature;

import com.lying.pvz.client.render.renderer.entity.creature.FoodieZombieRender;
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

/**
 * 贪吃僵尸（Foodie Zombie）模型。
 * total（setPos(0,24,0)，无方块，仅变换节点）
 *   ├─ head（setPos(0,-20,0)，7 个方块：头主体 + 左右凸片 + 后片 + 前脸面板 + 上下横条）
 *   │    └─ bone6（setPos(0,0,-7)，2 个方块）
 *   │         └─ bone7（setPos(-7,-1,0)，setRotationAngle(1.309,-0.6981,0)，1 个方块）
 *   ├─ right_hand（setPos(-8,-20,0)，1 个方块：手臂 4×25×4）
 *   │    ├─ bone3（setPos(0,0,0)，1 个方块）
 *   │    │    └─ bone4（setPos(-4,5,-2)，setRotationAngle(0,-1.1345,0)，1 个方块）
 *   │    └─ bone5（setPos(-4,5,2)，setRotationAngle(0,1.1345,0)，1 个方块）
 *   ├─ left_hand（setPos(9,-20,0)，1 个方块：手臂 4×25×4）
 *   │    └─ armor（setPos(3,5,0)，1 个方块）
 *   │         ├─ bone（setPos(0,0,3)，setRotationAngle(0,-1.1345,0)，1 个方块）
 *   │         └─ bone2（setPos(0,1,-2)，setRotationAngle(0,1.1345,0)，1 个方块）
 *   ├─ body（setPos(0,-3,0)，1 个方块：躯干 16×25×6）
 *   ├─ right_leg（setPos(-4,5,0)，1 个方块：腿 4×23×4）
 *   │    └─ right_foot（setPos(0,24,0)，3 个方块：脚掌 + 左右侧片）
 *   └─ left_leg（setPos(4,5,0)，1 个方块：腿 4×23×4）
 *        └─ left_foot（setPos(0,24,0)，3 个方块：脚掌 + 左右侧片）
 */
public class FoodieZombieModel extends EntityModel<FoodieZombieRender.FoodieZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(StringUtil.id("foodie_zombie"), "main");

	private final ModelPart total;
	private final ModelPart head;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart body;
	private final ModelPart right_leg;
	private final ModelPart right_foot;
	private final ModelPart left_leg;
	private final ModelPart left_foot;

	// 1.21.1: 本地 EntityModel 只有无参与 Function 两个构造器，无参默认 entityCutoutNoCull
	// root 不传给 super，仅用于取回顶层部件
	public FoodieZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		// 1.21.1: 子级通过父子链逐级 getChild 取回
		this.head = this.total.getChild("head");
		this.right_hand = this.total.getChild("right_hand");
		this.left_hand = this.total.getChild("left_hand");
		this.body = this.total.getChild("body");
		this.right_leg = this.total.getChild("right_leg");
		this.right_foot = this.right_leg.getChild("right_foot");
		this.left_leg = this.total.getChild("left_leg");
		this.left_foot = this.left_leg.getChild("left_foot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// 1.16.5: total.setPos(0, 24, 0); setRotationAngle(total, 0, 0, 0)
		// 1.21.1: 静态旋转为 0，只用 PartPose.offset；动态旋转在 setupAnim 中设置
		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		// ===== head =====
		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(80, 104).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 89).addBox(6.0F, -8.0F, -6.0F, 1.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 73).addBox(-7.0F, -8.0F, -6.0F, 1.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 68).addBox(-6.0F, -8.0F, 6.0F, 12.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(102, 60).addBox(-6.0F, -9.0F, -7.0F, 12.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(110, 56).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(110, 52).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -20.0F, 0.0F));

		// 1.16.5: bone6.setPos(0, 0, -7); head.addChild(bone6)
		PartDefinition bone6 = head.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(118, 45).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(110, 39).addBox(-8.0F, -2.0F, -2.0F, 7.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -7.0F));

		// 1.16.5: bone7.setPos(-7, -1, 0); setRotationAngle(bone7, 1.309, -0.6981, 0); bone6.addChild(bone7)
		bone6.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(40, 109).addBox(-1.6428F, -1.6943F, -0.3237F, 2.0F, 2.0F, 17.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -1.0F, 0.0F, 1.309F, -0.6981F, 0.0F));

		// ===== right_hand =====
		PartDefinition right_hand = total.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(112, 1).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 25.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-8.0F, -20.0F, 0.0F));

		// 1.16.5: bone3.setPos(0, 0, 0); right_hand.addChild(bone3)
		PartDefinition bone3 = right_hand.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(80, 89).addBox(-5.0F, 3.0F, -3.0F, 1.0F, 5.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		// 1.16.5: bone4.setPos(-4, 5, -2); setRotationAngle(bone4, 0, -1.1345, 0); bone3.addChild(bone4)
		bone3.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(76, 103).addBox(-1.0F, -2.0F, -4.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 5.0F, -2.0F, 0.0F, -1.1345F, 0.0F));

		// 1.16.5: bone5.setPos(-4, 5, 2); setRotationAngle(bone5, 0, 1.1345, 0); right_hand.addChild(bone5)
		right_hand.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(96, 47).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 5.0F, 2.0F, 0.0F, 1.1345F, 0.0F));

		// ===== left_hand =====
		PartDefinition left_hand = total.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(94, 0).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 25.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(9.0F, -20.0F, 0.0F));

		// 1.16.5: armor.setPos(3, 5, 0); left_hand.addChild(armor)
		PartDefinition armor = left_hand.addOrReplaceChild("armor",
				CubeListBuilder.create()
						.texOffs(76, 3).addBox(0.0F, -2.0F, -3.0F, 1.0F, 5.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, 5.0F, 0.0F));

		// 1.16.5: bone.setPos(0, 0, 3); setRotationAngle(bone, 0, -1.1345, 0); armor.addChild(bone)
		armor.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(79, 18).addBox(-0.866F, -2.0F, -0.5F, 1.0F, 5.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, -1.1345F, 0.0F));

		// 1.16.5: bone2.setPos(0, 1, -2); setRotationAngle(bone2, 0, 1.1345, 0); armor.addChild(bone2)
		armor.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(80, 35).addBox(0.0F, -3.0F, -4.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 1.1345F, 0.0F));

		// ===== body =====
		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(1, 93).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		// ===== right_leg / right_foot =====
		PartDefinition right_leg = total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(1, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, 5.0F, 0.0F));

		right_leg.addOrReplaceChild("right_foot",
				CubeListBuilder.create()
						.texOffs(22, 73).addBox(-2.0F, -1.0F, -11.0F, 4.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(56, 95).addBox(2.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(65, 81).addBox(-3.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		// ===== left_leg / left_foot =====
		PartDefinition left_leg = total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(57, 2).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, 5.0F, 0.0F));

		left_leg.addOrReplaceChild("left_foot",
				CubeListBuilder.create()
						.texOffs(62, 62).addBox(-2.0F, -1.0F, -11.0F, 4.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(49, 64).addBox(2.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(60, 50).addBox(-3.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(FoodieZombieRender.FoodieZombieRenderState state) {
		super.setupAnim(state);
		this.total.xRot = 3.14159f / 2;
		this.head.xRot = -3.14159f / 2;
		this.head.yRot = 0;
		this.right_hand.xRot = 0;
		this.left_hand.xRot = 0;
		this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.left_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.right_foot.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.left_foot.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
	}

}