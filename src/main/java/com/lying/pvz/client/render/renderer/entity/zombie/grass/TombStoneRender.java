package com.lying.pvz.client.render.renderer.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.grass.TombStoneModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.TombStoneEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * TombStone 渲染器。
 *
 * <p>与 {@code GigaTombStoneRender} 同构（共用 {@link TombStoneModel}）：
 * <ul>
 *   <li>{@code getScaleByEntity} 在僵尸侧是<b>活代码</b>（{@code PvZZombieRender}
 *       未覆写 {@code scale}，走 {@code PvZCreatureRender.scale} → 调用
 *       {@code getScaleByEntity(state)}）。改签名 {@code entity → state}，
 *       返回值 0.6f 原样保留。覆写后不走 {@code mini}/{@code renderScale}
 *       是旧行为，不修正。</li>
 *   <li>{@code getOffsetRisingHeight} 在 1.21.1 <b>无钩子</b>（rising 下沉由
 *       {@code PvZZombieModel.doRisingAnimation} 用模型侧常量
 *       {@code RISING_DEPTH_UNITS} 处理）。保留方法体、去 {@code @Override}，
 *       作逻辑存档。若需恢复 1f 偏移，需在 {@code PvZZombieModel} 新增钩子
 *       并让 {@code doRisingAnimation} 调用——跨层改动，需单独立项。</li>
 *   <li>{@code TombStoneModel<>} 泛型参数待核，与 {@code GigaTombStoneRender}
 *       <b>必须同改</b>。新 {@code PvZZombieModel} 已固定 {@code PvZZombieRenderState}，
 *       若模型定义未变则不应再带 {@code <>}。</li>
 * </ul>
 */
public class TombStoneRender extends PvZZombieRender<TombStoneEntity, TombStoneModel> {

	public TombStoneRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TombStoneModel(ctx.bakeLayer(TombStoneModel.LAYER_LOCATION)), 0.4f);
	}

	@Override
	protected float getScaleByEntity(PvZZombieRenderState state) {
		return 0.6f;
	}

	protected float getOffsetRisingHeight() {
		return 1f;
	}
}