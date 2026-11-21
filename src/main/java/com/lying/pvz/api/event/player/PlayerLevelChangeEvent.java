package com.lying.pvz.api.event.player;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * 玩家等级变化事件（树等级）。
 *
 * <p>只在服务端触发。事件不继承 {@code ICancellableEvent}——等级已经修改，
 * 监听方只能观察，不能回滚。
 */
public class PlayerLevelChangeEvent extends PlayerEvent {

    private final int oldLevel;
    private final int newLevel;

    public PlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * 变化后的等级。
     */
    public int getCurrentLevel() {
        return this.newLevel;
    }

    /**
     * 变化前的等级。
     */
    public int getOldLevel() {
        return this.oldLevel;
    }

    /**
     * 本次变化是否为升级。
     */
    public boolean isLevelUp() {
        return this.newLevel > this.oldLevel;
    }
}