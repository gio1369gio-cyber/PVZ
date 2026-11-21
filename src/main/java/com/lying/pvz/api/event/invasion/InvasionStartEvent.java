package com.lying.pvz.api.event.invasion;

import com.lying.pvz.api.invasion.IInvasionType;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * 入侵开始事件。取消可阻止本次入侵。
 */
public class InvasionStartEvent extends Event implements ICancellableEvent {

    private final ServerPlayer player;
    private final IInvasionType type;

    public InvasionStartEvent(ServerPlayer player, IInvasionType type) {
        this.player = player;
        this.type = type;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public IInvasionType getType() {
        return type;
    }
}