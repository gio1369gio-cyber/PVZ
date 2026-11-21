package com.lying.pvz.api.card;

import net.minecraft.network.chat.MutableComponent;

/**
 * 卡片使用失败的错误码。
 *
 * <p>每个实现返回一条可直接展示给玩家的文本。翻译与颜色由实现自行决定；
 * 调用方只负责在合适位置（聊天栏、ActionBar 等）发送。
 */
public interface ICardError {

    /**
     * 生成展示文本。
     *
     * @param args 可选参数（如缺失的阳光数量、无效卡片的类型名）。
     *             无参数时传空数组或 {@code 0} 均可；具体语义由实现决定。
     */
    MutableComponent format(Object... args);
}