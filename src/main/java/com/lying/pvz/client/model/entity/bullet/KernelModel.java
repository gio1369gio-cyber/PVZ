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
 * 玉米粒（Kernel）模型。
 *
 * <pre>
 * total（setPos(0,24,0)，1 个方块：玉米粒 2×4×2）
 * </pre>
 *
 * setupAnim 为空，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class KernelModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("kernel"), "main");

	private final ModelPart total;

	public KernelModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// 1.16.5: total.setPos(0, 24, 0); total.texOffs(0,0).addBox(-1,-4,-1, 2,4,2, 0.0F, false)
		root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}

}