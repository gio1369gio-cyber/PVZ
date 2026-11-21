package com.lying.pvz.client.model.entity.zombie.grass;

import java.util.Optional;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
// Made with Blockbench 3.7.1
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。铁桶僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-24,0)，1 个腿方块）
//       └─ up（setPos(0,-24,0)）
//            ├─ body（1 个躯干方块）
//            ├─ left_hand（1 个手方块）+ right_hand（1 个手方块）
//            └─ head（1 个头部方块）
//                 └─ bucket（setPos(0,-13,0) + X -5°，三层铁桶防御）
//                      ├─ defence1（5 个方块，第 3 个 mirror=true）
//                      │    └─ bar（setPos(0,-2,-1) + X 45°，5 个方块，桶沿）
//                      ├─ defence2（1 个方块）
//                      └─ defence3（1 个方块）
//   updateFreeParts：defence3/2/1.visible = entity.hasBucketHead(3/2/1)，铁桶逐层脱落。
//   getHelmet：返回 bucket，掉落手时禁用铁桶渲染。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class BucketHeadZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bucket_head_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart bucket;
	private final ModelPart defence1;
	private final ModelPart defence2;
	private final ModelPart defence3;

	public BucketHeadZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.bucket = this.head.getChild("bucket");
		this.defence1 = this.bucket.getChild("defence1");
		this.defence2 = this.bucket.getChild("defence2");
		this.defence3 = this.bucket.getChild("defence3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		PartDefinition bucket = head.addOrReplaceChild("bucket",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		PartDefinition defence1 = bucket.addOrReplaceChild("defence1",
				CubeListBuilder.create()
						.texOffs(137, 2).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(138, 8).addBox(-9.0F, -2.0F, 8.0F, 18.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(138, 15).addBox(-9.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, CubeDeformation.NONE).mirror()
						.texOffs(138, 38).addBox(8.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(184, 2).addBox(-9.0F, -7.0F, -9.0F, 18.0F, 5.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		defence1.addOrReplaceChild("bar",
				CubeListBuilder.create()
						.texOffs(182, 31).addBox(-11.0F, -1.0F, -15.0F, 22.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(138, 60).addBox(-11.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(137, 82).addBox(10.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(181, 38).addBox(9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(195, 38).addBox(-10.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, 0.7854F, 0.0F, 0.0F));


		bucket.addOrReplaceChild("defence2",
				CubeListBuilder.create()
						.texOffs(181, 48).addBox(-9.0F, -14.0F, -9.0F, 18.0F, 7.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bucket.addOrReplaceChild("defence3",
				CubeListBuilder.create()
						.texOffs(182, 76).addBox(-9.0F, -20.0F, -9.0F, 18.0F, 6.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		this.defence3.visible = state.innerDefenceStage >= 3;
		this.defence2.visible = state.innerDefenceStage >= 2;
		this.defence1.visible = state.innerDefenceStage >= 1;
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

	@Override
	public Optional<ModelPart> getHelmet() {
		return Optional.ofNullable(this.bucket);
	}
}