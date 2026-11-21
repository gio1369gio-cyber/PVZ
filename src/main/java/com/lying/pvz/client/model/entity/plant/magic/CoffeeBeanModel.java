package com.lying.pvz.client.model.entity.plant.magic;

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
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。结构极简：
//        total (setPos(0, 20, 0)) —— 注意 y=20 而非 24，咖啡豆整体上抬 4 格
//          ├─ body (setPos(0, 10, 0)) —— 纯容器，无方块，只挂 2 个 cube_r
//          │   ├─ cube_r1（咖啡豆主体，-15° 前倾）
//          │   └─ cube_r2（咖啡豆"裂缝"面，-40° 前倾）
//          ├─ right_wing（空部件，无方块）
//          └─ left_wing（空部件，无方块）
//
//   2. ⚠️ right_wing / left_wing 在 1.16.5 就是**空 ModelRenderer**（没有 addBox），
//      只靠 setupAnim 改 zRot。这对渲染结果无影响（空部件不参与几何渲染），
//      但保留了字段和动画调用——可能是原版作者预留的"翅膀"位置。
//      迁移后保持原样：空 PartDefinition + setupAnim 里改 zRot。
//
//   3. MathHelper → Mth：sin 方法签名不变，只换类名。

public class CoffeeBeanModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("coffee_bean"), "main");

	private final ModelPart total;

	private final ModelPart right_wing;
	private final ModelPart left_wing;

	public CoffeeBeanModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_wing = this.total.getChild("right_wing");
		this.left_wing = this.total.getChild("left_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 20.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 10.0F, 0.0F));

		body.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, -0.2618F, 0.0F, 0.0F));


		body.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 9).addBox(-3.0F, -7.0F, 0.5F, 6.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

		total.addOrReplaceChild("right_wing",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 10.0F, 0.0F));

		total.addOrReplaceChild("left_wing",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 10.0F, 0.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.left_wing.zRot = Mth.sin(state.ageInTicks / 10) * 3.14159f / 4;
		this.right_wing.zRot = -Mth.sin(state.ageInTicks / 10) * 3.14159f / 4;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}