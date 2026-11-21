package com.lying.pvz.client.model.armor;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;


public class GigaArmorModel extends HumanoidModel<HumanoidRenderState> {
	public static final ModelLayerLocation GIGA_HEAD_LAYER = new ModelLayerLocation(StringUtil.id("giga_head"), "main");
	public static final ModelLayerLocation GIGA_CHEST_LAYER = new ModelLayerLocation(StringUtil.id("giga_chest"), "main");

	private final ModelPart helmet;
	private final ModelPart chestplate;
	private final ModelPart right_hand_armor;
	private final ModelPart left_hand_armor;
	private final ModelPart bone2;
	private final ModelPart bone;
	private final EquipmentSlot slot;

	public GigaArmorModel(ModelPart root, EquipmentSlot slot) {
		super(root);
		this.slot = slot;

		this.head.skipDraw     = true;
		this.hat.skipDraw      = true;
		this.body.skipDraw     = true;
		this.rightArm.skipDraw = true;
		this.leftArm.skipDraw  = true;
		this.rightLeg.skipDraw = true;
		this.leftLeg.skipDraw  = true;

		this.helmet           = this.head.getChild("helmet");
		this.chestplate       = this.body.getChild("chestplate");
		this.right_hand_armor = this.rightArm.getChild("right_hand_armor");
		this.left_hand_armor  = this.leftArm.getChild("left_hand_armor");
		this.bone2            = this.right_hand_armor.getChild("bone2");
		this.bone             = this.left_hand_armor.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		PartDefinition head = root.getChild("head");
		PartDefinition body = root.getChild("body");
		PartDefinition rightArm = root.getChild("right_arm");
		PartDefinition leftArm = root.getChild("left_arm");

		head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -5.0F, 8.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-4.0F, -8.0F, 4.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-5.0F, -8.0F, -5.0F, 1.0F, 8.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(20, 10).addBox(4.0F, -8.0F, -5.0F, 1.0F, 8.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(18, 27).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 29).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 31).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 31).addBox(2.0F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 31).addBox(-3.0F, -8.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(30, 31).addBox(2.0F, -8.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-4.0F, -12.0F, 2.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 0).addBox(-5.0F, -9.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(18, 0).addBox(4.0F, -9.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_hand_armor = rightArm.addOrReplaceChild("right_hand_armor", CubeListBuilder.create().texOffs(28, 0).addBox(-5.0F, -2.0F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 28).addBox(-2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 13).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 28).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(10, 31).addBox(-3.0F, -2.0F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 10).addBox(-4.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 33).addBox(-5.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 10).addBox(-4.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 33).addBox(-3.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(30, 33).addBox(-5.0F, -2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 0.0F));

		right_hand_armor.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(18, 18).addBox(-3.5355F, -1.7071F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition left_hand_armor = leftArm.addOrReplaceChild("left_hand_armor", CubeListBuilder.create().texOffs(28, 5).addBox(2.0F, -2.0F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(18, 23).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(26, 28).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 33).addBox(2.0F, -2.0F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 33).addBox(3.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(34, 13).addBox(4.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 33).addBox(3.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(10, 31).addBox(2.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(34, 15).addBox(4.0F, -2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 31).addBox(-2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		left_hand_armor.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 26).addBox(-0.4645F, -1.7071F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		super.setupAnim(state);
		boolean headSlot  = this.slot == EquipmentSlot.HEAD;
		boolean chestSlot = this.slot == EquipmentSlot.CHEST;

		this.helmet.visible           = headSlot;
		this.chestplate.visible       = chestSlot;
		this.right_hand_armor.visible = chestSlot;
		this.left_hand_armor.visible  = chestSlot;
	}
}