package com.lying.pvz.api.entity;

import net.minecraft.network.chat.MutableComponent;

/**
 * 图鉴条目属性名。值由 {@code IPAZEntity#addAlmanacEntries} 中配对的
 * {@code Number} 提供，渲染时组合为"名称: 数值"。
 */
public interface IAlmanacEntry {

    MutableComponent getText();
}