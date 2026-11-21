package com.lying.pvz.client.render.state.entity.zombie;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.api.entity.ability.BodyType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 掉落肢体渲染状态。
 *
 * <p>直接实现 {@link IBodyEntity}，作为 {@code IZombieModel.tickPartAnim / renderBody}
 * 的数据来源。字段全部来自 {@code ZombieDropBodyEntity} 的只读快照。
 *
 * <p><b>不复用 {@link PvZZombieRenderState}</b>：
 * <ul>
 *   <li>语义不同：本体 mini 走 {@code ZombieEntity.isMiniZombie()}，
 *       掉落肢体 mini 走 {@code ZombieDropBodyEntity.isMini()}——同一僵尸被拆开后
 *       掉落肢体的 mini 可能与本体的 mini 状态不同步（构造时快照）。</li>
 *   <li>本类 {@code extends EntityRenderState}（非 Living），无 walk/pose 字段。</li>
 * </ul>
 */
public class ZombieDropBodyRenderState extends EntityRenderState implements IBodyEntity {

    public IZombieType zombieType;
    public BodyType bodyType;
    /** 存活 tick 数，{@code ZombieDropBodyEntity.getAnimTime()}。 */
    public int animTime;
    public boolean handDefence;
    public boolean mini;

    /* 渲染用额外字段 */
    /** 插值后的 yRot（度），用于朝向。 */
    public float yRot;
    public boolean onGround;
    /** {@code ZombieDropBodyEntity.HEAD_ROT}，头部随机旋转角。 */
    public int headRot;
    /** {@code ZombieDropBodyEntity.tickCount}，用于空中翻滚。 */
    public int tickCount;

    @Override public IZombieType getZombieType() { return zombieType; }
    @Override public BodyType getBodyType() { return bodyType; }
    @Override public boolean hasHandDefence() { return handDefence; }
    @Override public boolean isMini() { return mini; }
    @Override public int getAnimTime() { return animTime; }
}