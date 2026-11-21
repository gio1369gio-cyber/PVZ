package com.lying.pvz.client.model.entity.bullet;


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
 * 玉米（Corn）模型。
 *
 * <pre>
 * total（setPos(0,24,0)，无方块，仅变换节点）
 *   └─ corn（setPos(0,-1,-2)，2 个方块：主体 9×29×9 + 顶部薄片 7×1×7，CubeDeformation -0.25）
 * </pre>
 *
 * setupAnim 为空，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class CornModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "corn"), "main");

	private final ModelPart total;

	public CornModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("corn",
				CubeListBuilder.create()
						.texOffs(0, 26).addBox(-4.5F, -28.0F, -2.5F, 9.0F, 29.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(36, 56).addBox(-3.5F, -28.75F, -1.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -1.0F, -2.0F));


		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}

}