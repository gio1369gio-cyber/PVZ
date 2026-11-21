package com.lying.pvz.event.custom;

import com.lying.pvz.api.content.paz.IPlantType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class PeaGunShootEvent extends Event implements ICancellableEvent {

    private final Player player;
    private final ItemStack stack;
    private final IPlantType mode;

    public PeaGunShootEvent(Player player, ItemStack stack, IPlantType mode) {
        this.player = player;
        this.stack = stack;
        this.mode = mode;
    }

    public Player getPlayer() {
        return player;
    }

    public IPlantType getMode() {
        return mode;
    }

    public ItemStack getStack() {
        return stack;
    }
}