package com.lying.pvz.client.model.entity.plant.ice;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// IceCabbagePultModel —— 冰甘蓝投掷器
// 形状：底部一颗冰甘蓝（cabbage，底盘 + 内圈 + 主体 + 顶盖），顶部一圈装饰盖（dicoration），
//       甘蓝上方伸出炮臂（pult），炮臂末端发射口（out）内含一颗方冰弹丸（bullet，带一圈 0.5 膨胀的外壳）
public class IceCabbagePultModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ice_cabbage_pult"), "main");

	private final ModelPart total;
	private final ModelPart cabbage;
	private final ModelPart dicoration;
	private final ModelPart pult;
	private final ModelPart out;
	private final ModelPart bullet;

	public IceCabbagePultModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.cabbage = this.total.getChild("cabbage");
		this.dicoration = this.cabbage.getChild("dicoration");
		this.pult = this.total.getChild("pult");
		this.out = this.pult.getChild("out");
		this.bullet = this.out.getChild("bullet");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cabbage = total.addOrReplaceChild("cabbage",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(4, 47).addBox(-6.0F, -0.1F, -6.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 28).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 8.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(32, 20).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		cabbage.addOrReplaceChild("dicoration",
				CubeListBuilder.create()
						.texOffs(0, 15).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 3.0F, 10.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		PartDefinition pult = total.addOrReplaceChild("pult",
				CubeListBuilder.create()
						.texOffs(8, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition out = pult.addOrReplaceChild("out",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(42, 0).addBox(-3.0F, -12.0F, 0.0F, 6.0F, 6.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, -1.309F, 0.0F, 0.0F));

		out.addOrReplaceChild("bullet",
				CubeListBuilder.create()
						.texOffs(0, 45).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 53).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, -9.0F, 1.0F, 1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.attackTime > 0) {
			float percent = 1 - state.attackTime * 1.0f / state.pultAnimTime;
			this.pult.xRot = (1.0f - Mth.abs(Mth.cos(percent * 3.14159f))) * 1.5f;
			this.bullet.visible = (percent < 0.5);
		} else {
			this.pult.xRot = Mth.sin(state.ageInTicks / 10) / 8;
			this.bullet.visible = true;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}