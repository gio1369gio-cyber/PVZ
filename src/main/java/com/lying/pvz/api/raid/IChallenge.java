package com.lying.pvz.api.raid;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

/**
 * 一个正在运行的挑战实例。
 *
 * <p>本接口是 {@code RaidEvent} 等事件参数的类型，也是 {@link IRaidAPI} 查询方法的返回类型。
 * 实现层不得在本接口之外向调用方泄漏具体的 {@code Challenge} 类。
 */
public interface IChallenge {

    /**
     * 该实例对应的挑战类型 ID（即 JSON 中配置的 key）。
     */
    Identifier getID();

    /**
     * 挑战名称。
     *
     * <p>由实现基于 {@link #getID()} 生成，默认查翻译键
     * {@code raid.<namespace>.<path>}。
     */
    MutableComponent getName();

    /**
     * 挑战中心位置。
     */
    BlockPos getCenter();

    /**
     * 所在世界。挑战仅存在于服务端。
     */
    ServerLevel getWorld();

    /**
     * 该实例所使用的定义（datapack 数据）。
     */
    IChallengeDefinition getDefinition();
}