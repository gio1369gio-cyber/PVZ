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
 * 割草机（Lawn Mower）模型，texture 128×128。
 *
 * <pre>
 * total（setPos(0,24,0)，8 个方块）
 *   ├─ 车体 10×5×16（texOffs 0,23），y=-6、z=-8
 *   ├─ 外壳 10×5×18（texOffs 0,0），CubeDeformation -0.2，包住车体
 *   ├─ 前挡 10×5×2（texOffs 45,45），z=-9.5，CubeDeformation -0.4
 *   ├─ 引擎底 7×2×8（texOffs 38,0），y=-7.8
 *   ├─ 引擎中 7×3×8（texOffs 36,23），y=-8.7，CubeDeformation -0.2
 *   ├─ 引擎顶 6×2×6（texOffs 38,10），y=-9.8，CubeDeformation -0.2
 *   ├─ 顶盖 7×2×7（texOffs 0,44），y=-10.8，CubeDeformation -0.3
 *   ├─ 前踏板 12×1×2（texOffs 26,52），y=-2.8，CubeDeformation -0.2
 *   ├─ cube_r1（setRotationAngle(-0.7418,0,0)，2 个方块：排气口小件）
 *   ├─ cube_r2（setRotationAngle(-0.3491,0,0)，1 个方块：12×1×2，CubeDeformation -0.2）
 *   ├─ cube_r3（setRotationAngle(0.3491,0,0)，1 个方块：12×1×2，CubeDeformation -0.2）
 *   ├─ bone3（setPos(-5,-6,1)，4 个方块：小装饰件）
 *   │    └─ cube_r4（setRotationAngle(0,1.5708,0)，1 个方块：4×1×1）
 *   ├─ Thing（setPos(0,0,0)，2 个方块：前后横梁 13×1×1）
 *   │    └─ Wheel（setPos(0,0,0)，无方块）
 *   │         ├─ wheel1（setPos(-6,-2,-6.5)，2 个方块：1×5×3 + 1×4×5）
 *   │         ├─ wheel2（setPos(6,-2,-6.5)，2 个方块：1×4×5 + 1×5×3）
 *   │         ├─ wheel3（setPos(-6,-2,6.5)，2 个方块：1×4×5 + 1×5×3）
 *   │         └─ wheel4（setPos(6,-2,6.5)，2 个方块：1×4×5 + 1×5×3）
 *   ├─ Handle（setPos(0,0,0)，无方块）
 *   │    └─ cube_r5（setRotationAngle(-0.1745,0,0)，4 个方块：推杆框架 9×12×1）
 *   ├─ Wire（setPos(0,0,0)，无方块）
 *   │    ├─ cube_r6（setRotationAngle(2.0944,0,0)，1 个方块：1×3×1，CubeDeformation -0.4）
 *   │    ├─ cube_r7（setRotationAngle(2.4435,0,0)，1 个方块：1×2×1，CubeDeformation -0.4）
 *   │    └─ cube_r8（setRotationAngle(2.6616,0,0)，1 个方块：1×3×1，CubeDeformation -0.4）
 *   └─ bone2（setPos(0,0,0)，5 个方块：切割刀盘 7×2×1 ×2 + 6×1×6 + 1×2×6 ×2）
 *        └─ Cuter（setPos(0,0,0)，6 个方块：中央刀片组，含 4 个 2×2×2 外刃，CubeDeformation -0.5）
 *             └─ cube_r9（setRotationAngle(0,-1.5708,0)，1 个方块：1×1×2，CubeDeformation -0.3）
 * </pre>
 *
 * 结构分三块：
 * <ul>
 *   <li>车体与引擎：total 直接几何体，8 个方块堆叠成车身 + 引擎 + 顶盖。</li>
 *   <li>行走机构：Thing → Wheel 下 4 个轮子（wheel1～wheel4）位于 (±6,-2,±6.5)，
 *       每个轮子由 1×5×3 与 1×4×5 两块拼成，左右镜像。</li>
 *   <li>操作与刀盘：Handle（推杆）、Wire（3 段电线）、bone2 → Cuter（刀盘）。</li>
 * </ul>
 *
 * setupAnim：当 {@code state.isRun} 为 true 时，wheel1～wheel4 的 xRot 均设为
 * {@code state.ageInTicks * 1F}，四轮同步旋转；否则全部归零。
 * 车体、推杆、刀盘不参与动画。
 */
