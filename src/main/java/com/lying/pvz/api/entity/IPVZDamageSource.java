package com.lying.pvz.api.entity;

/**
 * PVZ 伤害来源标记接口。
 *
 * <p>用于区分本模组产生的伤害来源（如豌豆、地刺反伤）与通用伤害。
 * 具体实现由 common 层提供，API 层只暴露此标记供第三方做 {@code instanceof} 判定。
 */
public interface IPVZDamageSource {
}