package com.lying.pvz.api.event.invasion;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * 入侵波次事件。取消可阻止该波刷怪。
 */
public class InvasionWaveEvent extends Event implements ICancellableEvent {

    private final ServerPlayer player;
    private final int waveIndex;

    public InvasionWaveEvent(ServerPlayer player, int waveIndex) {
        this.player = player;
        this.waveIndex = waveIndex;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public int getWaveIndex() {
        return waveIndex;
    }
}