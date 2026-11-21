package com.lying.pvz.common.attachment.player;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * 玩家杂项状态。纯服务端，不参与网络同步。
 */
public final class PlayerMeta implements ValueIOSerializable {

    /** 剩余安全天数。每个早晨（timeline DAY marker）递减 1，归零后触发 Invasion.enable()。 */
    public int safeDays = 0;

    /** 是否已发放首登礼包。 */
    public boolean starterGiftGiven = false;

    /** 上次提示"距下次入侵还有几天"的天数，用于每日去重；-1 表示未提示过。 */
    public int lastSafeDaysNotified = -1;

    /** 客户端音效节流计时。 */
    public int playSoundTick = 0;

    @Override
    public void serialize(ValueOutput out) {
        out.putInt("safe_days", safeDays);
        out.putInt("sound_tick", playSoundTick);
        out.putBoolean("starter_gift", starterGiftGiven);
        out.putInt("last_safe_days", lastSafeDaysNotified);
    }

    @Override
    public void deserialize(ValueInput in) {
        // 旧存档的 safe_ticks 不再读取；缺省 0，由 onPlayerLogin 的首登分支兜底。
        safeDays = in.getIntOr("safe_days", 0);
        playSoundTick = in.getIntOr("sound_tick", 0);
        starterGiftGiven = in.getBooleanOr("starter_gift", false);
        lastSafeDaysNotified = in.getIntOr("last_safe_days", -1);
    }
}