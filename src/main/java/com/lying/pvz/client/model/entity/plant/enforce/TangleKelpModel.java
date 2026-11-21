package com.lying.pvz.client.model.entity.plant.enforce;


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

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。所有 f1..f4 内部挂着成对的 n_rN / s_rN "细长叶片"（0 厚度平面），
//   这是 1.16.5 用"0 厚度平面 + 旋转"模拟"薄叶片"的经典做法，1.21.1 依然支持（CubeDeformation 无关，仍走 addBox 的 dz=0）。
//
//   业务逻辑（setupAnim）：见方法内注释，1:1 保留。

public class TangleKelpModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("tangle_kelp"), "main");

	private final ModelPart total;

	private final ModelPart c1;
	private final ModelPart c2;
	private final ModelPart f1;
	private final ModelPart f2;
	private final ModelPart f3;
	private final ModelPart f4;

	public TangleKelpModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.c1 = this.total.getChild("c1");
		this.c2 = this.total.getChild("c2");
		this.f1 = this.total.getChild("f1");
		this.f2 = this.total.getChild("f2");
		this.f3 = this.total.getChild("f3");
		this.f4 = this.total.getChild("f4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(94, 0).addBox(-4.5F, -8.0F, -4.0F, 9.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(104, 16).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 8.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("c1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.5F, -18.0F, -5.0F, 11.0F, 19.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		total.addOrReplaceChild("c2",
				CubeListBuilder.create()
						.texOffs(0, 29).addBox(-5.5F, -18.0F, -5.0F, 11.0F, 19.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));



		PartDefinition f1 = total.addOrReplaceChild("f1",
				CubeListBuilder.create()
						.texOffs(54, -10).addBox(0.0F, -22.0F, -5.0F, 0.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		f1.addOrReplaceChild("n_r1",
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		f1.addOrReplaceChild("s_r1",
				CubeListBuilder.create()
						.texOffs(48, 44).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f2 = total.addOrReplaceChild("f2",
				CubeListBuilder.create()
						.texOffs(54, 12).addBox(0.0F, -22.0F, -5.0F, 0.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		f2.addOrReplaceChild("n_r2",
				CubeListBuilder.create()
						.texOffs(48, 22).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		f2.addOrReplaceChild("s_r2",
				CubeListBuilder.create()
						.texOffs(48, 66).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f3 = total.addOrReplaceChild("f3",
				CubeListBuilder.create()
						.texOffs(54, 34).addBox(0.0F, -22.0F, -5.0F, 0.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		f3.addOrReplaceChild("n_r3",
				CubeListBuilder.create()
						.texOffs(48, 44).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		f3.addOrReplaceChild("s_r3",
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f4 = total.addOrReplaceChild("f4",
				CubeListBuilder.create()
						.texOffs(54, 56).addBox(0.0F, -22.0F, -5.0F, 0.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		f4.addOrReplaceChild("n_r4",
				CubeListBuilder.create()
						.texOffs(48, 66).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		f4.addOrReplaceChild("s_r4",
				CubeListBuilder.create()
						.texOffs(48, 22).addBox(-8.0F, -11.0F, 0.0F, 16.0F, 22.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int period = state.existTick% 12;
		this.c1.visible = period > 6;
		this.c2.visible = period <= 6;

		this.f1.visible = false;
		this.f2.visible = false;
		this.f3.visible = false;
		this.f4.visible = false;

		if (period < 3) {
			this.f1.visible = true;
		} else if (period < 6) {
			this.f2.visible = true;
		} else if (period < 9) {
			this.f3.visible = true;
		} else {
			this.f4.visible = true;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}