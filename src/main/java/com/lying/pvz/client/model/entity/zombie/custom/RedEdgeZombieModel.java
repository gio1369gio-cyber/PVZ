package com.lying.pvz.client.model.entity.zombie.custom;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// RedEdgeZombieModel —— 红边僵尸
// 形状：标准僵尸骨架 + 左手（sword 下 cross_r1）一把十字刃
public class RedEdgeZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("red_edge_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart sword;
	private final ModelPart cross_r1;
	private final ModelPart right_hand;
	private final ModelPart head;

	public RedEdgeZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.sword = this.left_hand.getChild("sword");
		this.cross_r1 = this.sword.getChild("cross_r1");
		this.right_hand = this.up.getChild("right_hand");
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
						.texOffs(0, 32).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 32).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(32, 32).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(80, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		PartDefinition sword = left_hand.addOrReplaceChild("sword",
				CubeListBuilder.create(),
				PartPose.ZERO);

		sword.addOrReplaceChild("cross_r1",
				CubeListBuilder.create()
						.texOffs(102, 113).addBox(-2.0F, -13.0F, -21.0F, 2.0F, 3.0F, 13.0F, CubeDeformation.NONE)
						.texOffs(22, 82).addBox(-1.0F, -47.0F, -17.0F, 1.0F, 34.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(36, 112).addBox(0.0F, -16.0F, -17.0F, 1.0F, 13.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(80, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public ModelPart getZombieLeftHand() {
		return this.right_hand;
	}

	@Override
	public ModelPart getZombieRightHand() {
		return this.left_hand;
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