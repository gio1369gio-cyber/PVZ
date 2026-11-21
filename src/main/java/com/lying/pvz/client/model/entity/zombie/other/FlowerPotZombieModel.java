package com.lying.pvz.client.model.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;


import java.util.Optional;

// FlowerPotZombieModel —— 花盆僵尸
// 形状：标准僵尸骨架 + 头部附加帽子/眉毛（bone3）、红眼与普通眼两套可切换（red_eyes/normal_eyes）；
//       右手（right_hand）前伸抓一个大花盆（flower_pot，含花瓣 body2 三片）
public class FlowerPotZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("flower_pot_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart head;
	private final ModelPart bone3;
	private final ModelPart red_eyes;
	private final ModelPart normal_eyes;
	private final ModelPart right_hand;
	private final ModelPart flower_pot;
	private final ModelPart body2;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart left_hand;
	private final ModelPart body;

	public FlowerPotZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.head = this.up.getChild("head");
		this.bone3 = this.head.getChild("bone3");
		this.red_eyes = this.head.getChild("red_eyes");
		this.normal_eyes = this.head.getChild("normal_eyes");
		this.right_hand = this.up.getChild("right_hand");
		this.flower_pot = this.right_hand.getChild("flower_pot");
		this.body2 = this.flower_pot.getChild("body2");
		this.bone4 = this.body2.getChild("bone4");
		this.bone5 = this.body2.getChild("bone5");
		this.left_hand = this.up.getChild("left_hand");
		this.body = this.up.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(230, 225).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(196, 242).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(230, 192).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 22.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(191, 220).addBox(-3.0F, 22.0F, -5.0F, 6.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(66, 182).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(130, 207).addBox(-7.0F, -15.0F, 2.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(169, 208).addBox(-6.0F, -15.0F, -3.0F, 13.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		head.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(200, 188).addBox(-8.0F, -1.0F, -10.0F, 1.0F, 1.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(140, 180).addBox(7.0F, -1.0F, -10.0F, 1.0F, 1.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(183, 187).addBox(-7.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(206, 172).addBox(5.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(227, 175).addBox(-1.0F, -1.0F, -10.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(241, 169).addBox(-5.0F, -2.0F, -10.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(174, 175).addBox(1.0F, -2.0F, -10.0F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		head.addOrReplaceChild("red_eyes",
				CubeListBuilder.create()
						.texOffs(45, 187).addBox(-6.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 186).addBox(1.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		head.addOrReplaceChild("normal_eyes",
				CubeListBuilder.create()
						.texOffs(47, 178).addBox(-6.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(20, 175).addBox(1.0F, -13.0F, -7.05F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(148, 222).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-11.0F, -21.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

		PartDefinition flower_pot = right_hand.addOrReplaceChild("flower_pot",
				CubeListBuilder.create()
						.texOffs(0, 53).addBox(-5.0F, -8.0F, 10.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(1.0F))
						.texOffs(0, 50).addBox(-6.0F, -10.0F, 9.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(1.0F))
						.texOffs(0, 47).addBox(-7.0F, -13.0F, 8.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(1.0F))
						.texOffs(0, 0).addBox(-8.0F, -19.0F, 7.0F, 16.0F, 6.0F, 16.0F, new CubeDeformation(1.0F))
						.texOffs(0, 48).addBox(-8.0F, -20.0F, 7.0F, 1.0F, 1.0F, 15.0F, new CubeDeformation(1.0F))
						.texOffs(2, 31).addBox(7.0F, -20.0F, 7.0F, 1.0F, 1.0F, 15.0F, new CubeDeformation(1.0F))
						.texOffs(1, 32).addBox(-7.0F, -20.0F, 7.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(1.0F))
						.texOffs(2, 36).addBox(-8.0F, -20.0F, 22.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(11.0F, 45.0F, 0.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition body2 = flower_pot.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(55, 48).addBox(-1.0F, -12.0F, 15.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(1.0F, -11.5F, 5.5F, -0.1309F, 0.0F, -0.3054F));

		body2.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(55, 55).addBox(-1.0F, -9.0F, 15.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		body2.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(55, 44).addBox(-1.0F, -9.0F, 15.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(-0.25F, -3.75F, 0.0F, 0.0F, 0.0F, 0.6545F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(59, 221).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(11.0F, -21.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(3, 221).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean isPaperDestroyed = true;
		this.normal_eyes.visible = isPaperDestroyed;
		this.flower_pot.visible = isPaperDestroyed;
	}

	@Override
	public void refreshAnim() {
		this.getZombieLeftHand().xRot = -1.0472f;
		this.getZombieRightHand().xRot = -1.0472f;
	}

	@Override
	public Optional<ModelPart> getHandDefence() {
		return Optional.ofNullable(this.flower_pot);
	}

	@Override
	public ModelPart getZombieLeftHand() {
		return this.left_hand;
	}

	@Override
	public ModelPart getZombieRightHand() {
		return this.right_hand;
	}

	@Override
	public ModelPart getZombieLeftLeg() {
		return this.left_leg;
	}

	@Override
	public ModelPart getZombieRightLeg() {
		return this.right_leg;
	}

	@Override
	public ModelPart getZombieHead() {
		return this.head;
	}

	@Override
	public ModelPart getZombieUpBody() {
		return this.up;
	}

	@Override
	public ModelPart getZombieWholeBody() {
		return this.total;
	}
}