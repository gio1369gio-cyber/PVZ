package com.lying.pvz.api.content.paz;

/**
 * 僵尸类型。
 *
 * <p>僵尸专属元数据（如断肢、攀升等）由 trait / 能力接口扩展；
 * 渲染模型与贴图由客户端 {@code IClientPAZInfo} 按 {@link #getID()} 绑定。
 */
public interface IZombieType extends IPAZType {
}