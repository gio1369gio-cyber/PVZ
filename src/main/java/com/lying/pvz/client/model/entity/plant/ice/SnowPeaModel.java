package com.lying.pvz.client.model.entity.plant.ice;

import java.util.Optional;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。结构：
//        total
//          ├─ body  (setPos(0,0,0) + xRot -0.0873，1 方块：躯干)
//          │   └─ head (setPos(0,-12,0) + xRot 0.0873，4 方块：后脑 + 头顶 + 嘴 + 下巴)
//          │       └─ hair (7 个旋转叶片，构成豌豆射手的"冠毛")
//          └─ down  (纯容器，下挂 4 片旋转叶片 n_r1 / w_r1 / e_r1 / s_r1)
//
//   2. ⚠️ 继承关系变更：1.16.5 的 PlantShooterModel<T> 提供 getHeadModel()/getBodyModel()
//      返回 Optional<ModelRenderer>，用于外部按头部/身体分别渲染（发射时头部/身体有独立动作）。
//      1.21.1 中若 PlantShooterModel 已迁移，这两个方法签名的返回类型需要相应改为
//      Optional<ModelPart>；本类重写处已按 Optional<ModelPart> 写。
//      如果基类签名未变（仍返回 Optional<ModelRenderer>），需要把重写改为 Optional<ModelPart>
//      并把基类也同步修改。
//
//   3. 该类未提供 setupAnim 重写——原版由 PlantShooterModel 基类提供默认实现，
//      迁移后同样交给基类，不要在本类里补空实现。

public class SnowPeaModel extends PlantShooterModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("snow_pea"), "main");

	private final ModelPart total;
	private final ModelPart body;
	private final ModelPart head;

	public SnowPeaModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.head = this.body.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 31).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(30, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(44, 0).addBox(-3.0F, -6.0F, -9.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(40, 8).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
		PartDefinition hair = head.addOrReplaceChild("hair",
				CubeListBuilder.create()
						.texOffs(0, 56).addBox(-2.0F, -3.5F, 0.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(2, 49).addBox(-1.0F, 0.5F, -1.0F, 3.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-1.0F, -4.5F, 5.0F));

		hair.addOrReplaceChild("u_r1",
				CubeListBuilder.create()
						.texOffs(16, 55).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.5F, -1.0F, 0.0F, 0.2618F, -0.1745F, 0.0873F));

		hair.addOrReplaceChild("m_r1",
				CubeListBuilder.create()
						.texOffs(32, 49).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.5F, -1.0F, 0.0F, -0.1745F, 0.2618F, 0.0F));

		hair.addOrReplaceChild("wu_r1",
				CubeListBuilder.create()
						.texOffs(48, 56).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.5F, -3.0F, 0.0F, 0.5236F, 0.2618F, 0.0F));

		hair.addOrReplaceChild("wd_r1",
				CubeListBuilder.create()
						.texOffs(48, 48).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.5F, 2.0F, 0.0F, -0.5236F, 0.5236F, 0.0F));

		hair.addOrReplaceChild("ed_r1",
				CubeListBuilder.create()
						.texOffs(16, 47).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, 2.0F, 0.0F, -0.4363F, -0.5236F, 0.0F));

		hair.addOrReplaceChild("eu_r1",
				CubeListBuilder.create()
						.texOffs(34, 57).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, -3.0F, 0.0F, 0.5236F, -0.5236F, 0.0F));

		PartDefinition down = total.addOrReplaceChild("down",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		down.addOrReplaceChild("n_r1",
				CubeListBuilder.create()
						.texOffs(4, 20).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.0F));

		down.addOrReplaceChild("w_r1",
				CubeListBuilder.create()
						.texOffs(6, 27).addBox(1.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, -0.1745F));

		down.addOrReplaceChild("e_r1",
				CubeListBuilder.create()
						.texOffs(26, 27).addBox(-7.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.1745F));

		down.addOrReplaceChild("s_r1",
				CubeListBuilder.create()
						.texOffs(24, 20).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

	@Override
	public Optional<ModelPart> getHeadModel() {
		return Optional.ofNullable(this.head);
	}

	@Override
	public Optional<ModelPart> getBodyModel() {
		return Optional.ofNullable(this.body);
	}
}