package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.render.layer.DuckyTubeLayer;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.zombotany.AbstractZombotanyEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

/**
 * 植物僵尸混合体渲染器基类（zombotany 包）。
 *
 * <p><b>迁移点</b>：
 * <ul>
 *   <li>泛型 1 → 2：{@code PvZZombieRender<T, M>}，S 固定为 {@link PvZZombieRenderState}。</li>
 *   <li>{@code getTranslateVec(T)} → {@code getTranslateVec(PvZZombieRenderState)}：
 *       读 {@link PvZZombieRenderState#attackTime}（<b>注意</b>：本基类读
 *       {@code getAttackTime()}，<b>不是</b> {@code getAnimTime()}；该字段已在
 *       DiggerZombie 批次新增，<b>复用不重复声明</b>）与 {@code state.isInWater}
 *       （{@code EntityRenderState} 提供）。offset 0.6f 原样保留。</li>
 *   <li>DuckyTubeLayer 泛型按新架构 {@code <S, M>} 调整，与
 *       {@code AbstractNormalRender} 一致（同一待核项）。</li>
 * </ul>
 */
public abstract class AbstractZombotanyRender<
		T extends AbstractZombotanyEntity,
		M extends EntityModel<? super PvZZombieRenderState>>
		extends PvZZombieRender<T, M> {

	public AbstractZombotanyRender(EntityRendererProvider.Context ctx,
								   M model,
								   float shadowSize) {
		super(ctx, model, shadowSize);
	}

	@Override
	protected void addZombieLayers() {
		super.addZombieLayers();
		this.addLayer(new DuckyTubeLayer<PvZZombieRenderState, M>(this, this.context.getModelSet()));
	}

	@Override
	public Vec3 getTranslateVec(PvZZombieRenderState state) {
		if (state.attackTime >= 0 && state.isInWater) {
			return new Vec3(0, 0.6f, 0);
		}
		return super.getTranslateVec(state);
	}
}