public class LawnMowerModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("lawn_mower"), "main");

	private final ModelPart total;

	private final ModelPart wheel1;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;

	public LawnMowerModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		ModelPart wheel = this.total.getChild("Thing").getChild("Wheel");
		this.wheel1 = wheel.getChild("wheel1");
		this.wheel2 = wheel.getChild("wheel2");
		this.wheel3 = wheel.getChild("wheel3");
		this.wheel4 = wheel.getChild("wheel4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-5.0F, -6.0F, -8.0F, 10.0F, 5.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -6.0F, -9.0F, 10.0F, 5.0F, 18.0F, new CubeDeformation(-0.2F))
						.texOffs(45, 45).addBox(-5.0F, -6.0F, -9.5F, 10.0F, 5.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(38, 0).addBox(-3.5F, -7.8F, -3.0F, 7.0F, 2.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(36, 23).addBox(-3.5F, -8.7F, -3.0F, 7.0F, 3.0F, 8.0F, new CubeDeformation(-0.2F))
						.texOffs(38, 10).addBox(-3.0F, -9.8F, -2.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(-0.2F))
						.texOffs(0, 44).addBox(-3.5F, -10.8F, -2.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.3F))
						.texOffs(26, 52).addBox(-6.0F, -2.8F, -0.5F, 12.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(10, 16).addBox(-1.5F, -1.5F, 1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 8).addBox(-1.5F, -1.5F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.15F)),
				PartPose.offsetAndRotation(-2.0F, -11.0F, 8.0F, -0.7418F, 0.0F, 0.0F));


		total.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(36, 36).addBox(-1.0F, -0.5F, -1.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(-5.0F, -2.0F, 2.0F, -0.3491F, 0.0F, 0.0F));


		total.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(50, 42).addBox(-1.0F, -0.5F, -1.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(-5.0F, -2.0F, -1.0F, 0.3491F, 0.0F, 0.0F));


		PartDefinition bone3 = total.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(0, 26).addBox(0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
						.texOffs(0, 11).addBox(0.5F, -0.8F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(8, 4).addBox(-0.5F, -0.8F, 1.3F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.28F))
						.texOffs(8, 8).addBox(-1.0F, -0.8F, 1.3F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.18F)),
				PartPose.offset(-5.0F, -6.0F, 1.0F));


		bone3.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(0, 16).addBox(-3.0F, -0.8F, 1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.28F)),
				PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.0F, 1.5708F, 0.0F));


		PartDefinition thing = total.addOrReplaceChild("Thing",
				CubeListBuilder.create()
						.texOffs(52, 52).addBox(-6.5F, -2.5F, 6.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(36, 34).addBox(-6.5F, -2.5F, -7.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition wheel = thing.addOrReplaceChild("Wheel",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		wheel.addOrReplaceChild("wheel1",
				CubeListBuilder.create()
						.texOffs(31, 55).addBox(-0.3F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(49, 54).addBox(-0.3F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, -2.0F, -6.5F));


		wheel.addOrReplaceChild("wheel2",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-0.8F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(38, 0).addBox(-0.8F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, -2.0F, -6.5F));


		wheel.addOrReplaceChild("wheel3",
				CubeListBuilder.create()
						.texOffs(0, 55).addBox(-0.3F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(39, 55).addBox(-0.3F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, -2.0F, 6.5F));


		wheel.addOrReplaceChild("wheel4",
				CubeListBuilder.create()
						.texOffs(15, 53).addBox(-0.8F, -2.0F, -2.5F, 1.0F, 4.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(36, 23).addBox(-0.8F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, -2.0F, 6.5F));


		PartDefinition handle = total.addOrReplaceChild("Handle",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		handle.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(52, 39).addBox(-9.5F, -11.0F, 0.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(14, 0).addBox(-1.5F, -11.0F, 0.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(14, 0).addBox(-9.5F, -11.0F, 0.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(0, 53).addBox(-9.5F, -6.0F, 0.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
				PartPose.offsetAndRotation(5.0F, -6.0F, 7.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition wire = total.addOrReplaceChild("Wire",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		wire.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(8, 0).addBox(-1.0F, -3.0F, -0.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.0F, -9.0F, 7.0F, 2.0944F, 0.0F, 0.0F));


		wire.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(12, 27).addBox(-1.0F, -3.7F, -0.01F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.0F, -11.0F, 9.0F, 2.4435F, 0.0F, 0.0F));


		wire.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(14, 14).addBox(-1.0F, -3.3F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.0F, -12.0F, 9.0F, 2.6616F, 0.0F, 0.0F));


		PartDefinition bone2 = total.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-3.5F, -2.3F, -4.0F, 7.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 32).addBox(-3.5F, -2.3F, 3.0F, 7.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(21, 44).addBox(-3.0F, -1.8F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 8).addBox(-4.0F, -2.3F, -3.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(3.0F, -2.3F, -3.0F, 1.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition cuter = bone2.addOrReplaceChild("Cuter",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-0.5F, -2.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F))
						.texOffs(0, 3).addBox(-0.5F, -1.3F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.3F))
						.texOffs(56, 10).addBox(-1.0F, -2.0F, -2.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(7, 55).addBox(-1.0F, -2.0F, 0.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(7, 23).addBox(-2.2F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(8, 10).addBox(0.2F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		cuter.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-0.5F, -1.3F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));


		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
		if (state.isRun) {
			float speed = 1F;
			this.wheel1.xRot = state.ageInTicks * speed;
			this.wheel2.xRot = state.ageInTicks * speed;
			this.wheel3.xRot = state.ageInTicks * speed;
			this.wheel4.xRot = state.ageInTicks * speed;
		} else {
			this.wheel1.xRot = 0;
			this.wheel2.xRot = 0;
			this.wheel3.xRot = 0;
			this.wheel4.xRot = 0;
		}
	}

}