package com.lying.pvz.client.model.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
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
//   1. 几何 1:1 搬运。骨架 + 豌豆射手头。
//   2. 骨架（与所有 zombotany 僵尸共享）：
//        total
//          ├─ left_leg  ( 4,-24,0)  8×24×8
//          ├─ right_leg (-4,-24,0)  8×24×8
//          └─ up (0,-24,0)
//              ├─ head (0,-24,0) —— 豌豆射手头（9 方块）
//              ├─ right_hand (-12,-20,0)  8×24×8
//              ├─ left_hand  ( 12,-20,0)  8×24×8
//              └─ body (0,0,0)  16×24×8
//   3. ⚠️ 头部装配顺序与原版一致（head 在 right_hand/left_hand/body 之前），
//        这不是"必需"的顺序，但保留原样以避免后续 diff 混乱。
//   4. 7 个 getter：getZombieLeftHand 等 —— 返回类型 ModelRenderer → ModelPart，
//      由 PVZZombieModel 基类在 1.21.1 的签名决定。
//   5. 无 setupAnim 重写 —— 交给 PVZZombieModel 基类的默认步行/攻击动画。

public class PeaShooterZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pea_shooter_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart head;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart body;

	public PeaShooterZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.head = this.up.getChild("head");
		this.right_hand = this.up.getChild("right_hand");
		this.left_hand = this.up.getChild("left_hand");
		this.body = this.up.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(228, 243).addBox(-5.0F, -12.0F, 7.0F, 10.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(198, 202).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(230, 168).addBox(7.0F, -12.0F, -5.0F, 1.0F, 10.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(234, 137).addBox(-8.0F, -12.0F, -5.0F, 1.0F, 10.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(208, 251).addBox(-1.0F, -11.0F, 8.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(158, 245).addBox(-5.0F, -15.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(137, 240).addBox(-2.0F, -12.0F, 12.0F, 4.0F, 5.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(80, 227).addBox(-4.0F, -11.0F, -21.0F, 8.0F, 8.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
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