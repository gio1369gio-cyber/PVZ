package com.lying.pvz.client.render.renderer.entity.zombie.boss;

import com.lying.pvz.client.animal.QuadrupedMechAnimation;
import com.lying.pvz.client.model.entity.zombie.boss.QuadrupedMechModel;
import com.lying.pvz.client.render.layer.SummonVortexLayer;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.boss.AbstractZombotEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * 四足机甲（Zombot）共享渲染器：四个时空 BOSS（狮身终结者 / 甲板漫步者 / 战争马车 / 明日破译者）
 * 共用同一模型与渲染器，按实体类型区分贴图与装饰部件。
 */
public class QuadrupedMechRender <T extends AbstractZombotEntity> extends PvZZombieRender<T, QuadrupedMechModel> {


    public QuadrupedMechRender(EntityRendererProvider.Context ctx) {
        super(ctx, new QuadrupedMechModel(ctx.bakeLayer(QuadrupedMechModel.LAYER_LOCATION)), 0);
    }

    @Override
    protected void addZombieLayers() {
        super.addZombieLayers();
        this.addLayer(new SummonVortexLayer<>(this));
    }

    @Override
    public void extractRenderState(T entity, PvZZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        // 实体 AI 状态 + 血量事件 → 模型动画状态
        state.quadrupedAnim = mapAnimation(entity);
        // 动画时间取服务端同步的时钟（暂停时冻结），附带 partialTick 平滑；暂停时不推进以免画面抖动
        state.quadrupedAnimTime = entity.getAnimClockMillis()
                + (entity.isAnimPaused() ? 0L : (long) (partialTick * 50.0F));
        // SUMMON 用专用时钟（相位 2 冻结时不叠加 partialTick，jaw 完全定格）
        state.summonPhase = entity.getSummonPhase();
        state.summonAnimTime = entity.getSummonAnimMillis()
                + (state.summonPhase == 2 ? 0L : (long) (partialTick * 50.0F));
        // 保留 summonTime 传实体状态 ordinal，供旧逻辑/调试参考（模型动画不再读它）
        state.summonTime = entity.getState().ordinal();
        state.dashYaw = entity.getDashYaw();
        // 供跳跃动画读取竖直速度，区分「下蹲蓄力 / 上升 / 下落」阶段
        state.deltaMovement = entity.getDeltaMovement();
        // 装饰部件显隐由实体变体决定
        state.zombotVariant = entity.getZombotVariant();
    }

    /** 实体状态 → 模型动画映射；ANGRY 仅在 NORMAL（空闲）时由血量下降沿队列触发。 */
    private static AnimationDefinition mapAnimation(AbstractZombotEntity entity) {
        return switch (entity.getState()) {
            case SUMMON -> QuadrupedMechAnimation.SUMMON;
            case MISSILE -> QuadrupedMechAnimation.MISSILE;
            case DASH_CHARGE -> QuadrupedMechAnimation.CHARGING;
            case DASH -> QuadrupedMechAnimation.SPRINT;
            case IMP_CANNON -> QuadrupedMechAnimation.IMP_CANNON;
            case JUMP, JUMP_CHARGE -> QuadrupedMechAnimation.JUMP;
            case NORMAL -> entity.isAngryPlaying()
                    ? QuadrupedMechAnimation.ANGRY : QuadrupedMechAnimation.IDLE;
        };
    }
}
