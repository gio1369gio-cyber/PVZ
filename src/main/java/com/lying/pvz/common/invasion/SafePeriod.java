package com.lying.pvz.common.invasion;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.attachment.player.PlayerMeta;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 安全期驱动器。
 *
 * <ul>
 *   <li>每个早晨（timeline DAY marker）：在线玩家 {@code safeDays--}</li>
 *   <li>早晨 +1200 tick：去重提示"距下次入侵还有几天"</li>
 *   <li>{@code safeDays == 0}：触发 {@link Invasion#enable()}，失败 200 tick 后重试</li>
 * </ul>
 *
 * <p><b>兜底</b>：命令 {@code /neoforge day} 改 rate 会使 {@code isAtTimeMarker} 跳过 marker。
 * 用 {@code periodTicks} + {@code lastMorningTotalTicks} 检测跨过的早晨次数，逐次补触发。
 */
public final class SafePeriod {

    private static final int NOTIFY_DELAY_TICKS = 1200;   // 早晨后 1 分钟
    private static final int RETRY_TICKS = 200;           // enable 失败后的重试间隔

    /** 上次早晨的 totalTicks。-1 = 未初始化。 */
    private static long lastMorningTotalTicks = -1;

    /** 挂起的提示目标 totalTicks（绝对值，随游戏时间推进；rate 加速时按比例缩短）。 */
    private static final Map<UUID, Long> PENDING_NOTIFY_AT = new HashMap<>();

    /** enable 失败后的重试计时。 */
    private static final Map<UUID, Integer> RETRY_TIMER = new HashMap<>();

    private SafePeriod() {}

    public static void tick(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) return;

        final long now = InvasionTime.totalTicks(level);
        final int period = InvasionTime.periodTicks(level);

        final int morningsPassed = detectMornings(level, now, period);

        if (morningsPassed > 0) {
            PvZMod.LOGGER.info("[SafePeriod] morning detected: count={}, now={}, period={}, last={}",
                    morningsPassed, now, period, lastMorningTotalTicks);
        }


        for (int i = 0; i < morningsPassed; i++) {
            onMorning(level);
        }

        for (ServerPlayer p : level.players()) {
            tickNotify(p,now);
            tickRetry(p);
        }
    }

    /**
     * 检测自上次以来跨过的早晨次数。
     *
     * <p>精确命中 → 1；漏检 → {@code (now - last) / period} 次，逐次补触发。
     */
    private static int detectMornings(ServerLevel level, long now, int period) {
        final boolean atMorning = InvasionTime.isAtMarker(level, ClockTimeMarkers.DAY);

        if (lastMorningTotalTicks < 0) {
            if (atMorning) {
                lastMorningTotalTicks = now;
                PvZMod.LOGGER.info("[SafePeriod] init at morning: now={}, skip decrement", now);
            }
            return 0;   // ← 关键：初始化不 -1
        }

        if (atMorning) {
            if (now - lastMorningTotalTicks < period / 2L) return 0;
            final long prev = lastMorningTotalTicks;
            final int missed = (int) ((now - lastMorningTotalTicks) / period);
            lastMorningTotalTicks += (long) Math.max(1, missed) * period;
            PvZMod.LOGGER.debug("[SafePeriod] morning detected: count={}, now={}, period={}, last {} -> {}",
                    Math.max(1, missed), now, period, prev, lastMorningTotalTicks);
            return Math.max(1, missed);
        }

        if (now - lastMorningTotalTicks >= period) {
            final long prev = lastMorningTotalTicks;
            final int missed = (int) ((now - lastMorningTotalTicks) / period);
            lastMorningTotalTicks += (long) missed * period;
            PvZMod.LOGGER.debug("[SafePeriod] morning missed: count={}, now={}, period={}, last {} -> {}",
                    missed, now, period, prev, lastMorningTotalTicks);
            return missed;
        }
        return 0;
    }

    private static void onMorning(ServerLevel level) {
        for (ServerPlayer p : level.players()) {
            if (p.isSpectator()) continue;

            final PlayerMeta meta = p.getData(PvZAttachments.PLAYER_META);
            if (meta.safeDays <= 0) continue;

            final int old = meta.safeDays;
            meta.safeDays--;
            PvZMod.LOGGER.debug("[SafePeriod] {} safeDays {} -> {}",
                    p.getGameProfile().name(), old, meta.safeDays);

            if (meta.safeDays > 0) {
                final long now = InvasionTime.totalTicks(level);
                final long delay = Math.max(20L, InvasionTime.periodTicks(level) / 20L);
                PENDING_NOTIFY_AT.put(p.getUUID(), now + delay);
            } else {
                PvZMod.LOGGER.info("[SafePeriod] {} safeDays hit 0, trying enable",
                        p.getGameProfile().name());
                tryEnable(p);
            }
        }
    }

    private static void tryEnable(ServerPlayer p) {
        final Invasion active = InvasionManager.getActive(p);
        if (active != null && active.isRunning()) return;

        final Invasion inv = new Invasion(p);
        inv.enable();
        if (!inv.isRunning()) {
            RETRY_TIMER.put(p.getUUID(), RETRY_TICKS);

            PvZMod.LOGGER.debug("[SafePeriod] enable failed for {}, retry in {} ticks",
                    p.getGameProfile().name(), RETRY_TICKS);

        }
    }

    private static void tickNotify(ServerPlayer p, long now) {
        final Long target = PENDING_NOTIFY_AT.get(p.getUUID());
        if (target == null) return;
        if (now < target) return;

        PENDING_NOTIFY_AT.remove(p.getUUID());
        final PlayerMeta meta = p.getData(PvZAttachments.PLAYER_META);
        if (meta.safeDays > 0 && meta.safeDays != meta.lastSafeDaysNotified) {
            meta.lastSafeDaysNotified = meta.safeDays;
            PvZMod.LOGGER.info("[SafePeriod] notify {} safeDays={}", p.getGameProfile().name(), meta.safeDays);
            PlayerUtil.sendMsgTo(p,
                    Component.translatable("invasion.pvz.safe_days", meta.safeDays)
                            .withStyle(ChatFormatting.YELLOW));
        } else {
            PvZMod.LOGGER.debug("[SafePeriod] notify skipped for {}: safeDays={}, last={}",
                    p.getGameProfile().name(), meta.safeDays, meta.lastSafeDaysNotified);
        }
    }

    private static void tickRetry(ServerPlayer p) {
        final Integer t = RETRY_TIMER.get(p.getUUID());
        if (t == null) return;
        if (t <= 1) {
            RETRY_TIMER.remove(p.getUUID());
            final PlayerMeta meta = p.getData(PvZAttachments.PLAYER_META);
            if (meta.safeDays <= 0) {
                tryEnable(p);
            }
        } else {
            RETRY_TIMER.put(p.getUUID(), t - 1);
        }
    }

    public static void clear() {
        lastMorningTotalTicks = -1;
        PENDING_NOTIFY_AT.clear();
        RETRY_TIMER.clear();
    }
}