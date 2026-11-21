package com.lying.pvz.api.event.invasion;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * 入侵结束事件。
 *
 * <p>在服务端触发。监听方可通过 {@link #getReason()} 判断结束原因。
 */
public class InvasionEndEvent extends Event {

    /**
     * 结束原因。
     */
    public enum Reason {
        /**
         * 波次打完 + 清场 + 宽限（玩家赢了）。
         */
        VICTORY,
        /**
         * 日常 tick 触发的结束、玩家掉线、维度切换等被动结束。
         */
        NATURAL,
        /**
         * 玩家登出。
         */
        LOGOUT,
        /**
         * 命令强制结束。
         */
        COMMAND,
        /**
         * 维度切换离开主世界。
         */
        DIMENSION
    }

    private final ServerPlayer player;
    private final Reason reason;

    public InvasionEndEvent(ServerPlayer player, Reason reason) {
        this.player = player;
        this.reason = reason;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public Reason getReason() {
        return reason;
    }
}