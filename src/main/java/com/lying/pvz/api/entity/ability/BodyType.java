package com.lying.pvz.api.entity.ability;

/**
 * 身体部位。
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 被 NBT / 网络持久化。
 * <b>新值只能追加在末尾</b>，不能在中间插入或重排。
 */
public enum BodyType {

    HAND,
    HEAD,
    BODY,
    LEFT_HAND,
    RIGHT_HAND,
    LEFT_LEG,
    RIGHT_LEG
}