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
 * 小吞噬者（Small Chomper）模型，texture 128×128。
 *
 * <pre>
 * head（setPos(0,21,-2)，setRotationAngle(-1.5708,0,0)，1 个方块：头主体 12×10×4）
 *   ├─ bone14（setPos(0,-6,0)，setRotationAngle(-0.9599,0,0)，1 个方块：2×2×2）
 *   │    └─ bone15（setPos(0,-1,0)，setRotationAngle(0,0,-0.7854)，1 个方块：2×2×2）
 *   ├─ up_mouse（setPos(0,-4,-2)，setRotationAngle(-1.0472,0,0)，18 个方块：上颚）
 *   │    ├─ 上颚主体：12×5×12（texOffs 22,96）+ 左右侧板 1×4×11（x=±6～-7）
 *   │    ├─ 前缘 10×4×1（texOffs 103,89）
 *   │    ├─ 14 个 1×1×1 牙齿（texOffs 13,110），沿前缘与两侧排布，y=+2
 *   │    ├─ bone9（setPos(0,-3,-10)，setRotationAngle(-0.0873,0,0)，1 个方块：2×5×2）
 *   │    │    └─ bone8（setPos(0,-4,0)，setRotationAngle(0,0,-0.7854)，1 个方块：2×2×2）
 *   │    ├─ bone10（setPos(0,-3,-6)，setRotationAngle(-0.1745,0,0)，1 个方块：2×4×2）
 *   │    │    └─ bone11（setPos(0,-3,0)，setRotationAngle(0,0,-0.7854)，1 个方块：2×2×2）
 *   │    └─ bone12（setPos(0,-3,-2)，setRotationAngle(-0.2618,0,0)，1 个方块：2×3×2）
 *   │         └─ bone13（setPos(0,-2,0)，setRotationAngle(0,0,-0.7854)，1 个方块：2×2×2）
 *   ├─ down_mouse（setPos(0,1,-2)，setRotationAngle(1.0472,0,0)，19 个方块：下颚）
 *   │    ├─ 下颚主体：12×4×12（texOffs 5,63）+ 左右侧板 1×3×11（x=±6～-7）
 *   │    ├─ 前缘 10×3×1（texOffs 74,88）
 *   │    └─ 14 个 1×1×1 牙齿（texOffs 5,110），y=-3
 *   ├─ leaf（setPos(0,-1,0)，1 个方块：叶片根 4×4×1）
 *   │    ├─ bone16（setRotationAngle(-0.5236,0,0)，      1 个方块：2×4×1）
 *   │    ├─ bone17（setRotationAngle(-0.5236,0,1.0472)， 1 个方块：2×4×1）
 *   │    ├─ bone18（setRotationAngle(-0.5236,0,2.0944)， 1 个方块：2×4×1）
 *   │    ├─ bone19（setRotationAngle(-0.2618,0,-3.1416)，1 个方块：2×4×1）
 *   │    ├─ bone20（setRotationAngle(-0.5236,0,-1.0472)，1 个方块：2×4×1）
 *   │    └─ bone21（setRotationAngle(-0.5236,0,-2.0944)，1 个方块：2×4×1）
 *   └─ tongue（setPos(0,-1,-5)，1 个方块：舌头 2×1×4）
 *        └─ tongue2（setPos(0,0,-4)，1 个方块：2×1×4）
 * </pre>
 *
 * 结构分五块：
 * <ul>
 *   <li>head：整体前倾 90°（-1.5708）使模型贴地，为 head 子节点提供统一坐标系。</li>
 *   <li>bone14/bone15：头顶小触角，双段带旋转。</li>
 *   <li>up_mouse / down_mouse：上下颚。上下颚各自由主体、左右侧板、前缘、14 颗 1×1×1
 *       牙齿组成，牙齿沿"门"形边缘排列；上颚再挂 bone9～bone13 三组"上颚内侧小舌"，
 *       每组双层（主干 + 尖端），绕 Z 轴 -0.7854 旋转。</li>
 *   <li>leaf：叶片根部 + 6 根叶片，bone16～bone21 绕 Y 轴以 1.0472 rad 等角展开
 *       （0、±1.0472、±2.0944、π），构成 6 向放射状叶片。</li>
 *   <li>tongue：两节 2×1×4 舌头，沿 -Z 伸出。</li>
 * </ul>
 *
 * setupAnim：读取 {@code state.tick}，上颚 {@code up_mouse.xRot = -1 + tick * 0.05}，
 * 下颚 {@code down_mouse.xRot = 1 - tick * 0.05}。tick 递增时上下颚相向张开/闭合，
 * 形成"咀嚼"动作。其余部件（叶片、舌头、触角）不参与动画。
 */
public class SmallChomperModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("small_chomper"), "main");

	private final ModelPart head;
	private final ModelPart up_mouse;
	private final ModelPart down_mouse;

	public SmallChomperModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.up_mouse = this.head.getChild("up_mouse");
		this.down_mouse = this.head.getChild("down_mouse");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition head = root.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(72, 113).addBox(-6.0F, -7.0F, -4.0F, 12.0F, 10.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, -1.5708F, 0.0F, 0.0F));


		PartDefinition bone14 = head.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(105, 107).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.9599F, 0.0F, 0.0F));


		bone14.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(35, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		PartDefinition up_mouse = head.addOrReplaceChild("up_mouse",
				CubeListBuilder.create()
						.texOffs(75, 94).addBox(6.0F, -2.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(22, 96).addBox(-6.0F, -3.0F, -12.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(103, 89).addBox(-5.0F, -2.0F, -13.0F, 10.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(100, 70).addBox(-7.0F, -2.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-5.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-3.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-1.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(1.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(3.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, -2.0F, -1.0472F, 0.0F, 0.0F));


		PartDefinition bone9 = up_mouse.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(9, 99).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -10.0F, -0.0873F, 0.0F, 0.0F));


		bone9.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(51, 43).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		PartDefinition bone10 = up_mouse.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(4, 90).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -6.0F, -0.1745F, 0.0F, 0.0F));


		bone10.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(62, 44).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		PartDefinition bone12 = up_mouse.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(32, 89).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, -0.2618F, 0.0F, 0.0F));


		bone12.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(19, 91).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		head.addOrReplaceChild("down_mouse",
				CubeListBuilder.create()
						.texOffs(46, 79).addBox(6.0F, -2.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(5, 63).addBox(-6.0F, -2.0F, -12.0F, 12.0F, 4.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(74, 88).addBox(-5.0F, -2.0F, -13.0F, 10.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(73, 71).addBox(-7.0F, -2.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-4.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-2.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(0.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(2.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(4.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition leaf = head.addOrReplaceChild("leaf",
				CubeListBuilder.create()
						.texOffs(64, 73).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));


		leaf.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(58, 65).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 0.0F));

		leaf.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(70, 64).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 1.0472F));


		leaf.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(92, 64).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 2.0944F));


		leaf.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(108, 61).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.2618F, 0.0F, -3.1416F));


		leaf.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(119, 61).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -1.0472F));


		leaf.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(81, 57).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -2.0944F));


		PartDefinition tongue = head.addOrReplaceChild("tongue",
				CubeListBuilder.create()
						.texOffs(7, 53).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, -5.0F));

		tongue.addOrReplaceChild("tongue2",
				CubeListBuilder.create()
						.texOffs(23, 52).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -4.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
		int tick = state.tick;
		this.up_mouse.xRot = -1f + tick * 0.05f;
		this.down_mouse.xRot = 1f - tick * 0.05f;
	}

}