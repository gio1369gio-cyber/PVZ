package com.lying.pvz.api.client;

import net.minecraft.resources.Identifier;

/**
 * 客户端渲染元数据（纯数据，不含客户端类型）。
 *
 * <p><b>加载约束</b>：本接口<b>不 import 任何 {@code net.minecraft.client.*} 类型</b>，
 * 可以安全在公共包出现。具体模型 / 图层由客户端
 * {@code EntityRenderersEvent} 按 {@link Identifier} 绑定。
 */
public interface IClientPAZInfo {

    /**
     * 默认渲染缩放。
     */
    float getRenderScale();

    /**
     * 纹理路径（相对 {@code assets/<namespace>/textures/}）。
     */
    Identifier getTexture();
}