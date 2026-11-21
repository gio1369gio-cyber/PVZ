package com.lying.pvz.client.model.entity.zombie.roof;


import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;

// AirborneZombieModel —— 空降僵尸
// 形状：整体前倾 0.7854 的僵尸骨架，双腿前踢、双臂前伸、头上仰（吊绳状态）；
//       吊绳断开后回到正常僵尸姿态，由 hasLine 控制两套姿态切换
public class AirborneZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("airborne_zombie"), "main");

	private final ModelPart total;
	private final ModelPart zombie;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public AirborneZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.zombie = this.total.getChild("zombie");
		this.left_leg = this.zombie.getChild("left_leg");
		this.right_leg = this.zombie.getChild("right_leg");
		this.up = this.zombie.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.ZERO);

		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-5.0F, -1.0F, -3.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 13.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -1.0F, -3.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 13.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition up = zombie.addOrReplaceChild("up",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(12.0F, -20.0F, 0.0F, -1.5708F, -0.2182F, 0.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -1.6144F, 0.2182F, 0.0F));

		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, -1.0908F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		final boolean hasLine = state.hasLine;

		if (hasLine) {
			this.zombie.xRot = 1.0472f;
			this.left_leg.xRot = -0.7854f;
			this.right_leg.xRot = -0.7854f;
			this.up.xRot = 0.2618f;
			this.left_hand.xRot = -1.7453f;
			this.right_hand.xRot = -1.7453f;
			this.head.xRot = -1.0472f;
			this.head.yRot = 0.0f;
			this.updateFreeParts(state);
		} else {
			this.up.xRot = 0.0f;
			this.zombie.xRot = -0.75f;
			super.setupAnim(state);
		}
		this.updateFreeParts(state);
	}

	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasLine = state.hasLine;
		this.isLeftHandFree = !hasLine;
		this.isRightHandFree = !hasLine;
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_leg.xRot = 0.0f;
		this.right_leg.xRot = 0.0f;
		this.up.xRot = 0.0f;
		this.head.xRot = 0.0f;
		this.left_hand.xRot = 0.0f;
		this.right_hand.xRot = 0.0f;
		this.zombie.xRot = 0.0f;
		super.tickPartAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}


	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		super.renderBody(entity, stack, collector, renderType, packedLight, packedOverlay);
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