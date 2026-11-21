package com.lying.pvz.common.level.item.card;

import com.lying.pvz.api.card.ICardError;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 植物放置专属错误。
 */
public enum PlantPlacementErrors implements ICardError {
    GROUND_ERROR("ground"),
    WATER_ERROR("water"),
    UPGRADE_ERROR("upgrade"),
    OUTER_ERROR("outer"),
    OUTER_FULL("outer_full"),
    HEAL_WRONG_PLANT("heal_wrong_plant"),
    UPGRADE_WRONG_PLANT("upgrade_wrong_plant");

    private final String id;

    PlantPlacementErrors(String id) {
        this.id = id;
    }


    @Override
    public MutableComponent format(Object... args) {
        return Component.translatable("help.pvz."+ this.id, args).withStyle(ChatFormatting.RED);
    }
}