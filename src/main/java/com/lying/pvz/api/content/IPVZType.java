package com.lying.pvz.api.content;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

/**
 * 所有 PVZ 内容类型的基础契约。
 *
 * <p>每个类型用 {@link Identifier} 唯一标识；NBT / 网络编解码均以 {@link #getID()} 为准。
 * 翻译文本统一返回 {@link MutableComponent}，不再泄漏原始 translate key。
 *
 * <p>本接口<b>只描述"注册内容"</b>（数据包 / 代码注册的元数据），
 * 不涉及运行时实体行为；运行时契约见 {@code com.lying.pvz.api.entity}。
 */
public interface IPVZType {

    /**
     * 唯一标识，{@code namespace:path} 形式。
     */
    Identifier getID();

    /**
     * 命名空间（mod id）。
     */
    default String getModID() {
        return getID().getNamespace();
    }

    /**
     * 显示名称（已含翻译与样式）。
     */
    MutableComponent getText();
}