package com.lying.pvz.client.render.renderer.entity.zombie.custom;

import com.lying.pvz.client.model.entity.zombie.grass.TombStoneModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.custom.GigaTombStoneEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * GigaTombStone 渲染器。
 *
 * <p><b>getScaleByEntity</b>：僵尸侧 {@code PvZZombieRender} 未覆写 {@code scale}，
 * 继承 {@code PvZCreatureRender.scale}（其内部调用 {@code getScaleByEntity(state)}），
 * 所以此覆写在新架构下<b>是活代码</b>（与植物侧相反）。只需改签名 {@code entity → state}，
 * 返回值 0.7f 原样保留。注意：覆写后不再走基类的
 * {@code state.mini ? renderScale*MINI_SCALE : renderScale} 逻辑——这是旧代码
 * 行为（直接 return 0.7f），原样保留，不修正。
 *
 * <p><b>getOffsetRisingHeight</b>：1.21.1 的 {@code PvZZombieRender} /
 * {@code PvZCreatureRender} <b>没有</b>这个钩子。rising 下沉由
 * {@code PvZZombieModel.doRisingAnimation} 用模型侧常量 {@code RISING_DEPTH_UNITS}
 * 处理，renderer 侧无对应回调。因此本方法在 1.21.1 下<b>无调用点（死代码）</b>。
 * 按迁移原则保留方法体、去掉 {@code @Override}，作为逻辑存档。
 * 若日后需恢复 Giga 的 1.1f 偏移，需在 {@code PvZZombieModel} 新增钩子并让
 * {@code doRisingAnimation} 调用——跨层改动，需单独立项。
 *
 * <p><b>TombStoneModel&lt;&gt;</b>：旧代码显式泛型暗示曾带泛型参数；新
 * {@code PvZZombieModel} 已固定 {@code PvZZombieRenderState}，若模型定义未变则
 * 不应再带 {@code <>}。以实际模型签名为准。
 */
public class GigaTombStoneRender extends PvZZombieRender<GigaTombStoneEntity, TombStoneModel> {

	public GigaTombStoneRender(EntityRendererProvider.Context ctx) {
		super(ctx, new TombStoneModel(ctx.bakeLayer(TombStoneModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	protected float getScaleByEntity(PvZZombieRenderState state) {
		return 0.7f;
	}

	// ⚠️ 1.21.1 无父类方法可 @Override：保留方法体作逻辑存档
	protected float getOffsetRisingHeight() {
		return 1.1f;
	}
}