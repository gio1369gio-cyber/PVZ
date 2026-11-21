package com.lying.pvz.client.model.entity.plant.assist;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// FodderBushModel —— 饲料灌木
// 形状：底部两个交错的大方块芯（c1 高、c2 略矮），外加一个矮底座（total 自带 box），
//       四周四组朝北/朝南倾斜的薄片叶片（f1~f4，每组由 n_rX 与 s_rX 两张无厚度平面组成）
public class FodderBushModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("fodder_bush"), "main");

	private final ModelPart total;
	private final ModelPart c1;
	private final ModelPart c2;
	private final ModelPart f1;
	private final ModelPart n_r1;
	private final ModelPart s_r1;
	private final ModelPart f2;
	private final ModelPart n_r2;
	private final ModelPart s_r2;
	private final ModelPart f3;
	private final ModelPart n_r3;
	private final ModelPart s_r3;
	private final ModelPart f4;
	private final ModelPart n_r4;
	private final ModelPart s_r4;
	private final ModelPart getPlantWholeBody;

	public FodderBushModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.c1 = this.total.getChild("c1");
		this.c2 = this.total.getChild("c2");
		this.f1 = this.total.getChild("f1");
		this.n_r1 = this.f1.getChild("n_r1");
		this.s_r1 = this.f1.getChild("s_r1");
		this.f2 = this.total.getChild("f2");
		this.n_r2 = this.f2.getChild("n_r2");
		this.s_r2 = this.f2.getChild("s_r2");
		this.f3 = this.total.getChild("f3");
		this.n_r3 = this.f3.getChild("n_r3");
		this.s_r3 = this.f3.getChild("s_r3");
		this.f4 = this.total.getChild("f4");
		this.n_r4 = this.f4.getChild("n_r4");
		this.s_r4 = this.f4.getChild("s_r4");
		// 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
		this.getPlantWholeBody = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(94, 0).addBox(-3.5F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("c1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.5F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		total.addOrReplaceChild("c2",
				CubeListBuilder.create()
						.texOffs(0, 29).addBox(-4.5F, -15.0F, -5.0F, 10.0F, 16.0F, 10.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		PartDefinition f1 = total.addOrReplaceChild("f1", CubeListBuilder.create(), PartPose.ZERO);
		f1.addOrReplaceChild("n_r1",
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-7.0F, -2.0F, 0.0F, 15.0F, 13.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
		f1.addOrReplaceChild("s_r1",
				CubeListBuilder.create()
						.texOffs(48, 44).addBox(-7.0F, -6.0F, 0.0F, 15.0F, 17.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f2 = total.addOrReplaceChild("f2", CubeListBuilder.create(), PartPose.ZERO);
		f2.addOrReplaceChild("n_r2",
				CubeListBuilder.create()
						.texOffs(48, 22).addBox(-7.0F, -5.0F, 0.0F, 15.0F, 16.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
		f2.addOrReplaceChild("s_r2",
				CubeListBuilder.create()
						.texOffs(48, 66).addBox(-7.0F, -5.0F, 0.0F, 15.0F, 16.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f3 = total.addOrReplaceChild("f3", CubeListBuilder.create(), PartPose.ZERO);
		f3.addOrReplaceChild("n_r3",
				CubeListBuilder.create()
						.texOffs(48, 44).addBox(-7.0F, -6.0F, 0.0F, 15.0F, 17.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
		f3.addOrReplaceChild("s_r3",
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-7.0F, -6.0F, 0.0F, 15.0F, 17.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition f4 = total.addOrReplaceChild("f4", CubeListBuilder.create(), PartPose.ZERO);
		f4.addOrReplaceChild("n_r4",
				CubeListBuilder.create()
						.texOffs(48, 66).addBox(-7.0F, -6.0F, 0.0F, 15.0F, 17.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
		f4.addOrReplaceChild("s_r4",
				CubeListBuilder.create()
						.texOffs(48, 22).addBox(-7.0F, -9.0F, 0.0F, 15.0F, 20.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}