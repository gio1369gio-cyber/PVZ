package com.lying.pvz.client.render.renderer.entity.zombie.body;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.ability.BodyType;
import com.lying.pvz.client.registry.ClientModelRegistry;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.ZombieDropBodyRenderState;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.Optional;
import java.util.function.Function;

/**
 * 僵尸掉落肢体渲染器。
 *
 * <p>走 1.21 三段式：{@code createRenderState → extractRenderState → submit}。
 * {@link ZombieDropBodyRenderState} 直接 {@code implements IBodyEntity}，
 * 作为 {@link IZombieModel#tickPartAnim} / {@link IZombieModel#renderBody} 的
 * {@code IBodyEntity} 数据来源。
 *
 * <p><b>贴图 / scale</b>：从 {@code PVZAPI.client().paz(typeId)} 拿
 * {@link IClientPAZInfo}，与 {@code PvZPlantRender} / {@code PvZZombieRender} 一致。
 *
 * <p><b>模型工厂</b>：从 {@code ClientModelRegistry.getZombieModel(typeId)} 拿。
 * 旧代码用 {@code IZombieType.getZombieModel1/2()} 区分 HAND/HEAD 与 BODY，
 * 新 API 只提供一个 map，暂统一用同一 factory。若新 API 有区分，改为按
 * {@code part} 走两个 factory。
 *
 * <p><b>mini 契约</b>：{@code state.mini} 从 {@link ZombieDropBodyEntity#isMini()}
 * 填充（掉落时快照），<b>不</b>用 {@code ZombieEntity.isMiniZombie()}。
 */
public class ZombieBodyRender extends EntityRenderer<ZombieDropBodyEntity, ZombieDropBodyRenderState> {

    private static final Identifier FALLBACK_TEXTURE = StringUtil.id( "entity/zombie/missing");

    private final EntityRendererProvider.Context context;

    public ZombieBodyRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public ZombieDropBodyRenderState createRenderState() {
        return new ZombieDropBodyRenderState();
    }

    @Override
    public void extractRenderState(ZombieDropBodyEntity entity, ZombieDropBodyRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.zombieType  = entity.getZombieType();
        state.bodyType    = entity.getBodyType();
        state.animTime    = entity.getAnimTime();
        state.handDefence = entity.hasHandDefence();
        state.mini        = entity.isMini();

        state.yRot        = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        state.onGround    = entity.onGround();
        state.headRot     = entity.HEAD_ROT;
        state.tickCount   = entity.tickCount;
    }

    @Override
    public void submit(ZombieDropBodyRenderState state, PoseStack stack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        final BodyType part = state.bodyType;
        final Identifier typeId = state.zombieType.getID();

        final Optional<IClientPAZInfo> info = PVZAPI.client().paz(typeId);
        final float renderScale = info.map(IClientPAZInfo::getRenderScale).orElse(1.0F);
        final Identifier texture = info.map(IClientPAZInfo::getTexture).orElse(FALLBACK_TEXTURE);

        final Optional<Function<EntityModelSet, ? extends IZombieModel>> factory =
                ClientModelRegistry.getZombieModel(typeId);
        if (factory.isEmpty()) {
            return;
        }

        stack.pushPose();
        stack.rotateDegrees(Axis.YP, state.yRot + 180.0F);

        final float sz = state.mini ? renderScale * PvZZombieRender.MINI_SCALE : renderScale;
        stack.scale(-1, -1, 1);
        stack.scale(sz, sz, sz);
        stack.translate(0.0, part != BodyType.BODY ? -1.501 : -1.7, 0.0);

        if (part != BodyType.BODY) {
            final float pivotY = part == BodyType.HEAD ? 1.0625F : 2.0F;
            final float angle = state.onGround
                    ? (part == BodyType.HEAD ? state.headRot : 90.0F)
                    : -state.tickCount * 15.0F;
            stack.rotateAround(Axis.XP.rotationDegrees(angle), 0.0F, pivotY, 0.0F);
        }

        final IZombieModel m = factory.get().apply(this.context.getModelSet());
        final RenderType rt = m.getZombieModel().renderType(texture);
        m.tickPartAnim(state, 0, 0, state.ageInTicks, 0, 0);
        m.renderBody(state, stack, collector, rt, state.lightCoords, OverlayTexture.NO_OVERLAY);

        stack.popPose();
    }
}