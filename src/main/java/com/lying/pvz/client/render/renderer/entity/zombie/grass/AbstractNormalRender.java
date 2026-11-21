package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.render.layer.DuckyTubeLayer;
import com.lying.pvz.client.render.layer.ZombieBeardLayer;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.NormalZombieEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * 普通僵尸渲染器基类（grass 包）。
 *
 * <p><b>迁移点</b>：
 * <ul>
 *   <li>泛型 1 → 2：{@code PvZZombieRender<T, M>}，S 固定为 {@link PvZZombieRenderState}。</li>
 *   <li>{@code getTranslateVec(T)} → {@code getTranslateVec(PvZZombieRenderState)}：
 *       读 {@link PvZZombieRenderState#animTime}（基类已填）与 {@code state.isInWater}
 *       （{@code EntityRenderState} 提供，由父类 {@code extractRenderState} 填充）。
 *       {@code animTime >= 0 && isInWater} 分支与 offset 0.6f 原样保留，不修正计算。</li>
 *   <li>Layer 泛型按新架构 {@code <S, M>} 调整：{@code DuckyTubeLayer<PvZZombieRenderState, M>}（待核）。</li>
 * </ul>
 */
public abstract class AbstractNormalRender<
		T extends NormalZombieEntity,
		M extends EntityModel<? super PvZZombieRenderState>>
		extends PvZZombieRender<T, M> {

	public AbstractNormalRender(EntityRendererProvider.Context ctx,
								M model,
								float shadowSize) {
		super(ctx, model, shadowSize);
	}

	@Override
	protected void addZombieLayers() {
		super.addZombieLayers();
		this.addLayer(new ZombieBeardLayer<>(this));
		this.addLayer(new DuckyTubeLayer<PvZZombieRenderState, M>(this, this.context.getModelSet()));
	}

	@Override
	public Vec3 getTranslateVec(PvZZombieRenderState state) {
		if (state.animTime >= 0 && state.isInWater) {   // offset for render ducky tube.
			return new Vec3(0, 0.6f, 0);
		}
		return super.getTranslateVec(state);
	}
}