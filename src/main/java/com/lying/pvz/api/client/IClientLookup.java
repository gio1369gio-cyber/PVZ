package com.lying.pvz.api.client;

import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * 客户端只读查询视图。
 *
 * <p><b>加载约束</b>：本接口本身<b>不 import 任何 {@code net.minecraft.client.*} 类型</b>，
 * 可以安全出现在公共包中，不会触发服务端类加载。仅在客户端可用；
 * 服务端调用 {@code PVZAPI.client()} 会抛异常。
 */
public interface IClientLookup {

    Optional<IClientPAZInfo> paz(Identifier id);
}