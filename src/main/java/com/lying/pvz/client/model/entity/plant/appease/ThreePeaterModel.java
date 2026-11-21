package com.lying.pvz.client.model.entity.plant.appease;



import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ThreePeaterModel extends PlantShooterModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("three_peater"), "main");

	private final ModelPart total;

	private final ModelPart body;
	private final ModelPart left_up;
	private final ModelPart left_head;
	private final ModelPart mid_up;
	private final ModelPart mid_head;
	private final ModelPart right_up;
	private final ModelPart right_head;

	public ThreePeaterModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.left_up = this.body.getChild("left_up");
		this.left_head = this.left_up.getChild("left_head");
		this.mid_up = this.body.getChild("mid_up");
		this.mid_head = this.mid_up.getChild("mid_head");
		this.right_up = this.body.getChild("right_up");
		this.right_head = this.right_up.getChild("right_head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("leaves",
				CubeListBuilder.create()
						.texOffs(14, 30).addBox(0.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(13, 36).addBox(-2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(1, 30).addBox(-2.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(1, 35).addBox(-4.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(2, 45).addBox(2.0F, -1.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 55).addBox(-3.0F, -1.0F, 3.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(41, 1).addBox(-7.0F, -1.0F, -2.0F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(42, 30).addBox(-3.0F, -1.0F, -6.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(1.0F, 0.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition left_up = body.addOrReplaceChild("left_up",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		left_up.addOrReplaceChild("left_body",
				CubeListBuilder.create()
						.texOffs(34, 38).addBox(0.0F, -3.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(-1.0F, -13.0F, 0.0F));

		PartDefinition left_head = left_up.addOrReplaceChild("left_head",
				CubeListBuilder.create()
						.texOffs(38, 15).addBox(-2.0F, -4.0F, -7.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(1, 19).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(46, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-0.7071F, -15.7071F, 0.0F, 0.0F, -0.1309F, -0.7854F));


		PartDefinition bone7 = left_head.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, 0.6109F, 0.7418F, 0.0F));

		PartDefinition bone8 = bone7.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.48F, 0.0F, 0.0F));

		bone8.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.3927F, 0.0F, 0.0F));


		PartDefinition bone13 = left_head.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, 0.6109F, -0.6981F, 0.0F));

		PartDefinition bone14 = bone13.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.48F, 0.0F, 0.0F));

		bone14.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.3927F, 0.0F, 0.0F));


		PartDefinition bone16 = left_head.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bone17 = bone16.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.7854F, 0.0F, 0.0F));

		bone17.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.309F, 0.0F, 0.0F));

		PartDefinition mid_up = body.addOrReplaceChild("mid_up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		mid_up.addOrReplaceChild("mid_body",
				CubeListBuilder.create()
						.texOffs(55, 44).addBox(0.0F, -2.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(-1.0F, -16.0F, 0.0F));

		PartDefinition mid_head = mid_up.addOrReplaceChild("mid_head",
				CubeListBuilder.create()
						.texOffs(38, 15).addBox(-2.0F, -4.0F, -7.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(1, 19).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(46, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -18.0F, 0.0F));


		PartDefinition bone4 = mid_head.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, 0.6109F, 0.6981F, 0.0F));

		PartDefinition bone5 = bone4.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.48F, 0.0F, 0.0F));

		bone5.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone10 = mid_head.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, 0.6109F, -0.6981F, 0.0F));

		PartDefinition bone11 = bone10.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.48F, 0.0F, 0.0F));

		bone11.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition right_up = body.addOrReplaceChild("right_up",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		right_up.addOrReplaceChild("right_body",
				CubeListBuilder.create()
						.texOffs(45, 46).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition right_head = right_up.addOrReplaceChild("right_head",
				CubeListBuilder.create()
						.texOffs(38, 15).addBox(-2.0F, -4.0F, -7.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(1, 19).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(46, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.7071F, -15.7071F, 0.0F, 0.0F, 0.1309F, 0.7854F));


		PartDefinition bone = right_head.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(3, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(54, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8192F, 0.4264F, -0.48F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(33, 57).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final float OFFSET = 0.78F;
		this.left_head.xRot = 0;
		this.mid_head.xRot = 0;
		this.right_head.xRot = 0;
		this.body.xRot = 0;
		this.right_up.zRot = -OFFSET;
		this.right_head.zRot = OFFSET;
		this.left_up.zRot = OFFSET;
		this.left_head.zRot = -OFFSET;
		if (state.isPlantInSuperMode) {
			final int T = PlantShooterEntity.SHOOT_ANIM_CD;
			final int tick = state.superTime % T;
			if (tick >= 0) {
				this.left_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle() / 5);
				this.mid_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle() / 5);
				this.right_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle() / 5);
				this.body.xRot = AnimationUtil.getUpDownUpDown(tick, T, -getMaxRotAngle() / 5);
			}
		} else {
			final int T = PlantShooterEntity.SHOOT_ANIM_CD;
			final int tick = state.shootTick + T - state.shootCD;
			if (tick >= 0) {
				this.left_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle());
				this.mid_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle());
				this.right_head.xRot = AnimationUtil.getUpDownUpDown(tick, T, getMaxRotAngle());
				this.body.xRot = AnimationUtil.getUpDownUpDown(tick, T, -getMaxRotAngle());
			} else { // idle
				final int TT = 40;
				final int tt = state.existTick % TT;
				final float CHANGE = 3;
				this.mid_up.zRot = AnimationUtil.getUpDownUpDown(tt, TT, CHANGE / 4);
				this.left_up.zRot = AnimationUtil.getUpDownUpDown(tt, TT, CHANGE) + OFFSET;
				this.left_head.zRot = -AnimationUtil.getUpDownUpDown(tt, TT, CHANGE) - OFFSET;
				this.right_up.zRot = -AnimationUtil.getUpDownUpDown(tt, TT, CHANGE) - OFFSET;
				this.right_head.zRot = AnimationUtil.getUpDownUpDown(tt, TT, CHANGE) + OFFSET;
			}
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}