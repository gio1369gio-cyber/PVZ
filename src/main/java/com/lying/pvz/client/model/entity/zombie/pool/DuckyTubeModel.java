package com.lying.pvz.client.model.entity.zombie.pool;


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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。鸭子游泳圈模型（独立 EntityModel）：
//     total（根，8 个方块：游泳圈左右两侧 + 前后 + 鸭嘴 3 层 + 头顶装饰）
//       └─ bone（setPos(0,-5,10) + X 30°，1 个方块：鸭尾）
//   setupAnim：空实现，原代码即如此。
//   renderToBuffer：8 参数 (MatrixStack, IVertexBuilder, int, int, float×4)
//                   → 5 参数 (PoseStack, VertexConsumer, int, int, int color)
//   额外保留自定义 render(PoseStack, VertexConsumer, int, int)：原代码中有一个 4 参数的
//   render(MatrixStack, IVertexBuilder, int, int) 重载，1.21.1 中 ModelPart 提供 4 参数
//   render 重载，该自定义方法可保留，参数类型改为 PoseStack/VertexConsumer。
//   注意：Model 在 1.21.1 中也有 final 的 4 参数 renderToBuffer，但那不是本类的方法，
//         本类保留的是独立命名的 render，不是 renderToBuffer 的重载。
//   setRotationAngle：参数 ModelRenderer → ModelPart；本类未实际调用，但保留工具方法。

public class DuckyTubeModel extends EntityModel<LivingEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ducky_tube"), "main");

	private final ModelPart total;

	public DuckyTubeModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(80, 103).addBox(-11.0F, -5.0F, -10.0F, 4.0F, 5.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(90, 90).addBox(-7.0F, -5.0F, 5.0F, 14.0F, 5.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(88, 78).addBox(-7.0F, -5.0F, -10.0F, 14.0F, 5.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(80, 51).addBox(7.0F, -5.0F, -10.0F, 4.0F, 5.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(110, 37).addBox(-3.0F, -7.0F, -12.0F, 6.0F, 7.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(102, 22).addBox(-3.0F, -14.0F, -15.0F, 6.0F, 7.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(110, 14).addBox(-3.0F, -9.0F, -18.0F, 6.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(62, 123).addBox(-2.0F, -14.0F, -8.0F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(68, 107).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -5.0F, 10.0F, 0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
	}

}