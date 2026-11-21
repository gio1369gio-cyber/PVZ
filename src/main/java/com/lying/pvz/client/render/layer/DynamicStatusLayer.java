package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.client.render.status.StatusContext;
import com.lying.pvz.client.render.status.StatusVisual;
import com.lying.pvz.client.render.status.StatusVisualRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * 状态视觉动态层。
 *
 * <p>一个 Layer 消费 {@link StatusVisualRegistry} 的全部规则，按优先级渲染：
 * 全身层直接叠模型，粒子按固定间隔在头顶生成。新增效果不需要新 Layer。
 */
public class DynamicStatusLayer<S extends LivingEntityRenderState,
        M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    public DynamicStatusLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (state.isInvisible) {
            return;
        }
        final StatusContext context = context(state);
        if (context == null) {
            return;
        }
        final List<StatusVisual> visuals = StatusVisualRegistry.collect(context);
        if (visuals.isEmpty()) {
            return;
        }

        final int tick = (int) context.ageInTicks();
        final boolean onceThisTick = tick != lastParticleTick(state);
        boolean spawned = false;

        for (StatusVisual visual : visuals) {
            if (visual instanceof StatusVisual.FullSkin skin) {
                collector.order(0).submitModel(
                        this.getParentModel(), state, poseStack,
                        RenderTypes.entityTranslucent(skin.texture()),
                        lightCoords, OverlayTexture.NO_OVERLAY,
                        skin.color(), null, state.outlineColor);
            } else if (visual instanceof StatusVisual.Particle particle) {
                final int interval = Math.max(1, particle.intervalTicks());
                if (onceThisTick && tick % interval == 0) {
                    this.spawnParticles(state, particle);
                    spawned = true;
                }
            }
        }

        if (spawned) {
            setLastParticleTick(state, tick);
        }
    }

    private void spawnParticles(S state, StatusVisual.Particle particle) {
        final Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        final RandomSource random = level.getRandom();
        final double spread = Math.max(0.12D, state.boundingBoxWidth * 0.4D);
        final double x = state.x;
        final double y = state.y + state.boundingBoxHeight * particle.heightRatio();
        final double z = state.z;

        for (int i = 0; i < particle.count(); i++) {
            level.addParticle(particle.particle().get(),
                    x + (random.nextDouble() - 0.5D) * spread,
                    y + (random.nextDouble() - 0.5D) * 0.12D,
                    z + (random.nextDouble() - 0.5D) * spread,
                    0.0D, 0.02D, 0.0D);
        }
    }

    private static StatusContext context(LivingEntityRenderState state) {
        if (state instanceof PvZZombieRenderState zombie) {
            return new StatusContext(zombie.cold, zombie.frozen, zombie.buttered,
                    zombie.marks, zombie.ageInTicks);
        }
        if (state instanceof PvZPlantRenderState plant) {
            return new StatusContext(plant.cold, plant.frozen, plant.buttered,
                    plant.marks, plant.ageInTicks);
        }
        return null;
    }

    private static int lastParticleTick(LivingEntityRenderState state) {
        if (state instanceof PvZZombieRenderState zombie) {
            return zombie.statusParticleTick;
        }
        if (state instanceof PvZPlantRenderState plant) {
            return plant.statusParticleTick;
        }
        return -1;
    }

    private static void setLastParticleTick(LivingEntityRenderState state, int tick) {
        if (state instanceof PvZZombieRenderState zombie) {
            zombie.statusParticleTick = tick;
        } else if (state instanceof PvZPlantRenderState plant) {
            plant.statusParticleTick = tick;
        }
    }
}
