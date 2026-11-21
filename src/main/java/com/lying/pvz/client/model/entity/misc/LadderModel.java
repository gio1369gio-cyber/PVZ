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
 * 梯子（Ladder）模型，texture 64×64。
 *
 * <pre>
 * total（setPos(0,-5,0)，4 个方块）
 *   ├─ 左立柱 3×50×4（texOffs 14,10），x=-12、y=-22、z=-2，CubeDeformation 0.1
 *   ├─ 右立柱 3×50×4（texOffs 0,10）， x=9、 y=-22、z=-2，CubeDeformation 0.1
 *   ├─ 右底脚 5×1×6（texOffs 28,50），x=8、 y=28、 z=-3
 *   ├─ 左底脚 5×1×6（texOffs 28,57），x=-13、y=28、z=-3
 *   └─ floor（setPos(0,0,0)，5 个方块：横档 18×2×4，texOffs 0,4）
 *        Y 位置：20、12、4、-4、-12
 * </pre>
 *
 * total 承担整体位移；两根立柱沿 Y 轴 -22～+28 贯穿上下，两端各带一个 5×1×6 底脚。
 * floor 为 5 根等距横档（间距 8），构成梯子的踏面。
 *
 * setupAnim 为空实现，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class LadderModel<T extends PvZEntityRenderState> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "ladder"), "main");

	private final ModelPart total;

	public LadderModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(14, 10).addBox(-12.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
						.texOffs(0, 10).addBox(9.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
						.texOffs(28, 50).addBox(8.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(28, 57).addBox(-13.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -5.0F, 0.0F));


		total.addOrReplaceChild("floor",
				CubeListBuilder.create()
						.texOffs(0, 4).addBox(-9.0F, 20.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 4).addBox(-9.0F, 12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 4).addBox(-9.0F, 4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 4).addBox(-9.0F, -4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 4).addBox(-9.0F, -12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
	}

}