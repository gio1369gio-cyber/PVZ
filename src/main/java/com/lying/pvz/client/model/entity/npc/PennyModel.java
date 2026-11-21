package com.lying.pvz.client.model.entity.npc;


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
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;


/**
 * 潘妮（Penny）模型，texture 128×128。
 *
 * <pre>
 * car（setPos(0,24,0)，1 个方块：车体 15×18×32）
 *   ├─ head（setPos(9,-7,-6)，1 个方块：15×2×13）
 *   │    ├─ bone10（setPos(0,13,5)，1 个方块：15×5×5）
 *   │    │    ├─ bone（setRotationAngle(0.9275,0,0)，1 个方块：15×3×4）
 *   │    │    └─ light（setPos(0,0,-12)，2 个方块：4×1×4 + 3×2×3，前照灯）
 *   │    ├─ bone9（setPos(0,1,0)，1 个方块：9×11×0）
 *   │    │    ├─ bone2（setRotationAngle(0,0.6458,0)，1 个方块：0×11×5）
 *   │    │    └─ bone3（setRotationAngle(0,-0.6458,0)，1 个方块：0×11×5）
 *   │    └─ bone4（setPos(0,0,0)，3 个方块：左右侧板 2×6×9 + 中部 7×3×9）
 *   │         ├─ bone6（空）
 *   │         └─ bone5（setRotationAngle(0.3944,0,0)，1 个方块：7×1×10）
 *   ├─ body（setPos(9,-7,-6)，无方块）
 *   │    └─ top（setPos(0,17,-29)，3 个方块：13×1×4 + 2 个 1×3×4 支柱）
 *   ├─ tail（setPos(10,-5,-38)，1 个方块：12×12×1，尾翼）
 *   └─ tyres（setPos(9,0,11)，无方块，4 个轮胎的容器）
 *        ├─ tyre_1（setPos(0,0,-29)）   → bone14（setPos(0,-10,-68)）→ bone12, bone13(-1.5708,0,0)
 *        │                              └ bone15（setPos(0,-12,-70)，rot -0.7854,0,0）→ bone16, bone17(-1.5708,0,0)
 *        ├─ tyre_2（setPos(-15,0,-29)） → bone8 → bone11, bone18(-1.5708,0,0)
 *        │                              └ bone19（rot -0.7854,0,0）→ bone20, bone21(-1.5708,0,0)
 *        ├─ tyre_3（setPos(-15,0,0)）   → bone22 → bone23, bone24(-1.5708,0,0)
 *        │                              └ bone25（rot -0.7854,0,0）→ bone26, bone27(-1.5708,0,0)
 *        └─ tyre_4（setPos(0,0,0)）     → bone28 → bone29, bone30(-1.5708,0,0)
 *                                       └ bone31（rot -0.7854,0,0）→ bone32, bone33(-1.5708,0,0)
 * </pre>
 *
 * 结构分四块：
 * <ul>
 *   <li>car：车体主体 + 头 + body + tail + tyres 五个直接子节点。</li>
 *   <li>head：车头前部，含 bone10（含前照灯 light、斜置骨 bone）、bone9（含左右挡风斜片
 *       bone2/bone3）、bone4（含左右侧板、中部、斜置骨 bone5 与空节点 bone6），
 *       构成"脸 + 灯 + 挡风 + 侧板"的复合结构。</li>
 *   <li>body → top：车顶结构，由 1 块 13×1×4 顶板 + 2 根 1×3×4 支柱组成。</li>
 *   <li>tail：车尾尾翼，12×12×1 竖板。</li>
 *   <li>tyres：4 个轮胎（tyre_1～tyre_4），每个轮胎结构完全相同——
 *       两根 2×4×3 辐条直连 + 两根 2×4×3 辐条绕 X 轴 -1.5708 旋转，共 4 根辐条；
 *       每个轮胎内部再用 -0.7854 旋转的中间节点把 4 根辐条分两组错开。
 *       轮胎本体无独立几何体，只有辐条，呈"十字轮"观感。</li>
 * </ul>
 *
 * setupAnim 为空实现，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 * 轮胎不参与动画，即便车体移动也是静态轮。
 */
