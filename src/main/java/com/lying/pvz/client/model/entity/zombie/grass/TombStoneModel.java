package com.lying.pvz.client.model.entity.zombie.grass;


import com.lying.pvz.client.model.entity.zombie.SimpleZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。墓碑模型（独立 EntityModel，不继承 PVZZombieModel）：
//     total（根，5 个方块：底座 + 顶盖 + 左右竖条 + 正面碑面）
//       ├─ logo（setPos(0,0,0)，5 个方块，灰色图案）
//       └─ red_logo（setPos(0,0,0)，5 个方块，红色图案）
//   setupAnim：entity instanceof GigaTombStoneEntity 时切 red_logo.visible，
//              否则 logo.visible——两者互斥。
//   renderToBuffer：8 参数 (MatrixStack, IVertexBuilder, int, int, float×4)
//                   → 5 参数 (PoseStack, VertexConsumer, int, int, int color)
//   setRotationAngle：参数 ModelRenderer → ModelPart；本类未实际调用，但保留工具方法。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class TombStoneModel extends SimpleZombieModel<PvZZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("tomb_stone"), "main");

	private final ModelPart total;
	private final ModelPart logo;
	private final ModelPart red_logo;

	public TombStoneModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.logo = this.total.getChild("logo");
		this.red_logo = this.total.getChild("red_logo");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(56, 105).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 4.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(56, 81).addBox(-9.0F, -38.0F, -9.0F, 18.0F, 4.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(0, 82).addBox(-8.0F, -34.0F, -8.0F, 2.0F, 30.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(92, 0).addBox(6.0F, -34.0F, -8.0F, 2.0F, 30.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(3, 5).addBox(-6.0F, -34.0F, -7.0F, 12.0F, 30.0F, 15.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("logo",
				CubeListBuilder.create()
						.texOffs(0, 51).addBox(-3.0F, -30.0F, -7.1F, 6.0F, 7.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 60).addBox(-4.0F, -29.0F, -7.1F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(6, 60).addBox(3.0F, -29.0F, -7.1F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 68).addBox(-2.0F, -31.0F, -7.1F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 72).addBox(-2.0F, -23.0F, -7.1F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		total.addOrReplaceChild("red_logo",
				CubeListBuilder.create()
						.texOffs(18, 51).addBox(-3.0F, -30.0F, -7.1F, 6.0F, 7.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 60).addBox(-4.0F, -29.0F, -7.1F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(22, 60).addBox(3.0F, -29.0F, -7.1F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 68).addBox(-2.0F, -31.0F, -7.1F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(14, 72).addBox(-2.0F, -23.0F, -7.1F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		final boolean red = state.entityType == PvZEntityRegister.GIGA_TOMB_STONE.get();
		this.red_logo.visible = red;
		this.logo.visible = !red;
	}


}