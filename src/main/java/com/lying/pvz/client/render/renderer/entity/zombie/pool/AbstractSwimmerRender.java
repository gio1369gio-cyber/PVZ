package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.base.SwimmerZombieEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

/**
 * 游泳僵尸渲染器基类（pool 包）。
 *
 * <p><b>迁移点</b>：
 * <ul>
 *   <li>泛型 1 → 2：{@code PvZZombieRender<T, M>}，S 固定为 {@link PvZZombieRenderState}。</li>
 *   <li>{@code getTranslateVec(T)} → {@code getTranslateVec(PvZZombieRenderState)}：
 *       读 {@link PvZZombieRenderState#mini}（基类已填）与 {@code state.pose}
 *       （{@code LivingEntityRenderState} 提供，父类 {@code extractRenderState} 填充）。
 *       分支逻辑（非 mini + SWIMMING → 2.5f；SPIN_ATTACK → 1.25f；否则回退 super）
 *       原样保留，不修正计算。</li>
 *   <li>⚠️ {@code Pose.SPIN_ATTACK} 在 1.21.1 待核（重锤攻击相关枚举，应按 1.20.5+
 *       保留；若移除需改判定条件）。</li>
 * </ul>
 */
public abstract class AbstractSwimmerRender<
		T extends SwimmerZombieEntity,
		M extends EntityModel<? super PvZZombieRenderState>>
		extends PvZZombieRender<T, M> {

	public AbstractSwimmerRender(EntityRendererProvider.Context ctx,
								 M model,
								 float shadowSize) {
		super(ctx, model, shadowSize);
	}

	@Override
	public Vec3 getTranslateVec(PvZZombieRenderState state) {
		if (!state.mini) {
			if (state.pose == Pose.SWIMMING) return new Vec3(0, 2.5f, 0);
			if (state.pose == Pose.SPIN_ATTACK) return new Vec3(0, 1.25f, 0);
		}
		return super.getTranslateVec(state);
	}
}