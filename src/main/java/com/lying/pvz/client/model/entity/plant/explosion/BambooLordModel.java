package com.lying.pvz.client.model.entity.plant.explosion;


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
// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运，全部含多种膨胀（-0.2 / -0.1 / 0.2 / 0.25 / 0.3），逐条映射为 CubeDeformation。
//   结构：
//     total（根）
//       ├─ worm    （竹虫，3 节斜悬在左侧；仅在 getAttackTime()==0 时可见）
//       ├─ body    （竹筒主体 + 4 个方块 + cube_r4/cube_r5）
//       │   ├─ down（下段 2 个方块）
//       │   └─ head（头部 4 个侧面方块，绕 Z 轴轻微偏转）
//       ├─ face    （2 只眼睛，膨胀 0.3）
//       └─ leaves  （3 片叶，每片 leafN → cube_rN + boneN → boneN+1）
//
//   业务逻辑（setupAnim）：worm.visible = (attackTime == 0)，
//   即"闲置时钻出竹虫、攻击时缩回"。1.16.5 原样，保留。

public class BambooLordModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("bamboo_lord"), "main");

	private final ModelPart total;

	private final ModelPart worm;

	public BambooLordModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.worm = this.total.getChild("worm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition worm = total.addOrReplaceChild("worm",
				CubeListBuilder.create(),
				PartPose.offset(1.0F, -10.25F, 0.0F));

		worm.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 18).addBox(-0.5F, 5.4F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(-10.2695F, -6.5331F, 0.0F, 0.0F, 0.0F, -1.9199F));

		worm.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(18, 0).addBox(-1.0F, 4.8F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(-7.3079F, -12.6447F, 0.0F, 0.0F, 0.0F, -0.8727F));

		worm.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(14, 30).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(-2.25F, -8.5F, 0.0F, 0.0F, 0.0F, -0.3054F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(24, 0).addBox(-2.25F, -9.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F))
						.texOffs(0, 14).addBox(-3.5F, -1.5F, -4.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(-0.1F))
						.texOffs(21, 21).addBox(-3.5F, -1.0F, -4.5F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(0, 45).addBox(-2.0F, -13.25F, -3.0F, 5.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		body.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(21, 8).addBox(-3.0F, -0.7F, -5.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.25F)),
				PartPose.offsetAndRotation(0.6351F, -9.1122F, 1.001F, 0.0F, 0.0F, -0.1309F));


		body.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(21, 15).addBox(-2.0F, -3.7F, -8.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -9.0F, 5.0F, 0.0F, 0.0F, -0.3054F));


		PartDefinition down = body.addOrReplaceChild("down",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		down.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-3.0F, -8.0F, -5.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(0.0F, -0.25F, 1.0F, 0.0F, 0.0F, 0.0873F));

		down.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, -8.0F, -5.0F, 6.0F, 8.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0873F));


		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create(),
				PartPose.offset(-0.25F, 0.0F, 0.0F));

		head.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(0, 30).addBox(2.0F, -7.7F, -5.0F, 1.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(18, 29).addBox(-3.0F, -7.7F, -5.0F, 1.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(32, 39).addBox(-2.0F, -7.7F, 0.0F, 4.0F, 8.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(42, 42).addBox(-2.0F, -7.7F, -5.0F, 4.0F, 8.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -9.0F, 1.0F, 0.0F, 0.0F, -0.0436F));


		total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(21, 15).addBox(1.5F, -12.5F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F))
						.texOffs(21, 17).addBox(-1.5F, -12.5F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition leaf1 = leaves.addOrReplaceChild("leaf1",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		leaf1.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(18, 18).addBox(0.0F, -2.0F, -4.0F, 0.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, -0.0436F, -0.1309F));

		PartDefinition bone2 = leaf1.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		bone2.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(26, 26).addBox(0.0F, -2.0F, -3.5F, 0.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 13).addBox(-5.0F, -6.0F, -3.0F, 0.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.162F, -1.906F, 0.1305F, 0.0F, 0.0F, -0.4363F));

		bone3.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(4, 4).addBox(-5.0F, -7.0F, -2.0F, 0.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition leaf2 = leaves.addOrReplaceChild("leaf2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 2.0F, 1.0F, 0.0F, -1.5708F, 0.0436F));

		leaf2.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(18, 21).addBox(0.0F, -2.0F, -3.5F, 0.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition bone6 = leaf2.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(0, 11).addBox(-5.0F, -6.0F, -3.0F, 0.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.162F, -1.906F, 0.1305F, 0.0F, 0.0F, -0.4363F));

		bone6.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(2, 4).addBox(-5.0F, -7.0F, -2.0F, 0.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition leaf3 = leaves.addOrReplaceChild("leaf3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 2.0F, -2.0F, 3.1416F, 0.0F, -3.098F));

		leaf3.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(18, 0).addBox(0.0F, -2.0F, -3.5F, 0.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition bone9 = leaf3.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -6.0F, -3.0F, 0.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.162F, -1.906F, 0.1305F, 0.0F, 0.0F, -0.4363F));

		bone9.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(0, 4).addBox(-5.0F, -7.0F, -2.0F, 0.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.worm.visible = (state.attackTime == 0);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}