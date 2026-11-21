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
import net.minecraft.util.Mth;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。僵尸海豚模型（独立 EntityModel，泛型是 ZombieDolphinEntity 而非 Entity）：
//     body（根，setPos(0, 24, -3)，1 个 8×7×13 躯干方块）
//       ├─ head（setPos(0,0,0)，1 个 8×7×6 头方块）
//       │    └─ nose（setPos(0,0,-10)，1 个 4×3×6 吻部）
//       ├─ tail（setPos(0,-2.5,14) + X -5°，1 个 4×5×11 尾段）
//       │    └─ tail_fin（setPos(0,0,9) + X -8°，1 个 10×1×6 尾鳍）
//       ├─ back_fin（setPos(0,-7,5) + X 60°，1 个 1×4×5 背鳍）
//       ├─ left_fin（setPos(3,-2,5) + X 55° + Z 107°，1 个 1×4×7 左鳍）
//       └─ right_fin（setPos(-3,-2,5) + X 55° + Z -107°，1 个 1×4×7 右鳍，mirror=true）
//   setupAnim：
//     - body.xRot / body.yRot 跟随 headPitch / netHeadYaw
//     - 当 getDeltaMovement() 的水平距离平方 > 1e-7 时（即海豚在移动）：
//       body.xRot += -0.05 - 0.05*cos(ageInTicks*0.3)
//       tail.xRot = -0.1*cos(ageInTicks*0.3)
//       tail_fin.xRot = -0.2*cos(ageInTicks*0.3)
//       —— 这是"游动时身体上下摆动 + 尾巴左右摆动"的经典曲线
//   renderToBuffer：8 参数 → 5 参数
//   MathHelper → Mth：cos 方法不变
//   Entity.getHorizontalDistanceSqr 在 1.21.1 中仍存在（Vec3 的静态方法）

public class ZombieDolphinModel extends SimpleZombieModel<PvZZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("zombie_dolphin"), "main");

	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart tail_fin;

	public ZombieDolphinModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.tail = this.body.getChild("tail");
		this.tail_fin = this.tail.getChild("tail_fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition body = root.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(21, 43).addBox(-4.0F, -7.0F, 0.0F, 8.0F, 7.0F, 13.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, -3.0F));

		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(-4.0F, -7.0F, -6.0F, 8.0F, 7.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		head.addOrReplaceChild("nose",
				CubeListBuilder.create()
						.texOffs(30, 2).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -10.0F));


		PartDefinition tail = body.addOrReplaceChild("tail",
				CubeListBuilder.create()
						.texOffs(33, 12).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.5F, 14.0F, -0.0873F, 0.0F, 0.0F));

		tail.addOrReplaceChild("tail_fin",
				CubeListBuilder.create()
						.texOffs(29, 31).addBox(-5.0F, -0.5F, -1.0F, 10.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, -0.1396F, 0.0F, 0.0F));


		body.addOrReplaceChild("back_fin",
				CubeListBuilder.create()
						.texOffs(18, 16).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 4.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 5.0F, 1.0472F, 0.0F, 0.0F));


		body.addOrReplaceChild("left_fin",
				CubeListBuilder.create()
						.texOffs(1, 15).addBox(0.0F, -4.0F, -1.5F, 1.0F, 4.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.0F, -2.0F, 5.0F, 0.9599F, 0.0F, 1.8675F));

		body.addOrReplaceChild("right_fin",
				CubeListBuilder.create()
						.texOffs(1, 27).addBox(-1.0F, -4.0F, -1.5F, 1.0F, 4.0F, 7.0F, CubeDeformation.NONE).mirror(),
				PartPose.offsetAndRotation(-3.0F, -2.0F, 5.0F, 0.9599F, 0.0F, -1.8675F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		this.body.xRot = state.xRot * ((float) Math.PI / 180F);
		this.body.yRot = state.yRot * ((float) Math.PI / 180F);
		if (state.deltaMovement != null
				&& state.deltaMovement.horizontalDistanceSqr() > 1.0E-7D) {
			this.body.xRot += -0.05F - 0.05F * Mth.cos(state.ageInTicks * 0.3F);
			this.tail.xRot = -0.1F * Mth.cos(state.ageInTicks * 0.3F);
			this.tail_fin.xRot = -0.2F * Mth.cos(state.ageInTicks * 0.3F);
		}
	}

}