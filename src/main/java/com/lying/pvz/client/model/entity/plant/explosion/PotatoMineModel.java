package com.lying.pvz.client.model.entity.plant.explosion;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。关键点：
//   1. 土豆本体（body + red + white）用的是 -0.0873 的 X 轴前倾，位置有 -7.0038/-5.9128 这种
//      "非整" 坐标（Blockbench 自动导出），逐位保留。
//   2. 泥土（dirt → ne/sw/se/nw）的**全部方块 mirror=true**。
//      1.16.5 每个 addBox 尾参都是 true；迁移时用 CubeListBuilder.create().mirror() 一次性开启，
//      这样四个子部件内所有方块都镜像，与原版语义等价（不必每个 addBox 单独 .mirror）。
//   3. 闪烁：red/white 两个方块在两个相位里交替可见（红警灯闪烁感），
//      由 setupAnim 用 getExistTick() % (2*getSignChangeCD()) 驱动，原样保留。
//   4. ⚠️ total.setPos(0, 25, 0) —— 不是 24！土豆埋地里，整体下移 1 格，保留原值。

public class PotatoMineModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("potato_mine"), "main");

	private final ModelPart total;

	private final ModelPart red;
	private final ModelPart white;

	public PotatoMineModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.red = this.total.getChild("body").getChild("red");
		this.white = this.total.getChild("body").getChild("white");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 25.0F, 0.0F));


		PartDefinition dirt = total.addOrReplaceChild("dirt",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		dirt.addOrReplaceChild("ne",
				CubeListBuilder.create().mirror()
						.texOffs(5, 56).addBox(-6.5F, -4.0F, -6.5F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(4, 55).addBox(-4.5F, -2.0F, -6.5F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(5, 57).addBox(-2.5F, -2.0F, -6.5F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 58).addBox(-6.5F, -3.0F, -4.5F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		dirt.addOrReplaceChild("sw",
				CubeListBuilder.create().mirror()
						.texOffs(2, 51).addBox(0.5F, -2.0F, 4.5F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(2, 59).addBox(3.5F, -3.0F, 3.5F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(5, 55).addBox(4.5F, -2.0F, 0.5F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		dirt.addOrReplaceChild("se",
				CubeListBuilder.create().mirror()
						.texOffs(1, 55).addBox(-6.5F, -4.0F, 3.5F, 4.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 58).addBox(-6.5F, -2.0F, -0.5F, 3.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(4, 52).addBox(-2.5F, -2.0F, 4.5F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		dirt.addOrReplaceChild("nw",
				CubeListBuilder.create().mirror()
						.texOffs(1, 55).addBox(4.5F, -3.0F, -6.5F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 53).addBox(1.5F, -2.0F, -6.5F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(2, 58).addBox(5.5F, -2.0F, -3.5F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-6.0F, -7.0038F, -5.9128F, 12.0F, 8.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 25).addBox(0.0F, -10.0038F, -0.9128F, 0.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(40, 25).addBox(-3.0F, -7.5038F, -2.9128F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(14, 27).addBox(-1.0F, -10.0038F, 0.0872F, 2.0F, 3.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		body.addOrReplaceChild("red",
				CubeListBuilder.create()
						.texOffs(48, 8).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		body.addOrReplaceChild("white",
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-2.0F, -14.0038F, -1.9128F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int T = (state.signChangeCD << 1);
		final int current = state.existTick % T;
		final boolean flag = (current < (T >> 1));
		this.red.visible = flag;
		this.white.visible = !flag;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}