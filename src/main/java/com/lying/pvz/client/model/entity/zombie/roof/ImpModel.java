package com.lying.pvz.client.model.entity.zombie.roof;


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
//   几何 1:1 搬运。小鬼僵尸模型（64×64 小纹理，缩小版普通僵尸）：
//     total（根，setPos(0, 24, 0)）
//       ├─ left_leg（setPos(3, -13, 0)，1 个 4×13×4 腿方块）
//       ├─ right_leg（setPos(-3, -12, 0)，1 个 4×13×4 腿方块）
//       └─ up（setPos(0, -12, 0)）
//            ├─ body（setPos(0, 0, 0)，1 个 10×15×4 躯干方块）
//            ├─ right_arm（setPos(-7, -14, 0)，1 个 4×15×4 手方块）
//            ├─ left_arm（setPos(7, -14, 0)，1 个 4×15×4 手方块）
//            └─ head（setPos(0, -16, 0)，1 个 10×10×10 头部方块）
//   setupAnim：全部继承 PVZZombieModel，本类不重写。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。
//   与普通僵尸的比例：腿 13 高（vs 24）、躯干 15 高（vs 24）、头 10×10×10（vs 16×16×16），
//   整体约 0.6 倍缩小。
//   注意：左右腿的 Y 偏移不对称（-13 vs -12），这是原设计，原样保留。

public class ImpModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "imp"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart head;

	public ImpModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.right_arm = this.up.getChild("right_arm");
		this.left_arm = this.up.getChild("left_arm");
		this.head = this.up.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 47).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, -13.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(0, 29).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, -12.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -12.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 9).addBox(-5.0F, -16.0F, -2.0F, 10.0F, 15.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(47, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, -14.0F, 0.0F));


		up.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(29, 24).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(7.0F, -14.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 44).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart getZombieLeftHand() {
		return this.left_arm;
	}

	@Override
	public ModelPart getZombieRightHand() {
		return this.right_arm;
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