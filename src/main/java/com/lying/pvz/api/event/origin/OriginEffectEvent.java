package com.lying.pvz.api.event.origin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * 起源方块生效事件。
 *
 * <p>起源方块尝试将附近方块转化为精华矿时触发（服务端）。
 * 取消此事件可阻止本次转化。
 */
public class OriginEffectEvent extends Event implements ICancellableEvent {

    private final Level level;
    private final BlockPos pos;

    public OriginEffectEvent(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Level getLevel() {
        return this.level;
    }
}