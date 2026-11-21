package com.lying.pvz.client.render.renderer.entity.bullet;

import com.lying.pvz.client.render.bullet.BulletVisualRegistry;
import com.lying.pvz.client.render.bullet.VisualSpec;
import com.lying.pvz.client.render.state.entity.misc.BulletRenderState;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.projectile.BulletEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelSet;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

/**
 * 新弹药体系的唯一渲染器。
 *
 * <p>按 {@code visualKey} 解析 {@link VisualSpec}，支持物品 / 方块 / 模型三种形态。
 * 方块模型在这里统一居中（0..1 模型 → -0.5 平移），修复旧版黄油 / 西瓜偏离实体位置的问题。
 */
public class BulletRenderer extends EntityRenderer<BulletEntity, BulletRenderState> {

    private final ItemModelResolver itemModelResolver;
    private final EntityRendererProvider.Context context;
    private final Map<ModelLayerLocation, EntityModel<PvZEntityRenderState>> models = new HashMap<>();

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.context = context;
    }

    @Override
    public BulletRenderState createRenderState() {
        return new BulletRenderState();
    }

    @Override
    public void extractRenderState(BulletEntity entity, BulletRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.variant = entity.variant();
        state.visualKey = entity.kind().variant(entity.variant()).visualKey();
        state.yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        final VisualSpec spec = BulletVisualRegistry.get(state.visualKey);
        if (spec instanceof VisualSpec.Item item) {
            state.scale = item.scale();
            state.blockState = Blocks.AIR.defaultBlockState();
            state.layerTexture = null;
            this.itemModelResolver.updateForNonLiving(
                    state.item, item.stack().get(), ItemDisplayContext.GROUND, entity);
        } else if (spec instanceof VisualSpec.VariantItem variantItem) {
            state.scale = variantItem.scale();
            state.blockState = Blocks.AIR.defaultBlockState();
            state.layerTexture = null;
            this.itemModelResolver.updateForNonLiving(
                    state.item, variantItem.resolver().apply(entity.variant()),
                    ItemDisplayContext.GROUND, entity);
        } else if (spec instanceof VisualSpec.Block block) {
            state.scale = block.scale();
            state.blockState = block.state().get();
            state.layerTexture = null;
        } else if (spec instanceof VisualSpec.Model model) {
            state.scale = model.scale();
            state.texture = model.texture();
            state.layerTexture = model.layerTexture();
            state.spin = entity.tickCount() * model.spinSpeed();
            state.blockState = Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void submit(BulletRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        final VisualSpec spec = BulletVisualRegistry.get(state.visualKey);
        if (spec == null) {
            return;
        }

        poseStack.pushPose();
        if (spec instanceof VisualSpec.Block) {
            poseStack.scale(state.scale, state.scale, state.scale);
            // 方块模型坐标是 0..1，平移到以实体位置为中心。
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            this.submitBlock(state, poseStack, collector);
        } else if (spec instanceof VisualSpec.Model model) {
            this.submitModel(model, state, poseStack, collector);
        } else {
            poseStack.scale(state.scale, state.scale, state.scale);
            poseStack.rotate(camera.orientation);
            poseStack.rotateDegrees(Axis.YP, 180.0F);
            state.item.submit(poseStack, collector, state.lightCoords,
                    OverlayTexture.NO_OVERLAY, state.outlineColor);
        }
        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }

    private void submitBlock(BulletRenderState state, PoseStack poseStack,
                             SubmitNodeCollector collector) {
        if (state.blockState.isAir()) {
            return;
        }
        final BlockModelSet blockModels =
                Minecraft.getInstance().getModelManager().getBlockModelSet();
        final BlockModel blockModel = blockModels.get(state.blockState);
        final BlockModelRenderState modelState = new BlockModelRenderState();
        blockModel.update(modelState, state.blockState, BlockDisplayContext.create(), 0L);
        modelState.submit(poseStack, collector, state.lightCoords,
                OverlayTexture.NO_OVERLAY, 0);
    }

    private void submitModel(VisualSpec.Model visual, BulletRenderState state,
                             PoseStack poseStack, SubmitNodeCollector collector) {
        if (state.texture == null) {
            return;
        }
        final EntityModel<PvZEntityRenderState> model = this.models.computeIfAbsent(
                visual.layer(), layer -> visual.factory().apply(this.context.bakeLayer(layer)));

        switch (visual.rotation()) {
            case YAW_PITCH -> {
                poseStack.rotateDegrees(Axis.YP, state.yaw + 180.0F);
                poseStack.rotateDegrees(Axis.ZP, state.pitch);
            }
            case YAW -> poseStack.rotateDegrees(Axis.YP, state.yaw + 180.0F);
            case SPIN_X -> poseStack.rotateDegrees(Axis.XP, state.spin);
            case NONE -> {
            }
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(state.scale, state.scale, state.scale);
        poseStack.translate(0.0D, -1.501D, 0.0D);

        collector.submitModel(model, state, poseStack,
                RenderTypes.entityCutout(state.texture),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0);

        if (state.layerTexture != null) {
            collector.submitModel(model, state, poseStack,
                    RenderTypes.energySwirl(state.layerTexture, 0.0F, 0.0F),
                    state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFF7F7F7F, null, 0);
        }
    }
}
