package com.lying.pvz.client.model.entity.misc;


import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
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

/**
 * 雪车（Bobsle Car）模型，texture 512×512。
 *
 * <pre>
 * total（setPos(0,24,0)，无方块，仅变换节点）
 *   ├─ mid（setPos(0,-14,3)，6 个方块：底板 28×1×84 + 4 个座椅 16×8×6 + 前挡板 28×7×1）
 *   │    ├─ left（setPos(13,4,24)，1 个方块：1×25×84）
 *   │    └─ right（setPos(-13,4,25)，1 个方块：1×25×84）
 *   ├─ Head（setPos(0,-9,-22)，4 个方块：头主体 30×25×20 + 3 个前脸/顶棚片）
 *   ├─ wheal（setPos(13,-6,-20)，7 个方块：轮组 1，含主轮 2×2×16、4 个 4×2×4 轮块、轮轴 2×3×26）
 *   │    ├─ bone（setPos(0,3,-11)，setRotationAngle(-0.6981,0,0)，1 个方块）
 *   │    └─ bone2（setPos(0,3,14)，setRotationAngle(0.6981,0,0)，1 个方块）
 *   ├─ wheal2（setPos(13,-6,-20)，7 个方块：轮组 2，几何同 wheal 但 X 偏移 -27）
 *   │    ├─ bone3（setPos(0,3,-11)，setRotationAngle(-0.6981,0,0)，1 个方块）
 *   │    └─ bone4（setPos(0,3,14)，setRotationAngle(0.6981,0,0)，1 个方块）
 *   ├─ wheal3（setPos(13,-6,-20)，7 个方块：轮组 3，主轮 Z 偏移 +70 到 +86）
 *   │    ├─ bone5（setPos(0,3,-11)，setRotationAngle(-0.6981,0,0)，1 个方块）
 *   │    └─ bone6（setPos(0,3,14)，setRotationAngle(0.6981,0,0)，1 个方块）
 *   ├─ wheal4（setPos(13,-6,55)，7 个方块：轮组 4，几何同 wheal2 但 Z 偏移 +55）
 *   │    ├─ bone7（setPos(0,3,-11)，setRotationAngle(-0.6981,0,0)，1 个方块）
 *   │    └─ bone8（setPos(0,3,14)，setRotationAngle(0.6981,0,0)，1 个方块）
 *   └─ tail（setPos(0,-22,70)，setRotationAngle(0.4363,0,0)，2 个方块：左右尾翼 1×11×18）
 * </pre>
 *
 * 4 组轮（wheal～wheal4）各带 2 个绕 X 轴 ±0.6981 rad 倾斜的子方块，模拟履带/轮齿。
 * setupAnim 为空实现，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class BobsleCarModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bobsle_car"), "main");

	private final ModelPart total;

	public BobsleCarModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition mid = total.addOrReplaceChild("mid",
				CubeListBuilder.create()
						.texOffs(284, 421).addBox(-14.0F, 4.0F, -17.0F, 28.0F, 1.0F, 84.0F, CubeDeformation.NONE)
						.texOffs(459, 402).addBox(-8.0F, -4.0F, -3.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(402, 399).addBox(-8.0F, -4.0F, 17.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(463, 372).addBox(-8.0F, -4.0F, 37.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(402, 368).addBox(-8.0F, -4.0F, 57.0F, 16.0F, 8.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(440, 350).addBox(-14.0F, -2.0F, 67.0F, 28.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -14.0F, 3.0F));


		mid.addOrReplaceChild("left",
				CubeListBuilder.create()
						.texOffs(9, 399).addBox(1.0F, -24.0F, -41.0F, 1.0F, 25.0F, 84.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, 4.0F, 24.0F));


		mid.addOrReplaceChild("right",
				CubeListBuilder.create()
						.texOffs(336, 7).addBox(-2.0F, -24.0F, -42.0F, 1.0F, 25.0F, 84.0F, CubeDeformation.NONE),
				PartPose.offset(-13.0F, 4.0F, 25.0F));


		total.addOrReplaceChild("Head",
				CubeListBuilder.create()
						.texOffs(188, 449).addBox(-15.0F, -25.0F, -12.0F, 30.0F, 25.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(129, 436).addBox(-13.0F, -15.0F, -17.0F, 26.0F, 15.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(13, 378).addBox(-10.0F, -7.0F, -22.0F, 20.0F, 7.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(11, 340).addBox(-15.0F, -27.0F, -2.0F, 30.0F, 2.0F, 21.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -9.0F, -22.0F));


		PartDefinition wheal = total.addOrReplaceChild("wheal",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, -6.0F, -20.0F));


		wheal.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));


		wheal.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		PartDefinition wheal2 = total.addOrReplaceChild("wheal2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-27.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, -6.0F, -20.0F));


		wheal2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));


		wheal2.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		PartDefinition wheal3 = total.addOrReplaceChild("wheal3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -1.0F, 70.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 68.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -3.0F, 84.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 85.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 76.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 1.0F, 67.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, 3.0F, 65.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, -6.0F, -20.0F));


		wheal3.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -48.8519F, 55.2194F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));


		wheal3.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, 49.4946F, 56.9854F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		PartDefinition wheal4 = total.addOrReplaceChild("wheal4",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, -1.0F, -6.0F, 2.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, -8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, -3.0F, 8.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, 0.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-28.0F, 1.0F, -9.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-27.0F, 3.0F, -11.0F, 2.0F, 3.0F, 26.0F, CubeDeformation.NONE),
				PartPose.offset(13.0F, -6.0F, 55.0F));


		wheal4.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.0F, -3.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, -11.0F, -0.6981F, 0.0F, 0.0F));

		wheal4.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-27.0F, 0.6428F, -1.234F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, 0.6981F, 0.0F, 0.0F));


		total.addOrReplaceChild("tail",
				CubeListBuilder.create()
						.texOffs(261, 382).addBox(14.0F, -11.0F, 0.0F, 1.0F, 11.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(184, 378).addBox(-15.0F, -11.0F, 0.0F, 1.0F, 11.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -22.0F, 70.0F, 0.4363F, 0.0F, 0.0F));


		return LayerDefinition.create(mesh, 512, 512);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}


}