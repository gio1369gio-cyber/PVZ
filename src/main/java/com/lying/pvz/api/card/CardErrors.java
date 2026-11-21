package com.lying.pvz.api.card;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 卡片通用错误。
 *
 * <p>植物放置专属错误由实现层的 {@code PlantPlacementErrors} 提供。
 * 每条错误对应翻译键 {@code help.pvz.<id>}，文本渲染为红色。
 */
public enum CardErrors implements ICardError {

    CD_ERROR("cd"),
    LOCK_ERROR("lock"),
    SUN_ERROR("sun"),
    MULTIPLE_SUN_ERROR("multiple_sun"),
    EMPTY_CONTENT("empty_content"),
    INVALID_CONTENT("invalid_content");

    private final String id;

    CardErrors(String id) {
        this.id = id;
    }

    @Override
    public MutableComponent format(Object... args) {
        return Component.translatable("help.pvz." + this.id, args).withStyle(ChatFormatting.RED);
    }
}