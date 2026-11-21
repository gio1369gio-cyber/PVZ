package com.lying.pvz.client.render.renderer.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.other.CoffinModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.other.CoffinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * Coffin 渲染器。
 *
 * <p><b>getOffsetRisingHeight</b>：1.21.1 的 {@code PvZZombieRender} /
 * {@code PvZCreatureRender} <b>没有</b>这个钩子。rising 下沉由
 * {@code PvZZombieModel.doRisingAnimation} 用模型侧常量
 * {@code RISING_DEPTH_UNITS} 处理，renderer 侧无对应回调。
 * 因此本方法在 1.21.1 下<b>无调用点（死代码）</b>。
 * 按迁移原则保留方法体、去掉 {@code @Override}，作为逻辑存档。
 * 与 {@code TombStoneRender} / {@code GigaTombStoneRender} 同模板。
 *
 * <p>若日后需恢复 Coffin 的 2.5f 偏移，需在 {@code PvZZombieModel} 新增钩子
 * 并让 {@code doRisingAnimation} 调用——跨层改动，需单独立项。
 *
 * <p>shadow = 0f 原样保留（Coffin 通常由其他渲染路径处理阴影）。
 */
public class CoffinRender extends PvZZombieRender<CoffinEntity, CoffinModel> {

	public CoffinRender(EntityRendererProvider.Context ctx) {
		super(ctx, new CoffinModel(ctx.bakeLayer(CoffinModel.LAYER_LOCATION)), 0f);
	}

	@Override
	public void extractRenderState(CoffinEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.percentHealth = entity.getHealth() / entity.getMaxHealth();
	}

	// ⚠️ 1.21.1 无父类方法可 @Override：保留方法体作逻辑存档
	protected float getOffsetRisingHeight() {
		return 2.5f;
	}
}