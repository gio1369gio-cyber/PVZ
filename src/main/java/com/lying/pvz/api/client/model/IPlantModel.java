package com.lying.pvz.api.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 植物模型接口。
 *
 * <p><b>加载约束</b>：本接口 import 客户端专用类型，<b>禁止服务端引用</b>。
 * 由客户端 {@code ClientModelRegistry} 按 {@code IPlantType.getID()} 持有工厂。
 *
 * <p><b>1.21 渲染系统</b>：{@code EntityModel<T>} 的 {@code T} 是
 * {@link EntityRenderState}，不是实体类型。因此本接口不带实体泛型。
 */

public interface IPlantModel {

    EntityModel<? extends EntityRenderState> getPlantModel();
}