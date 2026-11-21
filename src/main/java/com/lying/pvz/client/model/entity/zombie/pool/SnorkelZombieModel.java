package com.lying.pvz.client.model.entity.zombie.pool;


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




public class SnorkelZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("snorkel_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart body;
	private final ModelPart head;

	public SnorkelZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.body = this.up.getChild("body");
		this.head = this.up.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition right_leg = total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(1, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -25.0F, 0.0F));


		right_leg.addOrReplaceChild("right_foot",
				CubeListBuilder.create()
						.texOffs(22, 73).addBox(-2.0F, -1.0F, -11.0F, 4.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(56, 95).addBox(2.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(65, 81).addBox(-3.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition left_leg = total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(57, 2).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -25.0F, 0.0F));


		left_leg.addOrReplaceChild("left_foot",
				CubeListBuilder.create()
						.texOffs(62, 62).addBox(-2.0F, -1.0F, -11.0F, 4.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(49, 64).addBox(2.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(60, 50).addBox(-3.0F, -1.0F, -10.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(94, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 25.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -23.0F, 0.0F));


		PartDefinition armor = left_hand.addOrReplaceChild("armor",
				CubeListBuilder.create()
						.texOffs(76, 3).addBox(0.0F, -2.0F, -3.0F, 1.0F, 5.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, 3.0F, 0.0F));


		armor.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(79, 18).addBox(-0.866F, -2.0F, -0.5F, 1.0F, 5.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, -1.1345F, 0.0F));


		armor.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(80, 35).addBox(0.0F, -3.0F, -4.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 1.1345F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(112, 1).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 25.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-10.0F, -23.0F, 0.0F));


		PartDefinition bone3 = right_hand.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(80, 89).addBox(-5.0F, 3.0F, -3.0F, 1.0F, 5.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, -2.0F, 0.0F));


		bone3.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(76, 103).addBox(-1.0F, -2.0F, -4.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 5.0F, -2.0F, 0.0F, -1.1345F, 0.0F));


		right_hand.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(96, 47).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, 3.0F, 2.0F, 0.0F, 1.1345F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(1, 93).addBox(-8.0F, -25.0F, -3.0F, 16.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(80, 104).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 89).addBox(6.0F, -8.0F, -6.0F, 1.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 73).addBox(-7.0F, -8.0F, -6.0F, 1.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(102, 68).addBox(-6.0F, -8.0F, 6.0F, 12.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(102, 60).addBox(-6.0F, -9.0F, -7.0F, 12.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(110, 56).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(110, 52).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		PartDefinition bone6 = head.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(118, 45).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(110, 39).addBox(-8.0F, -2.0F, -2.0F, 7.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -7.0F));


		bone6.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(40, 109).addBox(-1.6428F, -1.6943F, -0.3237F, 2.0F, 2.0F, 17.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -1.0F, 0.0F, 1.309F, -0.6981F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
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