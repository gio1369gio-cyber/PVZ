package com.lying.pvz.api.event.mission;

import com.lying.pvz.api.invasion.IMissionType;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * 任务阶段完成事件（服务端）。
 */
public class MissionStageCompleteEvent extends Event {

    private final ServerPlayer player;
    private final IMissionType type;
    private final int stage;

    public MissionStageCompleteEvent(ServerPlayer player, IMissionType type, int stage) {
        this.player = player;
        this.type = type;
        this.stage = stage;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public IMissionType getType() {
        return type;
    }

    public int getStage() {
        return stage;
    }
}