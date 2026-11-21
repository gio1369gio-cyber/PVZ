package com.lying.pvz.client.render.renderer.entity.zombie.roof;

import com.lying.pvz.client.model.entity.zombie.roof.EdgarRobotModel;
import com.lying.pvz.client.render.renderer.entity.zombie.PvZZombieRender;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.base.EdgarRobotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * EdgarRobot 渲染器（可被继承，<T> 泛型供子类指定具体实体类型）。
 *
 * <p><b>泛型保留</b>：旧 {@code EdgarRobotRender<T extends EdgarRobotEntity>} 的
 * {@code <T>} 是给子类用的（子类指定具体实体类型），必须保留。新架构下
 * {@code PvZZombieRender<T, M>} 的 {@code M} 固定为 {@link EdgarRobotModel}。
 *
 * <p><b>EdgarRobotModel&lt;&gt; 泛型待核</b>：旧代码 {@code new EdgarRobotModel<>(...)}
 * 显式泛型暗示曾带泛型参数；新 {@code PvZZombieModel} 已固定 {@code PvZZombieRenderState}，
 * 若模型定义未变则不应再带 {@code <>}。若 {@code EdgarRobotModel} <b>仍带</b>
 * {@code <T extends ...>}，则本类需改为
 * {@code extends PvZZombieRender<T, EdgarRobotModel<T>>}（且需满足
 * {@code EdgarRobotModel<T>} extends {@code EntityModel<? super PvZZombieRenderState>}）。
 * 以实际模型签名为准。
 */
public class EdgarRobotRender<T extends EdgarRobotEntity> extends PvZZombieRender<T, EdgarRobotModel> {

	public EdgarRobotRender(EntityRendererProvider.Context ctx) {
		super(ctx, new EdgarRobotModel(ctx.bakeLayer(EdgarRobotModel.LAYER_LOCATION)), 0);
	}

	@Override
	public void extractRenderState(T entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.robotState = entity.getRobotState();
		state.animShootCD = entity.getAnimShootCD();
		state.animThrowCD = entity.getAnimThrowCD();
		state.animStealCD = entity.getAnimStealCD();
	}
}