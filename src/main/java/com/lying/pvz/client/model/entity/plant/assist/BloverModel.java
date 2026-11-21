package com.lying.pvz.client.model.entity.plant.assist;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class BloverModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("blover"), "main");

	private final ModelPart total;
	private final ModelPart fan;

	public BloverModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.fan = this.total.getChild("head").getChild("fan");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 9.0F, -0.5F));


		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 24).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(-0.28F))
						.texOffs(26, 20).addBox(-2.25F, -2.0F, -2.14F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(20, 20).addBox(0.25F, -2.0F, -2.14F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(26, 17).addBox(-1.0F, 0.5F, -2.03F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition bone = head.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(8, 29).addBox(-1.0F, 4.0F, 1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.25F, -3.0F));

		bone.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(24, 8).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-0.634F, 0.1699F, 1.0F, 0.0F, 0.0F, -0.5236F));
		bone.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(16, 14).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0981F, 0.634F, 1.0F, 0.0F, 0.0F, -1.0472F));
		bone.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(24, 11).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.2679F, 1.2679F, 1.0F, 0.0F, 0.0F, -1.5708F));
		bone.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(12, 20).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0981F, 1.9019F, 1.0F, 0.0F, 0.0F, -2.0944F));
		bone.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(18, 17).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-0.634F, 2.366F, 1.0F, 0.0F, 0.0F, -2.618F));
		bone.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(24, 14).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 2.5359F, 1.0F, 0.0F, 0.0F, -3.1416F));
		bone.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.634F, 2.366F, 1.0F, 0.0F, 0.0F, 2.618F));
		bone.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(10, 23).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0981F, 1.9019F, 1.0F, 0.0F, 0.0F, 2.0944F));
		bone.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(8, 26).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.2679F, 1.2679F, 1.0F, 0.0F, 0.0F, 1.5708F));
		bone.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(0, 26).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0981F, 0.634F, 1.0F, 0.0F, 0.0F, 1.0472F));
		bone.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(0, 29).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.634F, 0.1699F, 1.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition fan = head.addOrReplaceChild("fan",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, -2.5F));

		PartDefinition bone4 = fan.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(22, 0).addBox(-2.0F, -7.5F, 1.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(12, 17).addBox(-1.5F, -4.6F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(18, 11).addBox(-1.5F, -8.5F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		bone4.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(0, 17).addBox(-2.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.4142F, -4.7198F, 0.0F, 0.0F, 0.0F, -1.0472F));
		bone4.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(2, 19).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.4142F, -4.7198F, 0.0F, 0.0F, 0.0F, 1.0472F));
		bone4.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(1, 21).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, -7.0858F, 0.0F, 0.0F, 0.0F, -0.7854F));
		bone4.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(6, 22).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.5F, -7.0858F, 0.0F, 0.0F, 0.0F, 0.7854F));
		bone4.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(10, 19).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -5.5858F, 0.0F, 0.0F, 0.0F, -1.5708F));
		bone4.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(22, 8).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -6.0858F, 0.0F, 0.0F, 0.0F, 1.5708F));
		bone4.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(22, 14).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -6.0858F, 0.0F, 0.0F, 0.0F, -1.5708F));
		bone4.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(6, 29).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -5.5858F, 0.0F, 0.0F, 0.0F, 1.5708F));


		PartDefinition bone3 = fan.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(12, 0).addBox(-2.0F, -7.5F, 1.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 4).addBox(-1.5F, -4.6F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(24, 6).addBox(-1.5F, -8.5F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		bone3.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(10, 10).addBox(-2.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.4142F, -4.7198F, 0.0F, 0.0F, 0.0F, -1.0472F));
		bone3.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(11, 8).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.4142F, -4.7198F, 0.0F, 0.0F, 0.0F, 1.0472F));
		bone3.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(10, 4).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, -7.0858F, 0.0F, 0.0F, 0.0F, -0.7854F));
		bone3.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(14, 6).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.5F, -7.0858F, 0.0F, 0.0F, 0.0F, 0.7854F));
		bone3.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(16, 4).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -5.5858F, 0.0F, 0.0F, 0.0F, -1.5708F));
		bone3.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(18, 9).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -6.0858F, 0.0F, 0.0F, 0.0F, 1.5708F));
		bone3.addOrReplaceChild("cube_r26",
				CubeListBuilder.create()
						.texOffs(20, 6).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -6.0858F, 0.0F, 0.0F, 0.0F, -1.5708F));
		bone3.addOrReplaceChild("cube_r27",
				CubeListBuilder.create()
						.texOffs(20, 4).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -5.5858F, 0.0F, 0.0F, 0.0F, 1.5708F));


		PartDefinition bone2 = fan.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(8, 12).addBox(-2.0F, -7.5F, 1.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(6, 6).addBox(-1.5F, -4.6F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(3, 8).addBox(-1.5F, -8.5F, 1.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("cube_r28",
				CubeListBuilder.create()
						.texOffs(0, 32).addBox(-2.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.4142F, -4.7198F, 1.0F, 0.0F, 0.0F, -1.0472F));
		bone2.addOrReplaceChild("cube_r29",
				CubeListBuilder.create()
						.texOffs(4, 2).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.4142F, -4.7198F, 1.0F, 0.0F, 0.0F, 1.0472F));
		bone2.addOrReplaceChild("cube_r30",
				CubeListBuilder.create()
						.texOffs(6, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, -7.0858F, 1.0F, 0.0F, 0.0F, -0.7854F));
		bone2.addOrReplaceChild("cube_r31",
				CubeListBuilder.create()
						.texOffs(2, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.5F, -7.0858F, 1.0F, 0.0F, 0.0F, 0.7854F));
		bone2.addOrReplaceChild("cube_r32",
				CubeListBuilder.create()
						.texOffs(4, 12).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -5.5858F, 1.0F, 0.0F, 0.0F, -1.5708F));
		bone2.addOrReplaceChild("cube_r33",
				CubeListBuilder.create()
						.texOffs(6, 10).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -6.0858F, 1.0F, 0.0F, 0.0F, 1.5708F));
		bone2.addOrReplaceChild("cube_r34",
				CubeListBuilder.create()
						.texOffs(6, 26).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.9142F, -6.0858F, 1.0F, 0.0F, 0.0F, -1.5708F));
		bone2.addOrReplaceChild("cube_r35",
				CubeListBuilder.create()
						.texOffs(9, 16).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.9142F, -5.5858F, 1.0F, 0.0F, 0.0F, 1.5708F));

		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(32, 0).addBox(-0.5F, -15.0F, -0.5F, 1.0F, 15.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(36, 0).addBox(-1.0F, -16.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.35F))
						.texOffs(36, 5).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 15.0F, 0.5F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.fan.zRot = state.ageInTicks / 1.5F;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}