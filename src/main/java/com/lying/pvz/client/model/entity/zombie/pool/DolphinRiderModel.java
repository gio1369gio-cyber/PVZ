package com.lying.pvz.client.model.entity.zombie.pool;


import com.lying.pvz.client.model.entity.zombie.SimpleZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
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
import net.minecraft.util.Mth;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。海豚骑士模型（独立 EntityModel，不继承 PVZZombieModel）：
//     total（根，无方块）
//       ├─ zombie（setPos(0,12,9)，无方块）
//       │    ├─ left_leg（setPos(4,-24,0) + X -45° + Z -30°，2 个方块：腿 + 脚）
//       │    ├─ right_leg（setPos(-4,-24,0) + X -45° + Z +30°，2 个方块：腿 + 脚）
//       │    ├─ body（setPos(0,-31,0)，1 个躯干方块）
//       │    ├─ left_hand（setPos(7,-46,0)，1 个手方块）
//       │    ├─ right_hand（setPos(-7,-46,0)，1 个手方块）
//       │    └─ head（setPos(0,-48,0)，1 个头部方块）
//       └─ dolphin（setPos(0,0,0)，4 个方块：身体 + 嘴 + 尾 + 尾尖）
//            ├─ bone（setPos(0,0,0) + Y -15° + Z -10°，1 个鳍）
//            ├─ bone2（setPos(0,0,0) + Y 15° + Z 10°，1 个鳍）
//            ├─ bone3（setPos(0,-4,32) + X 25°，1 个尾鳍）
//            └─ bone4（setPos(0,-12,10) + X -30°，1 个背鳍）
//   setupAnim：仅 head 跟随视线，其余全部静态。
//   renderToBuffer：8 参数 (MatrixStack, IVertexBuilder, int, int, float×4)
//                   → 5 参数 (PoseStack, VertexConsumer, int, int, int color)
//   setRotationAngle：参数 ModelRenderer → ModelPart；本类未实际调用，但保留工具方法。

public class DolphinRiderModel extends SimpleZombieModel<PvZZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("dolphin_rider"), "main");

	private final ModelPart total;
	private final ModelPart head;

	public DolphinRiderModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.head = this.total.getChild("zombie").getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 12.0F, 9.0F));

		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(112, 100).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 22.0F, -6.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, -24.0F, 0.0F, -0.7854F, 0.0F, -0.5236F));

		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(93, 100).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 22.0F, -6.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, -24.0F, 0.0F, -0.7854F, 0.0F, 0.5236F));


		zombie.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -17.0F, -3.0F, 14.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -31.0F, 0.0F));


		zombie.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(112, 66).addBox(0.0F, -2.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(7.0F, -46.0F, 0.0F));

		zombie.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(112, 66).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, -46.0F, 0.0F));


		zombie.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 104).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -48.0F, 0.0F));

		PartDefinition dolphin = total.addOrReplaceChild("dolphin",
				CubeListBuilder.create()
						.texOffs(43, 1).addBox(-7.0F, -12.0F, -8.0F, 14.0F, 12.0F, 28.0F, CubeDeformation.NONE)
						.texOffs(1, 32).addBox(-3.0F, -5.0F, -20.0F, 6.0F, 4.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(1, 50).addBox(-4.0F, -6.0F, 20.0F, 8.0F, 6.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(2, 67).addBox(-3.0F, -5.0F, 28.0F, 6.0F, 4.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		dolphin.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(3, 77).addBox(8.0F, -1.0F, 0.0F, 16.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2618F, -0.1745F));


		dolphin.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(41, 43).addBox(-24.0F, -1.0F, 0.0F, 17.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.1745F));


		dolphin.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(2, 94).addBox(-6.0F, -1.0F, -1.0F, 12.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 32.0F, 0.4363F, 0.0F, 0.0F));


		dolphin.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(44, 92).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -12.0F, 10.0F, -0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
	}

}