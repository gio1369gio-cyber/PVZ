package com.lying.pvz.client.render.renderer.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.pool.DuckyTubeModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieToolRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.misc.DuckyTubeEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * DuckyTube 渲染器。
 *
 * <p><b>特殊路径</b>：继承 {@link PvZZombieToolRender}（工具/坐骑类），
 * 而非 {@code PvZZombieRender}。Tool 基类的 {@code scale} 调
 * {@code getScaleByEntity(state)}（活代码），但无 rising / mini / 朝向特化。
 *
 * <p><b>泛型</b>：旧 1 泛型 {@code <DuckyTubeEntity>} → 新 3 泛型
 * {@code <T, S, M>}。<b>S 用 {@link PvZZombieRenderState}</b>：受
 * {@code M extends EntityModel<? super S>} 约束，若 {@link DuckyTubeModel}
 * 继承 {@code PvZEntityModel<PvZZombieRenderState>} 或 {@code PvZZombieModel}
 * （均满足 {@code EntityModel<? super PvZZombieRenderState>}），此选择正确。
 * 若模型泛型不同，按 {@code M extends EntityModel<? super S>} 约束调整 S。
 *
 * <p><b>createRenderState 必须覆写</b>：{@code MobRenderer} / {@code EntityRenderer}
 * 的 {@code createRenderState()} 是 abstract，基类未提供默认实现。
 */
public class DuckyTubeRender extends PvZZombieToolRender<DuckyTubeEntity, PvZZombieRenderState, DuckyTubeModel> {

	public DuckyTubeRender(EntityRendererProvider.Context ctx) {
		super(ctx, new DuckyTubeModel(ctx.bakeLayer(DuckyTubeModel.LAYER_LOCATION)), 0);
	}

	@Override
	public PvZZombieRenderState createRenderState() {
		return new PvZZombieRenderState();
	}

	@Override
	protected float getScaleByEntity(PvZZombieRenderState state) {
		return 0.6f;
	}

	@Override
	public Identifier getTextureLocation(PvZZombieRenderState state) {
		return StringUtil.id("textures/entity/zombie/pool/ducky_tube.png");
	}
}