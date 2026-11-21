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
//   1. 几何 1:1 搬运。骨架 + 巨型高坚果头（10 方块堆叠）。
//   2. 头部与 TallNutModel（非僵尸版）的 body 一致：
//        20×40×20 主坚果 + 上下沿 + 左右/前后各两片"外壳"，共 10 方块。
//   3. ⚠️ 与 PeaShooter/Squash/Pumpkin 不同，此模型 up 下的子节点**顺序**是
//        body → head → left_hand → right_hand
//      而 right_leg/left_leg 在 up 之后。原版如此，保留。
//   4. 无 setupAnim 重写。

public class TallNutZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("tall_nut_zombie"), "main");

	private final ModelPart total;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public TallNutZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.head = this.up.getChild("head");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(170, 190).addBox(-10.0F, -40.0F, -10.0F, 20.0F, 40.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(182, 156).addBox(-9.0F, -41.0F, -9.0F, 18.0F, 1.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(216, 5).addBox(-11.0F, -39.0F, -9.0F, 1.0F, 38.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(5, 207).addBox(11.0F, -37.0F, -7.0F, 1.0F, 34.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(42, 170).addBox(10.0F, -39.0F, -9.0F, 1.0F, 38.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(93, 178).addBox(-12.0F, -37.0F, -7.0F, 1.0F, 34.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(217, 68).addBox(-9.0F, -39.0F, -11.0F, 18.0F, 38.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(176, 3).addBox(-7.0F, -37.0F, -12.0F, 14.0F, 34.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(159, 52).addBox(-9.0F, -39.0F, 10.0F, 18.0F, 38.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(124, 112).addBox(-7.0F, -37.0F, 11.0F, 14.0F, 34.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

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