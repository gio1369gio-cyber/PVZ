package com.lying.pvz.api.event.raid;

import com.lying.pvz.api.raid.IChallenge;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * 挑战生命周期的基类事件。
 *
 * <p>三个子类分别在挑战开始、胜利、失败时触发。所有事件都在服务端触发。
 */
public class RaidEvent extends Event {

    private final IChallenge raid;

    public RaidEvent(IChallenge raid) {
        this.raid = raid;
    }

    public IChallenge getRaid() {
        return this.raid;
    }

    /**
     * 挑战开始时触发。
     */
    public static class RaidStartEvent extends RaidEvent {

        public RaidStartEvent(IChallenge raid) {
            super(raid);
        }
    }

    /**
     * 挑战胜利时触发。
     *
     * <p>取消此事件可阻止奖励发放，但挑战状态仍会正常转为"已结束"。
     */
    public static class RaidWinEvent extends RaidEvent implements ICancellableEvent {

        public RaidWinEvent(IChallenge raid) {
            super(raid);
        }
    }

    /**
     * 挑战失败时触发。
     */
    public static class RaidLossEvent extends RaidEvent {

        public RaidLossEvent(IChallenge raid) {
            super(raid);
        }
    }
}