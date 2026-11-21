package com.lying.pvz.client.impl;

import com.lying.pvz.api.client.IClientLookup;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.registry.ClientModelRegistry;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * 客户端 {@link IClientLookup} 的实现。
 *
 * <p>从 {@link ClientModelRegistry} 读取渲染元数据。若某 ID 未注册，
 * 返回 {@link Optional#empty()}——调用方（renderer）应自行兜底。
 */
public final class PVZClientLookupImpl implements IClientLookup {

    @Override
    public Optional<IClientPAZInfo> paz(Identifier id) {
        return ClientModelRegistry.getInfo(id);
    }
}