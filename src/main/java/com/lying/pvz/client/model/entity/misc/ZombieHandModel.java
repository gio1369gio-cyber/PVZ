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
 * 僵尸手（Zombie Hand）模型，texture 64×64。
 *
 * <pre>
 * total（setPos(0,24,0)，1 个方块：手掌/前臂 8×24×8，y 从 -24 到 0）
 *   ├─ bone （setPos(-3,-24, 2)，setRotationAngle(-0.7854,-0.2618,0)，1 个方块：指 2×7×2）
 *   ├─ bone2（setPos( 0,-24, 3)，setRotationAngle(-0.5236, 0,     0)，1 个方块：指 2×8×2）
 *   ├─ bone3（setPos( 3,-24, 2)，setRotationAngle(-0.7854, 0.2618,0)，1 个方块：指 2×7×2）
 *   ├─ bone4（setPos(-3,-24,-2)，setRotationAngle(-1.0472,-1.0472,0)，1 个方块：指 2×7×2）
 *   └─ bone5（setPos( 3,-24,-2)，setRotationAngle(-1.0472, 1.0472,0)，1 个方块：指 2×7×2）
 * </pre>
 *
 * total 为竖直的手掌/前臂主体；5 根指头均挂在 total 顶端（y=-24），
 * 三根在正面（z=2/3/2，bone/bone2/bone3），两根在背面（z=-2，bone4/bone5），
 * 各带不同的 xRot / yRot 初始角度，呈自然张开状。
 *
 * setupAnim：读取 {@code state.tick}，5 根指头的 xRot 在初始角度基础上按 tick 线性递增：
 * <pre>
 * bone.xRot  = -0.785 + tick * 0.02
 * bone2.xRot = -0.523 + tick * 0.02
 * bone3.xRot = -0.785 + tick * 0.02
 * bone4.xRot = -1.047 + tick * 0.035
 * bone5.xRot = -1.047 + tick * 0.035
 * </pre>
 * 正面三指同速、背面两指更快，形成"抓握/蠕动"效果。
 */
public class ZombieHandModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("zombie_hand"), "main");

	private final ModelPart total;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone5;

	public ZombieHandModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.bone = this.total.getChild("bone");
		this.bone2 = this.total.getChild("bone2");
		this.bone3 = this.total.getChild("bone3");
		this.bone4 = this.total.getChild("bone4");
		this.bone5 = this.total.getChild("bone5");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(1, 31).addBox(-4.0F, -24.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(35, 53).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, -24.0F, 2.0F, -0.7854F, -0.2618F, 0.0F));


		total.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(45, 53).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -24.0F, 3.0F, -0.5236F, 0.0F, 0.0F));


		total.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(55, 53).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.0F, -24.0F, 2.0F, -0.7854F, 0.2618F, 0.0F));


		total.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(34, 42).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, -24.0F, -2.0F, -1.0472F, -1.0472F, 0.0F));


		total.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(45, 41).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.0F, -24.0F, -2.0F, -1.0472F, 1.0472F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
		int tick = state.tick;
		this.bone.xRot = -0.785f + tick * 0.08f / 4;
		this.bone2.xRot = -0.523f + tick * 0.08f / 4;
		this.bone3.xRot = -0.785f + tick * 0.08f / 4;
		this.bone4.xRot = -1.047f + tick * 0.14f / 4;
		this.bone5.xRot = -1.047f + tick * 0.14f / 4;
	}


}