public class PennyModel extends EntityModel<EntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("penny"), "main");

	private final ModelPart car;

	public PennyModel(ModelPart root) {
		super(root);
		this.car = root.getChild("car");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition car = root.addOrReplaceChild("car",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.5F, -21.0F, -10.0F, 15.0F, 18.0F, 32.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition head = car.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 50).addBox(-16.5F, 2.0F, -17.0F, 15.0F, 2.0F, 13.0F, CubeDeformation.NONE),
				PartPose.offset(9.0F, -7.0F, -6.0F));

		PartDefinition bone10 = head.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(45, 50).addBox(-16.5F, -27.0F, -14.0F, 15.0F, 5.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 13.0F, 5.0F));


		bone10.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(60, 60).addBox(-15.5F, 4.6002F, -6.8003F, 15.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, -32.0F, -16.0F, 0.9275F, 0.0F, 0.0F));


		bone10.addOrReplaceChild("light",
				CubeListBuilder.create()
						.texOffs(34, 92).addBox(-11.0F, -27.5F, -1.5F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(45, 60).addBox(-10.5F, -29.0F, -1.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -12.0F));

		PartDefinition bone9 = head.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(36, 67).addBox(-13.5F, -10.0F, -8.0F, 9.0F, 11.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		bone9.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(54, 62).addBox(-0.422F, 5.0F, 4.6991F, 0.0F, 11.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, 0.6458F, 0.0F));


		bone9.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 45).addBox(-2.7725F, 5.0F, 7.1063F, 0.0F, 11.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, -0.6458F, 0.0F));


		PartDefinition bone4 = head.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(0, 67).addBox(-15.0F, -4.5F, -15.0F, 2.0F, 6.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(62, 16).addBox(-5.5F, -4.5F, -15.0F, 2.0F, 6.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-12.5F, -1.75F, -17.0F, 7.0F, 3.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone4.addOrReplaceChild("bone6",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone4.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(62, 0).addBox(-2.5F, 10.3111F, -9.708F, 7.0F, 1.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, -15.0F, -12.0F, 0.3944F, 0.0F, 0.0F));


		PartDefinition body = car.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offset(9.0F, -7.0F, -6.0F));


		body.addOrReplaceChild("top",
				CubeListBuilder.create()
						.texOffs(62, 11).addBox(-15.5F, -35.0F, 50.0F, 13.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(88, 77).addBox(-3.5F, -34.0F, 50.0F, 1.0F, 3.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(90, 23).addBox(-15.5F, -34.0F, 50.0F, 1.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 17.0F, -29.0F));


		car.addOrReplaceChild("tail",
				CubeListBuilder.create()
						.texOffs(0, 12).addBox(-16.5F, -12.0F, 60.0F, 12.0F, 12.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -5.0F, -38.0F));


		PartDefinition tyres = car.addOrReplaceChild("tyres",
				CubeListBuilder.create(),
				PartPose.offset(9.0F, 0.0F, 11.0F));


		PartDefinition tyre1 = tyres.addOrReplaceChild("tyre_1",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, -29.0F));

		PartDefinition bone14 = tyre1.addOrReplaceChild("bone14",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -10.0F, -68.0F));

		bone14.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(24, 90).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(10, 90).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone14.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(50, 89).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(60, 89).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone15 = tyre1.addOrReplaceChild("bone15",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		bone15.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(0, 89).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(88, 67).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		bone15.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(83, 88).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(86, 0).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition tyre2 = tyres.addOrReplaceChild("tyre_2",
				CubeListBuilder.create(),
				PartPose.offset(-15.0F, 0.0F, -29.0F));

		PartDefinition bone8 = tyre2.addOrReplaceChild("bone8",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -10.0F, -68.0F));

		bone8.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(17, 86).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(85, 50).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone8.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(73, 85).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(43, 85).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone19 = tyre2.addOrReplaceChild("bone19",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		bone19.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(33, 85).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(84, 20).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone19.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(63, 82).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(53, 82).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition tyre3 = tyres.addOrReplaceChild("tyre_3",
				CubeListBuilder.create(),
				PartPose.offset(-15.0F, 0.0F, 0.0F));

		PartDefinition bone22 = tyre3.addOrReplaceChild("bone22",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -10.0F, -68.0F));

		bone22.addOrReplaceChild("bone23",
				CubeListBuilder.create()
						.texOffs(10, 82).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 82).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone22.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(81, 74).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(81, 81).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone25 = tyre3.addOrReplaceChild("bone25",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		bone25.addOrReplaceChild("bone26",
				CubeListBuilder.create()
						.texOffs(78, 67).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(71, 78).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone25.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(46, 78).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(36, 78).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre4 = tyres.addOrReplaceChild("tyre_4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone28 = tyre4.addOrReplaceChild("bone28",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -10.0F, -68.0F));

		bone28.addOrReplaceChild("bone29",
				CubeListBuilder.create()
						.texOffs(75, 16).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(61, 75).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone28.addOrReplaceChild("bone30",
				CubeListBuilder.create()
						.texOffs(71, 71).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(64, 67).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone31 = tyre4.addOrReplaceChild("bone31",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		bone31.addOrReplaceChild("bone32",
				CubeListBuilder.create()
						.texOffs(62, 0).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(20, 25).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		bone31.addOrReplaceChild("bone33",
				CubeListBuilder.create()
						.texOffs(10, 25).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 25).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(EntityRenderState state) {
		super.setupAnim(state);
	}

}