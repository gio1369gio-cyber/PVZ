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


// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。结构：
//     total（1 方块：菇柄主体 8×7×8）
//       ├─ reye（1 个 0 厚度薄片：右眼，setPos(-3, -4, -4.25)）
//       ├─ leye（1 个 0 厚度薄片：左眼，setPos(3, -4, -4.25)）
//       └─ hat（3 个方块：三层伞盖 12→10→6）
//
//   setupAnim：hat.yRot = ageInTicks / 30 —— 帽子缓慢旋转（约 31 秒一圈）
//   ⚠️ 1.16.5 里还有两行被注释掉的 leye/reye zRot 逻辑，
//      迁移时**保留这段注释原样**，方便日后想恢复"眼睛转圈"时有据可查。

public class HypnoShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("hypno_shroom"), "main");

	private final ModelPart total;
	private final ModelPart hat;

	public HypnoShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.hat = this.total.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 32).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("reye",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, -4.0F, -4.25F));

		total.addOrReplaceChild("leye",
				CubeListBuilder.create()
						.texOffs(0, 4).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, -4.0F, -4.25F));

		total.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 17).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 5.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(30, 17).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 3.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		this.hat.yRot = state.ageInTicks / 30;

	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}