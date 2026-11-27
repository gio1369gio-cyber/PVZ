package com.lying.pvz.client.model.entity.zombie.boss;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.animal.QuadrupedMechAnimation;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.boss.AbstractZombotEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

/**
 * 四足机甲（Zombot）共享模型。
 *
 * <p>植物大战僵尸 2 的四个时空 BOSS——狮身终结者、甲板漫步者、战争马车、明日破译者——共用这套
 * 四足机甲躯体，仅在头部的装饰部件上有差异。所有部件均已建模，渲染时按实体的
 * {@link AbstractZombotEntity.ZombotVariant} 控制显隐：
 * <ul>
 *   <li>{@code nemes}：法老头巾（狮身终结者）；</li>
 *   <li>{@code trick_rod} + {@code flag}：桅杆 + 海盗旗（甲板漫步者）；</li>
 *   <li>{@code cow}：牛（战争马车）；</li>
 *   <li>明日破译者：无额外部件。</li>
 * </ul>
 */
public class QuadrupedMechModel extends EntityModel<PvZZombieRenderState> implements IZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(StringUtil.id("quadruped_mech"), "main");

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart jaw;
    /** 召唤漩涡面片（挂在 head 下、位于 head 与 jaw 之间；默认隐藏，由 SummonVortexLayer 临时显示）。 */
    private final ModelPart vortex;
    private final ModelPart nemes;
    private final ModelPart left_nemes;
    private final ModelPart rigth_nemes;
    private final ModelPart top_nemes;
    private final ModelPart trick_rod;
    private final ModelPart flag;
    private final ModelPart cow;
    private final ModelPart cow_h;
    private final ModelPart body;
    /** 构造时记录的枢轴基准 Y：setupAnim 必须复位到它，不能写 0，否则部件会被挪进头部。 */
    private final float baseHeadY;
    private final float baseBodyY;
    /** 四段腿枢轴基准 Y（蓄力下蹲时 f1/f2 与 h1/h2 在其上叠加，f3/h3 不动）。 */
    private final float baseF1Y;
    private final float baseF2Y;
    private final float baseH1Y;
    private final float baseH2Y;
    private final ModelPart leg;
    private final ModelPart left_front;
    private final ModelPart left_f1;
    private final ModelPart left_f2;
    private final ModelPart left_f3;
    private final ModelPart right_front;
    private final ModelPart right_f1;
    private final ModelPart right_f2;
    private final ModelPart right_f3;
    private final ModelPart left_hind;
    private final ModelPart left_h1;
    private final ModelPart left_h2;
    private final ModelPart left_h3;
    private final ModelPart right_hind;
    private final ModelPart right_h1;
    private final ModelPart right_h2;
    private final ModelPart right_h3;
    /** 预烘焙的 Blockbench 动画（AnimationDefinition → KeyframeAnimation），构造时 bake 一次复用。 */
    private final Map<AnimationDefinition, KeyframeAnimation> animations = new HashMap<>();

    public QuadrupedMechModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.head = this.total.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.jaw = this.head.getChild("jaw");
        this.vortex = this.head.getChild("vortex");
        this.nemes = this.head.getChild("nemes");
        this.left_nemes = this.nemes.getChild("left_nemes");
        this.rigth_nemes = this.nemes.getChild("rigth_nemes");
        this.top_nemes = this.nemes.getChild("top_nemes");
        this.trick_rod = this.head.getChild("trick_rod");
        this.flag = this.head.getChild("flag");
        this.cow = this.head.getChild("cow");
        this.cow_h = this.cow.getChild("cow_h");
        this.body = this.total.getChild("body");
        this.baseHeadY = this.head.y;
        this.baseBodyY = this.body.y;
        this.leg = this.total.getChild("leg");
        this.left_front = this.leg.getChild("left_front");
        this.left_f1 = this.left_front.getChild("left_f1");
        this.left_f2 = this.left_front.getChild("left_f2");
        this.left_f3 = this.left_front.getChild("left_f3");
        this.right_front = this.leg.getChild("right_front");
        this.right_f1 = this.right_front.getChild("right_f1");
        this.right_f2 = this.right_front.getChild("right_f2");
        this.right_f3 = this.right_front.getChild("right_f3");
        this.left_hind = this.leg.getChild("left_hind");
        this.left_h1 = this.left_hind.getChild("left_h1");
        this.left_h2 = this.left_hind.getChild("left_h2");
        this.left_h3 = this.left_hind.getChild("left_h3");
        this.right_hind = this.leg.getChild("right_hind");
        this.right_h1 = this.right_hind.getChild("right_h1");
        this.right_h2 = this.right_hind.getChild("right_h2");
        this.right_h3 = this.right_hind.getChild("right_h3");
        // 腿段枢轴基准从模型读取，避免硬编码与 createBodyLayer 不同步
        this.baseF1Y = this.left_f1.y;
        this.baseF2Y = this.left_f2.y;
        this.baseH1Y = this.left_h1.y;
        this.baseH2Y = this.left_h2.y;
        // 预烘焙 Blockbench 动画定义（bake 会按骨骼名绑定到本模型的 ModelPart）
        this.animations.put(QuadrupedMechAnimation.IDLE, QuadrupedMechAnimation.IDLE.bake(root));
        this.animations.put(QuadrupedMechAnimation.SUMMON, QuadrupedMechAnimation.SUMMON.bake(root));
        this.animations.put(QuadrupedMechAnimation.MISSILE, QuadrupedMechAnimation.MISSILE.bake(root));
        this.animations.put(QuadrupedMechAnimation.CHARGING, QuadrupedMechAnimation.CHARGING.bake(root));
        this.animations.put(QuadrupedMechAnimation.SPRINT, QuadrupedMechAnimation.SPRINT.bake(root));
        this.animations.put(QuadrupedMechAnimation.IMP_CANNON, QuadrupedMechAnimation.IMP_CANNON.bake(root));
        this.animations.put(QuadrupedMechAnimation.JUMP, QuadrupedMechAnimation.JUMP.bake(root));
        this.animations.put(QuadrupedMechAnimation.ANGRY, QuadrupedMechAnimation.ANGRY.bake(root));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 6.2639F, -0.25F));

        PartDefinition head = total.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.7917F, -6.25F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.4722F, -0.5F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.7917F, -8.25F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -1.5F, -2.0F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.7083F, -5.25F));

        // 召唤漩涡面片：UV 覆盖整张 16x16 贴图，实际渲染由 SummonVortexLayer 的独立面片负责。
        // 这里的位置与 layer 保持一致：head 局部 y≈8.7 的嘴洞中心（head 底 5.21 / jaw 张开后顶 12.21）。
        PartDefinition vortex = head.addOrReplaceChild("vortex", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.7F, -6.5F));

        PartDefinition nemes = head.addOrReplaceChild("nemes", CubeListBuilder.create(), PartPose.offset(0.0F, -5.125F, -2.25F));

        PartDefinition left_nemes = nemes.addOrReplaceChild("left_nemes", CubeListBuilder.create().texOffs(36, 43).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 1.3333F, 0.0F));

        PartDefinition rigth_nemes = nemes.addOrReplaceChild("rigth_nemes", CubeListBuilder.create().texOffs(44, 43).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 1.3333F, 0.0F));

        PartDefinition top_nemes = nemes.addOrReplaceChild("top_nemes", CubeListBuilder.create().texOffs(32, 24).addBox(-8.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.6667F, 0.0F));

        PartDefinition trick_rod = head.addOrReplaceChild("trick_rod", CubeListBuilder.create().texOffs(8, 60).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.7917F, -9.25F, 1.0472F, 0.0F, 0.0F));

        PartDefinition flag = head.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(20, 43).addBox(-4.0F, -5.5F, 0.0F, 8.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.2917F, 4.75F));

        PartDefinition cow = head.addOrReplaceChild("cow", CubeListBuilder.create().texOffs(32, 28).addBox(-3.0F, -1.5F, -2.25F, 6.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.2917F, 8.0F));

        PartDefinition cow_h = cow.addOrReplaceChild("cow_h", CubeListBuilder.create().texOffs(48, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, -0.25F));

        PartDefinition body = total.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.7361F, 0.25F));

        PartDefinition leg = total.addOrReplaceChild("leg", CubeListBuilder.create(), PartPose.offset(0.0F, 11.7361F, 0.25F));

        PartDefinition left_front = leg.addOrReplaceChild("left_front", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, -5.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition left_f1 = left_front.addOrReplaceChild("left_f1", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 0.0F));

        PartDefinition cube_r1 = left_f1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition left_f2 = left_front.addOrReplaceChild("left_f2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r2 = left_f2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition left_f3 = left_front.addOrReplaceChild("left_f3", CubeListBuilder.create().texOffs(8, 50).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -1.0F, 0.0F));

        PartDefinition right_front = leg.addOrReplaceChild("right_front", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 0.0F, -5.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition right_f1 = right_front.addOrReplaceChild("right_f1", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 0.0F));

        PartDefinition cube_r3 = right_f1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 43).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition right_f2 = right_front.addOrReplaceChild("right_f2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r4 = right_f2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(48, 4).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition right_f3 = right_front.addOrReplaceChild("right_f3", CubeListBuilder.create().texOffs(52, 53).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.0F, 0.0F));

        PartDefinition left_hind = leg.addOrReplaceChild("left_hind", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, 5.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition left_h1 = left_hind.addOrReplaceChild("left_h1", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 0.0F));

        PartDefinition cube_r5 = left_h1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 54).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition left_h2 = left_hind.addOrReplaceChild("left_h2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r6 = left_h2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(48, 8).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition left_h3 = left_hind.addOrReplaceChild("left_h3", CubeListBuilder.create().texOffs(24, 54).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -1.0F, 0.0F));

        PartDefinition right_hind = leg.addOrReplaceChild("right_hind", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 0.0F, 5.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition right_h1 = right_hind.addOrReplaceChild("right_h1", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 0.0F));

        PartDefinition cube_r7 = right_h1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 28).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition right_h2 = right_hind.addOrReplaceChild("right_h2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r8 = right_h2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(48, 12).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition right_h3 = right_hind.addOrReplaceChild("right_h3", CubeListBuilder.create().texOffs(0, 60).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    /**
     * 按 {@link QuadrupedMechAnimation} 的 Blockbench 动画定义驱动动画（由渲染器从实体状态 + 血量事件
     * 映射后写入 {@code state.quadrupedAnim}，即 {@link AnimationDefinition}）。
     * <ul>
     *   <li>IDLE：站立腿部摆动循环；</li>
     *   <li>SUMMON：jaw 张合；</li>
     *   <li>MISSILE：眼睛放大；</li>
     *   <li>CHARGING：眼睛放大闪烁 + 右前腿刨地；</li>
     *   <li>SPRINT：四腿交替蹬跑；</li>
     *   <li>IMP_CANNON：眼睛放大闪烁；</li>
     *   <li>JUMP：整个跳跃期间一套动画；</li>
     *   <li>ANGRY：血量下降沿触发，前倾俯头 + 眼睛放大。</li>
     * </ul>
     *
     * <p>蓄力/冲刺阶段实体已通过 {@code setYBodyRot(dashYaw)} 让整体朝向冲刺方向，
     * 渲染器用 bodyRot 旋转模型，模型在 CHARGING/SPRINT 时额外把头部朝向锁定到 dashYaw。
     *
     * <p>头部的装饰部件（nemes / trick_rod / flag / cow）按 {@code state.zombotVariant} 显隐。
     */
    @Override
    public void setupAnim(PvZZombieRenderState state) {
        super.setupAnim(state);
        // 复位（含四肢，避免上一个状态的腿角度残留）
        this.jaw.xRot = 0;
        // 漩涡默认隐藏（仅在 SUMMON 冻结相位由 layer 临时显示），并每帧复位变换
        this.vortex.visible = false;
        this.vortex.xScale = 0.5F;
        this.vortex.yScale = 0.5F;
        this.vortex.zRot = 0F;
        this.body.xRot = 0;
        // 复位到建模时的枢轴 Y（body=4.7361、head=-1.4722），不能写 0：
        // 写 0 会把 body 挪进 head 内部导致躯干完全不可见。
        this.body.y = this.baseBodyY;
        this.head.y = this.baseHeadY;
        this.total.yRot = 0;
        // 眼睛（head 部件）默认跟随 netHeadYaw（头部绝对朝向 = bodyRot + yRot）。
        // 蓄力/冲刺时（CHARGING/SPRINT 分支）覆盖为精确锁定 dashYaw。
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.eyes.visible = true;
        this.eyes.xScale = 1F;
        this.eyes.yScale = 1F;
        this.left_front.xRot = 0;
        this.right_front.xRot = 0;
        this.left_hind.xRot = 0;
        this.right_hind.xRot = 0;
        // 三段腿复位
        this.left_f1.xRot = 0;
        this.left_f2.xRot = 0;
        this.left_f3.xRot = 0;
        this.right_f1.xRot = 0;
        this.right_f2.xRot = 0;
        this.right_f3.xRot = 0;
        this.left_h1.xRot = 0;
        this.left_h2.xRot = 0;
        this.left_h3.xRot = 0;
        this.right_h1.xRot = 0;
        this.right_h2.xRot = 0;
        this.right_h3.xRot = 0;
        // 腿段枢轴 Y 复位（蓄力下蹲会改 f1/f2/h1/h2 的 y，f3/h3 不动故无需复位）
        this.left_f1.y = this.baseF1Y;
        this.left_f2.y = this.baseF2Y;
        this.right_f1.y = this.baseF1Y;
        this.right_f2.y = this.baseF2Y;
        this.left_h1.y = this.baseH1Y;
        this.left_h2.y = this.baseH2Y;
        this.right_h1.y = this.baseH1Y;
        this.right_h2.y = this.baseH2Y;

        // 按实体变体控制头部装饰部件显隐
        final AbstractZombotEntity.ZombotVariant variant = state.zombotVariant;
        this.nemes.visible = variant.nemes;
        this.trick_rod.visible = variant.trickRod;
        this.flag.visible = variant.flag;
        this.cow.visible = variant.cow;

        final AnimationDefinition anim = state.quadrupedAnim;

        // SUMMON 用专用时钟（0.75s 处冻结）；其它动画用全局时钟。
        // looping 动画内部按 period 取模，冻结时时间不推进→画面定格。
        final boolean summon = anim == QuadrupedMechAnimation.SUMMON;
        final long animTime = summon ? state.summonAnimTime : state.quadrupedAnimTime;
        final KeyframeAnimation baked = anim == null ? null : this.animations.get(anim);
        if (baked != null) {
            baked.apply(animTime, 1.0F);
        }
        // 漩涡缓慢自转（绕 Z 轴，面片在 XY 平面内旋转），只在 SUMMON 时转
        this.vortex.zRot = summon ? state.summonAnimTime * 0.002F : 0F;

        // 蓄力/冲刺时头部精确锁定冲刺方向（朝向逻辑，非 Blockbench 动画，需在动画后覆盖）
        if (anim == QuadrupedMechAnimation.CHARGING || anim == QuadrupedMechAnimation.SPRINT) {
            this.head.yRot = Mth.wrapDegrees(state.dashYaw - state.bodyRot) * Mth.DEG_TO_RAD;
        }
    }

    /**
     * 漩涡渲染辅助：把主模型中 root → total → head 的父链变换叠加到给定 PoseStack 上。
     *
     * <p>SummonVortexLayer 用一个独立的漩涡面片部件渲染，直接 submitModelPart 只会应用面片
     * 自身的局部变换、丢失父链动画（head 在 SUMMON 0.75s 有 -10° 抬头与 +1 上移）。
     * 这里显式复刻父链，使独立面片与模型中的 head/vortex 处于同一坐标系；随后 layer 再
     * 提交面片自身变换，最终效果等同于把面片挂在 head 下一起渲染。
     *
     * <p>调用前应先执行 {@link #setupAnim(PvZZombieRenderState)} 让父链部件带上当前动画姿态。
     */
    public void applyVortexParentTransform(PoseStack poseStack) {
        // root 姿态通常为单位变换，仍显式叠加以完整复刻主模型链路
        this.root.translateAndRotate(poseStack);
        this.total.translateAndRotate(poseStack);
        this.head.translateAndRotate(poseStack);
    }

    @Override
    public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {

    }

    @Override
    public EntityModel<? extends EntityRenderState> getZombieModel() {
        return this;
    }
}
