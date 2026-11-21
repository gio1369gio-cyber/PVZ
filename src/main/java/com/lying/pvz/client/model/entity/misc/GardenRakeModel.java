package com.lying.pvz.client.model.entity.misc;


import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.utils.AnimationUtil;
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
 * 花园耙（Garden Rake）模型，texture 128×128。
 *
 * <pre>
 * total（setPos(0,24,-3)，14 个方块）
 *   ├─ 横杆 24×1×2（texOffs 75,124），y=-1、z=+1
 *   ├─ 12 根齿：1×3×1（texOffs 0,0），y=-4、z=0
 *   │    X 位置：-12、-10、-8、-6、-4、-2、0(2 宽)、3、5、7、9、11
 *   ├─ 手柄 2×1×48（texOffs 23,5），y=-1、z=+3，沿 +Z 延伸
 *   ├─ bone（setPos(1,0,12)，setRotationAngle(0,0.7854,0)，1 个方块：15×1×1）
 *   └─ bone2（setPos(-1,0,12)，setRotationAngle(0,-0.7854,0)，1 个方块：15×1×1）
 * </pre>
 *
 * total 由横杆、12 根齿（中间一根 2 宽）、一根长手柄组成；
 * bone / bone2 为手柄两侧 ±0.7854 rad 展开的斜杆，构成耙头两侧支撑。
 *
 * setupAnim：读取 {@code state.attackTime}、{@code state.animTime}（均为 int，由
 * GardenRakeRender#extractRenderState 从 {@code entity.getAttackTime()} /
 * {@code entity.getAnimTime()} 填入）。当 {@code attackTime > 0} 时
 * total 绕 X 轴设为 {@code AnimationUtil.getUp(attackTime, animTime, 120)}，
 * 否则归零——即攻击期间耙体上抬，其余时间水平放置。
 */
public class GardenRakeModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("garden_rake"), "main");

	private final ModelPart total;

	public GardenRakeModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(75, 124).addBox(-12.0F, -1.0F, 1.0F, 24.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-12.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-10.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-6.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(11.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(9.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(5.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(3.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(0.0F, -4.0F, 0.0F, 2.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(23, 5).addBox(-1.0F, -1.0F, 3.0F, 2.0F, 1.0F, 48.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, -3.0F));


		total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(40, 117).addBox(-1.0F, -1.0F, 0.0F, 15.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, 0.0F, 12.0F, 0.0F, 0.7854F, 0.0F));


		total.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(28, 123).addBox(-14.0F, -1.0F, 0.0F, 15.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 12.0F, 0.0F, -0.7854F, 0.0F));


		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
		int time = state.attackTime;
		if (time > 0) {
			int max = state.animTime;
			this.total.xRot = AnimationUtil.getUp(time, max, 120);
		} else {
			this.total.xRot = 0;
		}
	}

}